/*
 * #%L
 * prolobjectlink-jpi-ip
 * %%
 * Copyright (C) 2019 Prolobjectlink Project
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package org.worklogic.prolog.interprolog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.logicware.prolog.AbstractEngine;
import org.logicware.prolog.AbstractQuery;
import org.logicware.prolog.PrologQuery;
import org.logicware.prolog.PrologTerm;

import com.declarativa.interprolog.SolutionIterator;
import com.declarativa.interprolog.TermModel;

public final class InterPrologQuery extends AbstractQuery implements PrologQuery {

	private InterPrologParser ip = new InterPrologParser();
	private final List<String> variables = new ArrayList<String>();
	private List<Map<String, Object>> allSolutions = new ArrayList<Map<String, Object>>();
	private final Iterator<Map<String, Object>> itr;

	private void enumerateVariables(List<String> vector, TermModel term) {
		if (!(term instanceof TermVariable)) {
			TermModel[] terms = term.children;
			if (terms != null && terms.length > 0) {
				for (TermModel t : terms) {
					enumerateVariables(vector, t);
				}
			}
		} else if (!vector.contains(((TermVariable) term).getName())) {
			vector.add(((TermVariable) term).getName());
		}
	}

	public InterPrologQuery(AbstractEngine engine, String cache, String string) {
		super(engine);
		InterPrologEngine pe = engine.unwrap(InterPrologEngine.class);

		TermModel[] models = ip.parseTerms(string);
		for (TermModel term : models) {
			enumerateVariables(variables, term);
		}

		pe.engine.consultAbsolute(cache);
		String key = "_KEY_";
		StringBuilder b = new StringBuilder();
		b.append("findall(");
		Iterator<?> j = variables.iterator();
		while (j.hasNext()) {
			b.append(j.next());
			if (j.hasNext()) {
				b.append('/');
			}
		}
		b.append(',');
		b.append("(");
		b.append(string);
		b.append(')');
		b.append(',');
		b.append(key);
		b.append(')');

		SolutionIterator si = pe.engine.goal(b + ", buildTermModel(" + key + ",TM)", "[TM]");
		while (si.hasNext()) {
			Object[] bindings = si.next();
			for (Object object : bindings) {
				if (object instanceof TermModel) {
					TermModel list = (TermModel) object;
					while (list.getChildCount() > 0) {
						int index = variables.size() - 1;
						TermModel solvedTerm = (TermModel) list.getChild(0);
						Map<String, Object> solution = new HashMap<String, Object>();
						while (solvedTerm.getChildCount() > 0 && index >= 0) {
							solution.put(variables.get(index--), solvedTerm.getChild(1));
							solvedTerm = (TermModel) solvedTerm.getChild(0);
						}
						solution.put(variables.get(index--), solvedTerm);
						list = (TermModel) list.getChild(1);
						allSolutions.add(solution);
						index = 0;
					}
				}
			}
		}

		itr = allSolutions.iterator();

	}

	public boolean hasSolution() {
		return !allSolutions.isEmpty() && itr.hasNext();
	}

	public boolean hasMoreSolutions() {
		return !allSolutions.isEmpty() && itr.hasNext();
	}

	public PrologTerm[] oneSolution() {
		if (hasSolution()) {
			int index = 0;
			PrologTerm[] array = new PrologTerm[allSolutions.get(0).size()];
			for (Iterator<String> e = variables.iterator(); e.hasNext();) {
				array[index++] = InterPrologUtil.toTerm(getProvider(), allSolutions.get(0).get(e.next()));
			}
			return array;
		}
		return new PrologTerm[0];
	}

	public Map<String, PrologTerm> oneVariablesSolution() {
		if (hasSolution()) {
			Map<String, PrologTerm> varMap = new HashMap<String, PrologTerm>(allSolutions.get(0).size());
			for (Iterator<String> e = variables.iterator(); e.hasNext();) {
				String key = e.next();
				varMap.put(key, InterPrologUtil.toTerm(getProvider(), allSolutions.get(0).get(key)));
			}
			return varMap;
		}
		return new HashMap<String, PrologTerm>(0);
	}

	public PrologTerm[] nextSolution() {
		int index = 0;
		Map<String, Object> map = itr.next();
		PrologTerm[] array = new PrologTerm[map.size()];
		for (Iterator<String> e = variables.iterator(); e.hasNext();) {
			array[index++] = InterPrologUtil.toTerm(getProvider(), map.get(e.next()));
		}
		return array;
	}

	public Map<String, PrologTerm> nextVariablesSolution() {
		Map<String, Object> map = itr.next();
		Map<String, PrologTerm> varMap = new HashMap<String, PrologTerm>(map.size());
		for (Iterator<String> e = variables.iterator(); e.hasNext();) {
			String key = e.next();
			varMap.put(key, InterPrologUtil.toTerm(getProvider(), map.get(key)));
		}
		return varMap;
	}

	public PrologTerm[][] nSolutions(int n) {
		if (n > 0) {
			int m = 0;
			Map<String, PrologTerm>[] k = allVariablesSolutions();
			List<PrologTerm[]> all = new ArrayList<PrologTerm[]>();
			for (int i = 0; i < k.length && i < n; i++) {
				int index = 0;
				Map<String, PrologTerm> map = k[i];
				PrologTerm[] solutions = new PrologTerm[map.size()];
				for (Iterator<String> e = variables.iterator(); e.hasNext();) {
					solutions[index++] = map.get(e.next());
				}
				m = solutions.length > m ? solutions.length : m;
				all.add(solutions);
			}

			PrologTerm[][] allTable = new PrologTerm[n][m];
			for (int i = 0; i < n; i++) {
				PrologTerm[] solutionArray = all.get(i);
				System.arraycopy(solutionArray, 0, allTable[i], 0, m);
			}
			return allTable;
		}
		return new PrologTerm[0][0];
	}

	@SuppressWarnings("unchecked")
	public Map<String, PrologTerm>[] nVariablesSolutions(int n) {
		if (n > 0) {
			Map<String, PrologTerm>[] solutionMaps = new HashMap[n];
			for (int i = 0; i < allSolutions.size() && i < n; i++) {
				Map<String, Object> map = allSolutions.get(i);
				Map<String, PrologTerm> varMap = new HashMap<String, PrologTerm>(map.size());
				for (Iterator<String> e = variables.iterator(); e.hasNext();) {
					String key = e.next();
					varMap.put(key, InterPrologUtil.toTerm(getProvider(), map.get(key)));
				}
				solutionMaps[i] = varMap;
			}
			return solutionMaps;
		}
		return new HashMap[0];
	}

	public PrologTerm[][] allSolutions() {
		// n:solutionCount, m:solutionSize
		int n = 0;
		int m = 0;
		Map<String, PrologTerm>[] k = allVariablesSolutions();
		List<PrologTerm[]> all = new ArrayList<PrologTerm[]>();
		for (int i = 0; i < k.length; i++) {
			int index = 0;
			Map<String, PrologTerm> map = k[i];
			PrologTerm[] solutions = new PrologTerm[map.size()];
			for (Iterator<String> e = variables.iterator(); e.hasNext();) {
				solutions[index++] = map.get(e.next());
			}
			m = solutions.length > m ? solutions.length : m;
			n++;
			all.add(solutions);
		}

		PrologTerm[][] allTable = new PrologTerm[n][m];
		for (int i = 0; i < n; i++) {
			PrologTerm[] solutionArray = all.get(i);
			System.arraycopy(solutionArray, 0, allTable[i], 0, m);
		}
		return allTable;
	}

	@SuppressWarnings("unchecked")
	public Map<String, PrologTerm>[] allVariablesSolutions() {
		Map<String, PrologTerm>[] m = new HashMap[allSolutions.size()];
		for (int i = 0; i < allSolutions.size(); i++) {
			Map<String, Object> map = allSolutions.get(i);
			Map<String, PrologTerm> varMap = new HashMap<String, PrologTerm>(map.size());
			for (Iterator<String> e = variables.iterator(); e.hasNext();) {
				String key = e.next();
				varMap.put(key, InterPrologUtil.toTerm(getProvider(), map.get(key)));
			}
			m[i] = varMap;
		}
		return m;
	}

	public List<Map<String, PrologTerm>> all() {
		List<Map<String, PrologTerm>> m = new ArrayList<Map<String, PrologTerm>>(allSolutions.size());
		for (int i = 0; i < allSolutions.size(); i++) {
			Map<String, Object> map = allSolutions.get(i);
			Map<String, PrologTerm> varMap = new HashMap<String, PrologTerm>(map.size());
			for (Iterator<String> e = variables.iterator(); e.hasNext();) {
				String key = e.next();
				varMap.put(key, InterPrologUtil.toTerm(getProvider(), map.get(key)));
			}
			m.add(varMap);
		}
		return m;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + allSolutions.hashCode();
		result = prime * result + ip.hashCode();
		result = prime * result + variables.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		InterPrologQuery other = (InterPrologQuery) obj;
		if (!allSolutions.equals(other.allSolutions)) {
			return false;
		} else if (!ip.equals(other.ip)) {
			return false;
		} else if (!variables.equals(other.variables)) {
			return false;
		}
		return true;
	}

	public void dispose() {
		allSolutions.clear();
	}

}

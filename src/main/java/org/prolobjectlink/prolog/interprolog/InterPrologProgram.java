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
package org.prolobjectlink.prolog.interprolog;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.prolobjectlink.ArrayIterator;
import org.prolobjectlink.RuntimeError;
import org.prolobjectlink.prolog.PrologClauses;

import com.declarativa.interprolog.TermModel;

public final class InterPrologProgram extends AbstractSet<List<TermModel>> {

	//
	private final InterPrologParser parser = new InterPrologParser();

	// program initializations goals
	private final List<TermModel> goals = new LinkedList<TermModel>();

	// list of directives goals
	private final List<TermModel> directives = new LinkedList<TermModel>();

	// program (data base) in read order
	private final LinkedHashMap<String, List<TermModel>> clauses = new LinkedHashMap<String, List<TermModel>>();

	private String getKey(TermModel clause) {
		String key = clause.getFunctorArity();
		if (key.equals(":-/2")) {
			key = ((TermModel) clause.getChild(0)).getFunctorArity();
		}
		return key;
	}

	private String getKey(List<TermModel> cls) {
		String msg = "Empty clause list";
		if (!cls.isEmpty()) {
			TermModel t = cls.get(0);
			String key = t.getFunctorArity();
			return key;
		}
		throw new RuntimeError(msg);
	}

	public List<TermModel> get(String key) {
		return clauses.get(key);
	}

	public void add(TermModel clause) {
		String key = getKey(clause);
		List<TermModel> family = get(key);
		if (family == null) {
			family = new LinkedList<TermModel>();
			family.add(clause);
			clauses.put(key, family);
		} else if (!family.contains(clause)) {
			family.add(clause);
		}
	}

	@Override
	public boolean add(List<TermModel> cls) {
		String key = getKey(cls);
		List<TermModel> family = get(key);
		if (family != null) {
			family.addAll(cls);
		} else {
			clauses.put(key, cls);
		}
		return true;
	}

	public void add(InterPrologProgram program) {
		goals.addAll(program.getGoals());
		clauses.putAll(program.getClauses());
		directives.addAll(program.getDirectives());
	}

	@Override
	public boolean remove(Object o) {

		if (o instanceof TermModel) {
			TermModel c = (TermModel) o;
			String key = getKey(c);
			List<TermModel> family = get(key);
			if (family != null) {
				return family.remove(c);
			}
		}

		else if (o instanceof PrologClauses) {
			PrologClauses cs = (PrologClauses) o;
			String key = cs.getIndicator();
			List<TermModel> oldFamily = clauses.remove(key);
			return oldFamily != null;
		}

		return false;
	}

	public void push(TermModel clause) {
		String key = getKey(clause);
		List<TermModel> family = clauses.remove(key);
		List<TermModel> cs = new LinkedList<TermModel>();
		if (family != null && !family.contains(clause)) {
			cs.add(clause);
			for (TermModel term : family) {
				cs.add(term);
			}
		} else {
			cs.add(clause);
		}
		clauses.put(key, cs);
	}

	public void removeAll(String key) {
		clauses.remove(key);
	}

	public void removeAll(String functor, int arity) {
		removeAll(functor + "/" + arity);
	}

	public List<TermModel> getDirectives() {
		return directives;
	}

	public boolean addDirective(TermModel directive) {
		return directives.add(directive);
	}

	public boolean removeDirective(TermModel directive) {
		return directives.remove(directive);
	}

	public List<TermModel> getGoals() {
		return goals;
	}

	public boolean addGoal(TermModel goal) {
		return goals.add(goal);
	}

	public boolean removeGoal(TermModel goal) {
		return goals.remove(goal);
	}

	public Set<String> getIndicators() {
		return clauses.keySet();
	}

	public Map<String, List<TermModel>> getClauses() {
		return clauses;
	}

	@Override
	public String toString() {

		StringBuilder families = new StringBuilder();

		if (!directives.isEmpty()) {
			Iterator<TermModel> i = directives.iterator();
			while (i.hasNext()) {
				families.append(":-");
				families.append(i.next());
				families.append(i.hasNext() ? "\n" : "\n\n");
			}
		}

		if (!clauses.isEmpty()) {
			Iterator<List<TermModel>> i = iterator();
			while (i.hasNext()) {
				List<TermModel> l = i.next();
				Iterator<TermModel> j = l.iterator();
				while (j.hasNext()) {
					TermModel term = j.next();
					String key = term.getFunctorArity();
					if (term.getChildCount() == 2 && key.equals(":-/2")) {
						TermModel h = (TermModel) term.getChild(0);
						TermModel b = (TermModel) term.getChild(1);
						families.append(h);
						families.append(" :- ");
						families.append('\n');
						families.append('\t');
						TermModel[] array = parser.parseTerms(b);
						Iterator<TermModel> k = new ArrayIterator<TermModel>(array);
						while (k.hasNext()) {
							TermModel item = k.next();
							families.append(item);
							if (k.hasNext()) {
								families.append(',');
								families.append('\n');
								families.append('\t');
							}
						}
					} else {
						families.append(term);
					}
					families.append('.');
					families.append('\n');
				}
				if (i.hasNext()) {
					families.append('\n');
				}
			}
		}

		return "" + families + "";
	}

	@Override
	public Iterator<List<TermModel>> iterator() {
		return clauses.values().iterator();
	}

	@Override
	public int size() {
		int size = 0;
		Iterator<List<TermModel>> i = iterator();
		while (i.hasNext()) {
			List<TermModel> l = i.next();
			Iterator<TermModel> j = l.iterator();
			while (j.hasNext()) {
				j.next();
				size++;
			}
		}
		return size;
	}

	@Override
	public void clear() {
		goals.clear();
		clauses.clear();
		directives.clear();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + clauses.hashCode();
		result = prime * result + directives.hashCode();
		result = prime * result + goals.hashCode();
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
		InterPrologProgram other = (InterPrologProgram) obj;
		if (!clauses.equals(other.clauses)) {
			return false;
		}
		if (!directives.equals(other.directives)) {
			return false;
		}
		return goals.equals(other.goals);
	}

}

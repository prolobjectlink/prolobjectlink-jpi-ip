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

import static org.prolobjectlink.logging.LoggerConstants.IO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.prolobjectlink.ArrayIterator;
import org.prolobjectlink.logging.LoggerUtils;
import org.prolobjectlink.prolog.AbstractEngine;
import org.prolobjectlink.prolog.PredicateIndicator;
import org.prolobjectlink.prolog.PrologClause;
import org.prolobjectlink.prolog.PrologEngine;
import org.prolobjectlink.prolog.PrologIndicator;
import org.prolobjectlink.prolog.PrologOperator;
import org.prolobjectlink.prolog.PrologProvider;
import org.prolobjectlink.prolog.PrologQuery;
import org.prolobjectlink.prolog.PrologTerm;
import org.prolobjectlink.prolog.PrologTermType;

import com.declarativa.interprolog.AbstractPrologEngine;
import com.declarativa.interprolog.SolutionIterator;
import com.declarativa.interprolog.TermModel;
import com.xsb.interprolog.NativeEngine;

/**
 * 
 * @author Jose Zalacain
 * @since 1.0
 */
public abstract class InterPrologEngine extends AbstractEngine implements PrologEngine {

	// used only for findall list result
	protected static final String KEY = "X";

	// JPL use for fact clauses true prolog term
	protected static final TermModel BODY = new TermModel("true");

	// cache file in OS temporal directory
	private static String cache = null;

	// XSB native engine
	public static AbstractPrologEngine engine;

	// parser to obtain terms from text
	private final InterPrologParser parser = new InterPrologParser();

	// main memory prolog program
	protected InterPrologProgram program = new InterPrologProgram();

	// path to binary engine directory
//	private static String xsbPath = "C:\\Program Files (x86)\\XSB\\bin";
	private static String xsbPath = "C:\\Program Files (x86)\\XSB\\config\\x64-pc-windows\\bin";

	static {
		try {
			File f = File.createTempFile("prolobjectlink-jpi-ip-cache-", ".pl");
			cache = f.getCanonicalPath().replace(File.separatorChar, '/');
			InterPrologEngine.engine = new NativeEngine(xsbPath);
//			InterPrologEngine.engine = new XSBSubprocessEngine(xsbPath);
			InterPrologEngine.engine.setAllowSimultaneousThreads(true);
		} catch (IOException e) {
			LoggerUtils.error(InterPrologEngine.class, IO, e);
		}
	}

	protected InterPrologEngine(PrologProvider provider) {
		super(provider);
	}

	protected InterPrologEngine(PrologProvider provider, String path) {
		super(provider);
		consult(path);
	}

	public final void include(String path) {
		program.add(parser.parseProgram(path));
		persist(cache);
	}

	public final void consult(String path) {
		program = parser.parseProgram(path);
		persist(cache);
	}

	public final void persist(String path) {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new FileOutputStream(path, false));
			writer.print(program);
		} catch (FileNotFoundException e) {
			LoggerUtils.error(getClass(), IO + cache, e);
		}

		finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	public final void abolish(String functor, int arity) {
		program.removeAll(functor, arity);
		persist(cache);
	}

	public final void asserta(String stringClause) {
		asserta(parser.parseTerm(stringClause));
	}

	public final void asserta(PrologTerm head, PrologTerm... body) {
		asserta(fromTerm(head, body, TermModel.class));
	}

	private void asserta(TermModel t) {
		program.push(t);
		persist(cache);
	}

	public final void assertz(String stringClause) {
		assertz(parser.parseTerm(stringClause));
	}

	public final void assertz(PrologTerm head, PrologTerm... body) {
		assertz(fromTerm(head, body, TermModel.class));
	}

	private void assertz(TermModel t) {
		program.add(t);
		persist(cache);
	}

	public final boolean clause(String stringClause) {
		return clause(parser.parseTerm(stringClause));
	}

	public final boolean clause(PrologTerm head, PrologTerm... body) {
		return clause(fromTerm(head, body, TermModel.class));
	}

	private boolean clause(TermModel t) {
		TermModel h = t;
		TermModel b = BODY;
		if (t.getFunctorArity().equals(":-/2")) {
			h = t.children[0];
			b = t.children[1];
		}
		return new InterPrologQuery(

				this, cache, "clause(" + h + "," + b + ")"

		).hasSolution();
	}

	public final void retract(String stringClause) {
		retract(parser.parseTerm(stringClause));
	}

	public final void retract(PrologTerm head, PrologTerm... body) {
		retract(provider.fromTerm(head, body, TermModel.class));
	}

	private void retract(TermModel t) {
		program.remove(t);
		persist(cache);
	}

	public final PrologQuery query(String stringQuery) {
		return new InterPrologQuery(this, cache, stringQuery);
	}

	public final PrologQuery query(PrologTerm[] terms) {
		Iterator<PrologTerm> i = new ArrayIterator<PrologTerm>(terms);
		StringBuilder buffer = new StringBuilder();
		while (i.hasNext()) {
			buffer.append(i.next());
			if (i.hasNext()) {
				buffer.append(',');
			}
		}
		return query("" + buffer + "");
	}

	public final PrologQuery query(PrologTerm term, PrologTerm... terms) {
		Iterator<PrologTerm> i = new ArrayIterator<PrologTerm>(terms);
		StringBuilder buffer = new StringBuilder();
		buffer.append("" + term + "");
		while (i.hasNext()) {
			buffer.append(',');
			buffer.append(i.next());
		}
		return query("" + buffer + "");
	}

	public final void operator(int priority, String specifier, String operator) {
		engine.deterministicGoal("op(" + priority + "," + specifier + ", " + operator + ")");
	}

	public final boolean currentPredicate(String functor, int arity) {
		return engine.deterministicGoal("current_predicate(" + functor + "/" + arity + ")");
	}

	public final boolean currentOperator(int priority, String specifier, String operator) {
		return engine.deterministicGoal("current_op(" + priority + "," + specifier + ", " + operator + ")");
	}

	public final Set<PrologOperator> currentOperators() {
		Set<PrologOperator> operators = new HashSet<PrologOperator>();
		String stringQuery = "findall(P/S/O,current_op(P,S,O)," + KEY + "), buildTermModel(" + KEY + ",TM)";
		SolutionIterator si = engine.goal(stringQuery, "[TM]");
		while (si.hasNext()) {
			Object[] bindings = si.next();
			for (Object object : bindings) {
				if (object instanceof TermModel) {
					TermModel list = (TermModel) object;
					while (list.getChildCount() > 0) {
						TermModel solvedTerm = (TermModel) list.getChild(0);
						Integer p = (Integer) solvedTerm.children[0].children[0].node;
						String s = (String) solvedTerm.children[0].children[1].node;
						String n = (String) solvedTerm.children[1].node;
						PrologOperator o = new InterPrologOperator(p, s, n);
						list = (TermModel) list.getChild(1);
						operators.add(o);
					}
				}
			}
		}
		return operators;
	}

	public final int getProgramSize() {
		return program.size();
	}

	public final Set<PrologIndicator> getPredicates() {
		Set<PrologIndicator> pis = predicates();
		Set<PrologIndicator> builtins = getBuiltIns();
		for (PrologIndicator prologIndicator : pis) {
			if (!builtins.contains(prologIndicator)) {
				pis.remove(prologIndicator);
			}
		}
		return pis;
	}

	public final Set<PrologIndicator> getBuiltIns() {
		Set<PrologIndicator> pis = predicates();
		Set<PrologClause> clauses = getProgramClauses();
		for (PrologClause prologClause : clauses) {
			PrologIndicator pi = prologClause.getPrologIndicator();
			if (pis.contains(pi)) {
				pis.remove(pi);
			}
		}
		return pis;
	}

	private Set<PrologIndicator> predicates() {
		Set<PrologIndicator> indicators = new HashSet<PrologIndicator>();
		String stringQuery = "findall(X/Y,current_predicate(X/Y)," + KEY + "), buildTermModel(" + KEY + ",TM)";
		SolutionIterator si = engine.goal(stringQuery, "[TM]");
		while (si.hasNext()) {
			Object[] bindings = si.next();
			for (Object object : bindings) {
				if (object instanceof TermModel) {
					TermModel list = (TermModel) object;
					while (list.getChildCount() > 0) {
						TermModel solvedTerm = (TermModel) list.getChild(0);
						String functor = (String) solvedTerm.children[0].node;
						Integer arity = (Integer) solvedTerm.children[1].node;
						PredicateIndicator pi = new PredicateIndicator(functor, arity);
						list = (TermModel) list.getChild(1);
						indicators.add(pi);
					}
				}
			}
		}
		return indicators;
	}

	public final Iterator<PrologClause> iterator() {
		List<PrologClause> cls = new ArrayList<PrologClause>();
		for (List<TermModel> family : program.getClauses().values()) {
			for (TermModel clause : family) {
				if (clause.getFunctorArity().equals(":-/2")) {
					PrologTerm head = toTerm(clause.children[0], PrologTerm.class);
					PrologTerm body = toTerm(clause.children[1], PrologTerm.class);
					if (body.getType() != PrologTermType.TRUE_TYPE) {
						cls.add(new InterPrologClause(provider, head, body, false, false, false));
					} else {
						cls.add(new InterPrologClause(provider, head, false, false, false));
					}
				} else {
					cls.add(new InterPrologClause(provider, toTerm(clause, PrologTerm.class), false, false, false));
				}
			}
		}
		return new PrologProgramIterator(cls);
	}

	public final void dispose() {
		File c = new File(cache);
		c.deleteOnExit();
		engine.shutdown();
		program.clear();
	}

	public final String getCache() {
		return cache;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((program == null) ? 0 : program.hashCode());
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
		InterPrologEngine other = (InterPrologEngine) obj;
		if (program == null) {
			if (other.program != null)
				return false;
		} else if (!program.equals(other.program)) {
			return false;
		}
		return true;
	}

}

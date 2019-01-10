/*
 * #%L
 * prolobjectlink-jpi-ip
 * %%
 * Copyright (C) 2012 - 2019 WorkLogic Project
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

import java.util.Iterator;
import java.util.Set;

import org.logicware.prolog.AbstractEngine;
import org.logicware.prolog.PrologClause;
import org.logicware.prolog.PrologEngine;
import org.logicware.prolog.PrologIndicator;
import org.logicware.prolog.PrologOperator;
import org.logicware.prolog.PrologProvider;
import org.logicware.prolog.PrologQuery;
import org.logicware.prolog.PrologTerm;

import com.declarativa.interprolog.AbstractPrologEngine;

public class InterPrologEngine extends AbstractEngine implements PrologEngine {

	public AbstractPrologEngine engine;

	protected InterPrologEngine(PrologProvider provider) {
		super(provider);
	}

	public Iterator<PrologClause> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	public void include(String path) {
		engine.consultAbsolute(path);
	}

	public void consult(String path) {
		engine.consultAbsolute(path);
	}

	public void persist(String path) {
		// TODO Auto-generated method stub

	}

	public void abolish(String functor, int arity) {
		// TODO Auto-generated method stub

	}

	public void asserta(String stringClause) {
		// TODO Auto-generated method stub

	}

	public void asserta(PrologTerm head, PrologTerm... body) {
		// TODO Auto-generated method stub

	}

	public void assertz(String stringClause) {
		// TODO Auto-generated method stub

	}

	public void assertz(PrologTerm head, PrologTerm... body) {
		// TODO Auto-generated method stub

	}

	public boolean clause(String stringClause) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean clause(PrologTerm head, PrologTerm... body) {
		// TODO Auto-generated method stub
		return false;
	}

	public void retract(String stringClause) {
		// TODO Auto-generated method stub

	}

	public void retract(PrologTerm head, PrologTerm... body) {
		// TODO Auto-generated method stub

	}

	public PrologQuery query(String query) {
		// TODO Auto-generated method stub
		return null;
	}

	public PrologQuery query(PrologTerm[] terms) {
		// TODO Auto-generated method stub
		return null;
	}

	public PrologQuery query(PrologTerm term, PrologTerm... terms) {
		// TODO Auto-generated method stub
		return null;
	}

	public void operator(int priority, String specifier, String operator) {
		// TODO Auto-generated method stub

	}

	public boolean currentPredicate(String functor, int arity) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean currentOperator(int priority, String specifier, String operator) {
		// TODO Auto-generated method stub
		return false;
	}

	public Set<PrologOperator> currentOperators() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getProgramSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Set<PrologIndicator> getPredicates() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<PrologIndicator> getBuiltIns() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getLicense() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getVersion() {
		return engine.getPrologVersion();
	}

	public String getName() {
		return engine.firstJavaMessageName;
	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((engine == null) ? 0 : engine.hashCode());
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
		if (engine == null) {
			if (other.engine != null)
				return false;
		} else if (!engine.equals(other.engine)) {
			return false;
		}
		return true;
	}

}

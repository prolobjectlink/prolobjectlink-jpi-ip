/*
 * #%L
 * prolobjectlink-jpi-ip
 * %%
 * Copyright (C) 2012 - 2017 WorkLogic Project
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

import java.util.List;
import java.util.Map;

import org.logicware.prolog.AbstractEngine;
import org.logicware.prolog.AbstractQuery;
import org.logicware.prolog.PrologQuery;
import org.logicware.prolog.PrologTerm;

import com.declarativa.interprolog.SolutionIterator;

public final class InterPrologQuery extends AbstractQuery implements PrologQuery {

	private SolutionIterator i;

	public InterPrologQuery(AbstractEngine engine) {
		super(engine);
	}

	public boolean hasSolution() {
		return i != null && i.hasNext();
	}

	public boolean hasMoreSolutions() {
		return i != null && i.hasNext();
	}

	public PrologTerm[] oneSolution() {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<String, PrologTerm> oneVariablesSolution() {
		// TODO Auto-generated method stub
		return null;
	}

	public PrologTerm[] nextSolution() {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<String, PrologTerm> nextVariablesSolution() {
		// TODO Auto-generated method stub
		return null;
	}

	public PrologTerm[][] nSolutions(int n) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<String, PrologTerm>[] nVariablesSolutions(int n) {
		// TODO Auto-generated method stub
		return null;
	}

	public PrologTerm[][] allSolutions() {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<String, PrologTerm>[] allVariablesSolutions() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Map<String, PrologTerm>> all() {
		// TODO Auto-generated method stub
		return null;
	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

}

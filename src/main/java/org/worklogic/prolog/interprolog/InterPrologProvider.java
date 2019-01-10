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

import org.logicware.prolog.AbstractProvider;
import org.logicware.prolog.PrologAtom;
import org.logicware.prolog.PrologConverter;
import org.logicware.prolog.PrologDouble;
import org.logicware.prolog.PrologFloat;
import org.logicware.prolog.PrologInteger;
import org.logicware.prolog.PrologList;
import org.logicware.prolog.PrologLong;
import org.logicware.prolog.PrologProvider;
import org.logicware.prolog.PrologStructure;
import org.logicware.prolog.PrologTerm;
import org.logicware.prolog.PrologVariable;

public abstract class InterPrologProvider extends AbstractProvider implements PrologProvider {

	public InterPrologProvider(PrologConverter<?> converter) {
		super(converter);
	}

	public PrologTerm parseTerm(String term) {
		// TODO Auto-generated method stub
		return null;
	}

	public PrologTerm[] parseTerms(String stringTerms) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isCompliant() {
		return false;
	}

	public PrologTerm prologNil() {
		return new InterPrologNil(this);
	}

	public PrologTerm prologCut() {
		return new InterPrologCut(this);
	}

	public PrologTerm prologFail() {
		return new InterPrologFail(this);
	}

	public PrologTerm prologTrue() {
		return new InterPrologTrue(this);
	}

	public PrologTerm prologFalse() {
		return new InterPrologFalse(this);
	}

	public PrologTerm prologEmpty() {
		return new InterPrologEmpty(this);
	}

	public PrologAtom newAtom(String functor) {
		return new InterPrologAtom(this, functor);
	}

	public PrologFloat newFloat(Number value) {
		return new InterPrologFloat(this, value);
	}

	public PrologDouble newDouble(Number value) {
		return new InterPrologDouble(this, value);
	}

	public PrologInteger newInteger(Number value) {
		return new InterPrologInteger(this, value);
	}

	public PrologLong newLong(Number value) {
		return new InterPrologLong(this, value);
	}

	public PrologVariable newVariable(int position) {
		return new InterPrologVariable(this, position);
	}

	public PrologVariable newVariable(String name, int position) {
		return new InterPrologVariable(this, name, position);
	}

	public PrologList newList() {
		return new InterPrologList(this);
	}

	public PrologList newList(PrologTerm[] arguments) {
		return new InterPrologList(this, arguments);
	}

	public PrologList newList(PrologTerm head, PrologTerm tail) {
		return new InterPrologList(this, head, tail);
	}

	public PrologList newList(PrologTerm[] arguments, PrologTerm tail) {
		return new InterPrologList(this, arguments, tail);
	}

	public PrologStructure newStructure(String functor, PrologTerm... arguments) {
		return new InterPrologStructure(this, functor, arguments);
	}

	public PrologTerm newStructure(PrologTerm left, String operator, PrologTerm right) {
		return new InterPrologStructure(this, left, operator, right);
	}
}

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

import static org.prolobjectlink.prolog.AbstractConverter.SIMPLE_ATOM_REGEX;

import java.util.HashMap;
import java.util.Map;

import org.prolobjectlink.prolog.AbstractProvider;
import org.prolobjectlink.prolog.PrologAtom;
import org.prolobjectlink.prolog.PrologConverter;
import org.prolobjectlink.prolog.PrologDouble;
import org.prolobjectlink.prolog.PrologFloat;
import org.prolobjectlink.prolog.PrologInteger;
import org.prolobjectlink.prolog.PrologList;
import org.prolobjectlink.prolog.PrologLong;
import org.prolobjectlink.prolog.PrologProvider;
import org.prolobjectlink.prolog.PrologStructure;
import org.prolobjectlink.prolog.PrologTerm;
import org.prolobjectlink.prolog.PrologVariable;

public abstract class InterPrologProvider extends AbstractProvider implements PrologProvider {

	protected static final Map<String, String> varCache = new HashMap<String, String>();

	public InterPrologProvider(PrologConverter<?> converter) {
		super(converter);
	}

	public PrologTerm parseTerm(String term) {
		return toTerm(new InterPrologParser().parseTerm(term), PrologTerm.class);
	}

	public PrologTerm[] parseTerms(String stringTerms) {
		return toTermArray(new InterPrologParser().parseTerms(stringTerms), PrologTerm[].class);
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
		if (!functor.matches(SIMPLE_ATOM_REGEX)) {
			return new InterPrologAtom(this, "'" + functor + "'");
		}
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

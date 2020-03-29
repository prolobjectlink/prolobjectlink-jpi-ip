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
package io.github.prolobjectlink.prolog.interprolog;

import static io.github.prolobjectlink.prolog.AbstractConverter.SIMPLE_ATOM_REGEX;

import java.util.HashMap;
import java.util.Map;

import io.github.prolobjectlink.prolog.AbstractProvider;
import io.github.prolobjectlink.prolog.PrologAtom;
import io.github.prolobjectlink.prolog.PrologConverter;
import io.github.prolobjectlink.prolog.PrologDouble;
import io.github.prolobjectlink.prolog.PrologFloat;
import io.github.prolobjectlink.prolog.PrologInteger;
import io.github.prolobjectlink.prolog.PrologList;
import io.github.prolobjectlink.prolog.PrologLogger;
import io.github.prolobjectlink.prolog.PrologLong;
import io.github.prolobjectlink.prolog.PrologProvider;
import io.github.prolobjectlink.prolog.PrologStructure;
import io.github.prolobjectlink.prolog.PrologTerm;
import io.github.prolobjectlink.prolog.PrologVariable;

/**
 * 
 * @author Jose Zalacain
 * @since 1.0
 */
public abstract class InterPrologProvider extends AbstractProvider implements PrologProvider {

	protected static final Map<String, String> varCache = new HashMap<String, String>();
	protected static final PrologLogger logger = new InterPrologLogger();

	public InterPrologProvider(PrologConverter<?> converter) {
		super(converter);
	}

	public final PrologTerm parseTerm(String term) {
		return toTerm(InterPrologUtil.parseTerm(term), PrologTerm.class);
	}

	public final PrologTerm[] parseTerms(String stringTerms) {
		return toTermArray(InterPrologUtil.parseTerms(stringTerms), PrologTerm[].class);
	}

	public final boolean isCompliant() {
		return false;
	}

	public final PrologTerm prologNil() {
		return new InterPrologNil(this);
	}

	public final PrologTerm prologCut() {
		return new InterPrologCut(this);
	}

	public final PrologTerm prologFail() {
		return new InterPrologFail(this);
	}

	public final PrologTerm prologTrue() {
		return new InterPrologTrue(this);
	}

	public final PrologTerm prologFalse() {
		return new InterPrologFalse(this);
	}

	public final PrologTerm prologEmpty() {
		return new InterPrologEmpty(this);
	}

	public final PrologTerm prologInclude(String file) {
		return newStructure("consult", newAtom(file));
	}

	public final PrologAtom newAtom(String functor) {
		if (!functor.matches(SIMPLE_ATOM_REGEX)) {
			return new InterPrologAtom(this, "'" + functor + "'");
		}
		return new InterPrologAtom(this, functor);
	}

	public final PrologFloat newFloat(Number value) {
		return new InterPrologFloat(this, value);
	}

	public final PrologDouble newDouble(Number value) {
		return new InterPrologDouble(this, value);
	}

	public final PrologInteger newInteger(Number value) {
		return new InterPrologInteger(this, value);
	}

	public final PrologLong newLong(Number value) {
		return new InterPrologLong(this, value);
	}

	public final PrologVariable newVariable(int position) {
		return new InterPrologVariable(this, position);
	}

	public final PrologVariable newVariable(String name, int position) {
		return new InterPrologVariable(this, name, position);
	}

	public final PrologList newList() {
		return new InterPrologList(this);
	}

	public final PrologList newList(PrologTerm[] arguments) {
		return new InterPrologList(this, arguments);
	}

	public final PrologList newList(PrologTerm head, PrologTerm tail) {
		return new InterPrologList(this, head, tail);
	}

	public final PrologList newList(PrologTerm[] arguments, PrologTerm tail) {
		return new InterPrologList(this, arguments, tail);
	}

	public final PrologStructure newStructure(String functor, PrologTerm... arguments) {
		return new InterPrologStructure(this, functor, arguments);
	}

	public final PrologTerm newStructure(PrologTerm left, String operator, PrologTerm right) {
		return new InterPrologStructure(this, left, operator, right);
	}

	public PrologTerm newReference(Object reference) {
		throw new UnsupportedOperationException("newReference(Object reference)");
	}

	public final PrologLogger getLogger() {
		return logger;
	}
}

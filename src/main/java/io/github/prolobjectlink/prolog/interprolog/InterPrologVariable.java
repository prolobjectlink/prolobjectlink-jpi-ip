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

import static io.github.prolobjectlink.prolog.PrologTermType.VARIABLE_TYPE;

import io.github.prolobjectlink.prolog.ArityError;
import io.github.prolobjectlink.prolog.FunctorError;
import io.github.prolobjectlink.prolog.IndicatorError;
import io.github.prolobjectlink.prolog.PrologProvider;
import io.github.prolobjectlink.prolog.PrologTerm;
import io.github.prolobjectlink.prolog.PrologVariable;

/**
 * 
 * @author Jose Zalacain
 * @since 1.0
 */
class InterPrologVariable extends InterPrologTerm implements PrologVariable {

	InterPrologVariable(PrologProvider provider, int position) {
		super(VARIABLE_TYPE, provider, new TermVariable("_", position));
	}

	InterPrologVariable(PrologProvider provider, String name) {
		super(VARIABLE_TYPE, provider, new TermVariable(name, vIndexer++));
	}

	InterPrologVariable(PrologProvider provider, String name, int position) {
		super(VARIABLE_TYPE, provider, new TermVariable(name, position));
	}

	public int getArity() {
		throw new ArityError(this);
	}

	public String getFunctor() {
		throw new FunctorError(this);
	}

	public String getIndicator() {
		throw new IndicatorError(this);
	}

	public boolean hasIndicator(String functor, int arity) {
		return false;
	}

	public int getPosition() {
		return vIndex;
	}

	public boolean isAnonymous() {
		return getName().equals("_");
	}

	public final String getName() {
		return ((TermVariable) value).getName();
	}

	public final void setName(String name) {
		TermVariable old = (TermVariable) value;
		value = new TermVariable(name, old.getPosition());
	}

	public final PrologTerm[] getArguments() {
		return new InterPrologFloat[0];
	}

}

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

import static org.prolobjectlink.prolog.PrologTermType.VARIABLE_TYPE;

import org.prolobjectlink.prolog.ArityError;
import org.prolobjectlink.prolog.FunctorError;
import org.prolobjectlink.prolog.IndicatorError;
import org.prolobjectlink.prolog.PrologProvider;
import org.prolobjectlink.prolog.PrologTerm;
import org.prolobjectlink.prolog.PrologVariable;

public class InterPrologVariable extends InterPrologTerm implements PrologVariable {

	public InterPrologVariable(PrologProvider provider, int position) {
		super(VARIABLE_TYPE, provider, new TermVariable("_", position));
	}

	public InterPrologVariable(PrologProvider provider, String name) {
		super(VARIABLE_TYPE, provider, new TermVariable(name, vIndexer++));
	}

	public InterPrologVariable(PrologProvider provider, String name, int position) {
		super(VARIABLE_TYPE, provider, new TermVariable(name, position));
	}

	public PrologTerm[] getArguments() {
		return new InterPrologVariable[0];
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

}

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

import com.declarativa.interprolog.TermModel;

import io.github.prolobjectlink.prolog.ArityError;
import io.github.prolobjectlink.prolog.FunctorError;
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

	InterPrologVariable(int type, PrologProvider provider) {
		super(type, provider);
	}

	InterPrologVariable(int type, PrologProvider provider, String name) {
		super(type, provider, new TermVariable(name, vIndexer++));
	}

	InterPrologVariable(int type, PrologProvider provider, TermModel var) {
		super(type, provider, var);
	}

	public int getArity() {
		throw new ArityError(this);
	}

	public String getFunctor() {
		throw new FunctorError(this);
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

	public PrologTerm[] getArguments() {
		return new InterPrologFloat[0];
	}

}

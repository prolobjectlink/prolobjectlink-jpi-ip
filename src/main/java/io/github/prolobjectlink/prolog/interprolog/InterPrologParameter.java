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

import io.github.prolobjectlink.prolog.PrologParameter;
import io.github.prolobjectlink.prolog.PrologProvider;
import io.github.prolobjectlink.prolog.PrologTerm;
import io.github.prolobjectlink.prolog.PrologTermType;
import io.github.prolobjectlink.prolog.PrologVariable;

public class InterPrologParameter extends InterPrologVariable implements PrologParameter {

	InterPrologParameter(PrologProvider provider, String name) {
		super(PrologTermType.PARAMETER_TYPE, provider, name);
	}

	InterPrologParameter(PrologProvider provider, PrologTerm name) {
		super(PrologTermType.PARAMETER_TYPE, provider);
		this.value = new TermVariable(((PrologVariable) name).getName(), vIndexer++);
	}

	InterPrologParameter(PrologProvider provider, int position) {
		super(PrologTermType.PARAMETER_TYPE, provider, new TermVariable("_", position));
	}

	InterPrologParameter(PrologProvider provider, String name, int position) {
		super(PrologTermType.PARAMETER_TYPE, provider, new TermVariable(name, position));
	}

	public final PrologTerm getNameTerm() {
		return provider.newVariable(getName(), getPosition());
	}

}
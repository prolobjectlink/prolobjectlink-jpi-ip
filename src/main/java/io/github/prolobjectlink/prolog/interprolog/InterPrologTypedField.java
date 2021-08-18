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

import io.github.prolobjectlink.prolog.PrologProvider;
import io.github.prolobjectlink.prolog.PrologTerm;
import io.github.prolobjectlink.prolog.PrologTypedField;

public class InterPrologTypedField extends InterPrologField implements PrologTypedField {

	private final PrologTerm kind;

	InterPrologTypedField(PrologProvider provider, PrologTerm name, PrologTerm kind) {
		super(provider, name);
		this.kind = kind;
	}

	InterPrologTypedField(PrologProvider provider, String kind, int position) {
		super(provider, provider.newVariable(position));
		this.kind = provider.newVariable(kind, position);
	}

	InterPrologTypedField(PrologProvider provider, String name, String kind, int position) {
		super(provider, provider.newVariable(name, position));
		this.kind = provider.newVariable(kind, position);
	}

	public final int getArity() {
		return 2;
	}

	public final String getFunctor() {
		return "-";
	}

	@Override
	public PrologTerm[] getArguments() {
		return new PrologTerm[] { getKey(), getValue() };
	}

	@Override
	public PrologTerm getKey() {
		return getNameTerm();
	}

	@Override
	public PrologTerm getValue() {
		return kind;
	}

	@Override
	public PrologTerm setValue(PrologTerm value) {
		// this.type = value.getFunctor()
		getLogger().debug(getClass(), "No value setting allow");
		return kind;
	}

	public PrologTerm getKindTerm() {
		return getValue();
	}

	public String getKind() {
		return kind.getFunctor();
	}

}

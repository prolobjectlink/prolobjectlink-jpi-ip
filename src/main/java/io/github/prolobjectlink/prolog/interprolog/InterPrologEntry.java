/*-
 * #%L
 * prolobjectlink-jpi-ip
 * %%
 * Copyright (C) 2020 - 2021 Prolobjectlink Project
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

import java.util.Map.Entry;

import io.github.prolobjectlink.prolog.AbstractCompounds;
import io.github.prolobjectlink.prolog.PrologEntry;
import io.github.prolobjectlink.prolog.PrologProvider;
import io.github.prolobjectlink.prolog.PrologTerm;
import io.github.prolobjectlink.prolog.PrologTermType;

/**
 * PrologEntry is key-value pair of PrologTerm. Is an implementation of
 * {@link Entry} and {@link PrologTerm}.
 * 
 * @author Jose Zalacain
 * @since 1.1
 */
public final class InterPrologEntry extends AbstractCompounds implements PrologEntry {

	private final PrologTerm key;
	private PrologTerm value;

	InterPrologEntry(PrologProvider provider, PrologTerm key, PrologTerm value) {
		super(PrologTermType.MAP_ENTRY_TYPE, provider);
		this.value = value;
		this.key = key;
	}

	public PrologTerm getKey() {
		return key;
	}

	public PrologTerm getValue() {
		return value;
	}

	public PrologTerm setValue(PrologTerm value) {
		this.value = value;
		return value;
	}

	public boolean isList() {
		return false;
	}

	public boolean isStructure() {
		return true;
	}

	public boolean isEmptyList() {
		return false;
	}

	public String getFunctor() {
		return "-";
	}

	public int getArity() {
		return 2;
	}

	public PrologTerm[] getArguments() {
		return new PrologTerm[] { key, value };
	}

	@Override
	public int hashCode() {
		int result = 0;
		final int prime = 31;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InterPrologEntry other = (InterPrologEntry) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key)) {
			return false;
		}
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "" + key + "-" + value + "";
	}

}

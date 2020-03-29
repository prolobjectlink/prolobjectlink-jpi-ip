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

import static io.github.prolobjectlink.prolog.interprolog.InterPrologProvider.varCache;

import java.io.Serializable;

import com.declarativa.interprolog.TermModel;
import com.declarativa.interprolog.util.VariableNode;

/**
 * 
 * @author Jose Zalacain
 * @since 1.0
 */
class TermVariable extends TermModel implements Serializable {

	private static final long serialVersionUID = -4471030713129859522L;
	private final int position;
	private final String name;

	TermVariable(String name, int position) {
		super(new VariableNode(position));
		varCache.put("Var" + position, name);
		this.position = position;
		this.name = name;
	}

	public final String getName() {
		return name;
	}

	public final int getPosition() {
		return position;
	}

	@Override
	public String toString() {
		return "" + name + "";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + position;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		TermVariable other = (TermVariable) obj;
		if (position != other.position)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

}

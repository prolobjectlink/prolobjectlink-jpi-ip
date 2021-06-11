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

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import io.github.prolobjectlink.prolog.PrologClause;
import io.github.prolobjectlink.prolog.PrologClauses;

class InterPrologClauses extends AbstractList<PrologClause> implements PrologClauses {

	private final int arity;
	private final String functor;
	private final List<PrologClause> list;

	InterPrologClauses(String functor, int arity) {
		list = new ArrayList<PrologClause>();
		this.functor = functor;
		this.arity = arity;
	}

	public void add(int index, PrologClause element) {
		list.add(index, element);
	}

	public PrologClause remove(int index) {
		return list.remove(index);
	}

	public PrologClause get(int index) {
		return list.get(index);
	}

	public int size() {
		return list.size();
	}

	public boolean isDynamic() {
		for (PrologClause prologClause : list) {
			if (!prologClause.isDynamic()) {
				return false;
			}
		}
		return true;
	}

	public boolean isMultifile() {
		for (PrologClause prologClause : list) {
			if (!prologClause.isMultifile()) {
				return false;
			}
		}
		return true;
	}

	public boolean isDiscontiguous() {
		for (PrologClause prologClause : list) {
			if (!prologClause.isDiscontiguous()) {
				return false;
			}
		}
		return true;
	}

	public String getIndicator() {
		return functor + "/" + arity;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((list == null) ? 0 : list.hashCode());
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
		InterPrologClauses other = (InterPrologClauses) obj;
		if (list == null) {
			if (other.list != null)
				return false;
		} else if (!list.equals(other.list)) {
			return false;
		}
		return true;
	}

}

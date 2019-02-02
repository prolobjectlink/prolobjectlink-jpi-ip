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

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.prolobjectlink.prolog.PrologOperator;
import org.prolobjectlink.prolog.PrologOperatorSet;

public final class InterPrologOperatorSet extends AbstractSet<PrologOperator> implements PrologOperatorSet {

	protected final Set<PrologOperator> operators;

	public InterPrologOperatorSet() {
		operators = new LinkedHashSet<PrologOperator>();
	}

	public boolean currentOp(String opreator) {
		for (PrologOperator operatorEntry : operators) {
			if (operatorEntry.getOperator().equals(opreator)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Iterator<PrologOperator> iterator() {
		return operators.iterator();
	}

	@Override
	public int size() {
		return operators.size();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((operators == null) ? 0 : operators.hashCode());
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
		InterPrologOperatorSet other = (InterPrologOperatorSet) obj;
		if (operators == null) {
			if (other.operators != null)
				return false;
		} else if (!operators.equals(other.operators)) {
			return false;
		}
		return true;
	}

}

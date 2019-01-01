/*
 * #%L
 * prolobjectlink-jpi-tuprolog
 * %%
 * Copyright (C) 2012 - 2017 WorkLogic Project
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
package org.worklogic.prolog.xsb;

import java.util.AbstractSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.logicware.prolog.PrologOperator;
import org.logicware.prolog.PrologOperatorSet;

final class XsbPrologOperatorSet extends AbstractSet<PrologOperator> implements PrologOperatorSet {

	protected final Set<PrologOperator> operators;

	public XsbPrologOperatorSet() {
		// TODO LOAD OPS
		operators = new HashSet<PrologOperator>();
	}

	public boolean currentOp(String opreator) {
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
		result = prime * result + operators.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		XsbPrologOperatorSet other = (XsbPrologOperatorSet) obj;
		return operators.equals(other.operators);

	}

}

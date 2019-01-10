/*
 * #%L
 * prolobjectlink-jpi-ip
 * %%
 * Copyright (C) 2012 - 2019 WorkLogic Project
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
package org.worklogic.prolog.interprolog;

import static org.logicware.prolog.PrologTermType.ATOM_TYPE;
import static org.logicware.prolog.PrologTermType.CUT_TYPE;
import static org.logicware.prolog.PrologTermType.DOUBLE_TYPE;
import static org.logicware.prolog.PrologTermType.EMPTY_TYPE;
import static org.logicware.prolog.PrologTermType.FAIL_TYPE;
import static org.logicware.prolog.PrologTermType.FALSE_TYPE;
import static org.logicware.prolog.PrologTermType.FLOAT_TYPE;
import static org.logicware.prolog.PrologTermType.INTEGER_TYPE;
import static org.logicware.prolog.PrologTermType.LIST_TYPE;
import static org.logicware.prolog.PrologTermType.LONG_TYPE;
import static org.logicware.prolog.PrologTermType.NIL_TYPE;
import static org.logicware.prolog.PrologTermType.STRUCTURE_TYPE;
import static org.logicware.prolog.PrologTermType.TRUE_TYPE;
import static org.logicware.prolog.PrologTermType.VARIABLE_TYPE;

import java.util.Map;

import org.logicware.prolog.AbstractTerm;
import org.logicware.prolog.PrologNumber;
import org.logicware.prolog.PrologProvider;
import org.logicware.prolog.PrologTerm;

import com.declarativa.interprolog.PrologOperatorsContext;
import com.declarativa.interprolog.PrologOperatorsContext.PrologOperator;
import com.declarativa.interprolog.TermModel;

public class InterPrologTerm extends AbstractTerm implements PrologTerm {

	protected int vIndex;
	protected TermModel value;
	protected PrologTerm vValue;
	protected static int vIdexer = 0;

	protected InterPrologTerm(int type, PrologProvider provider) {
		super(type, provider);
	}

	protected InterPrologTerm(int type, PrologProvider provider, TermModel value) {
		super(type, provider);
		this.value = value;
	}

	protected InterPrologTerm(int type, PrologProvider provider, int vIndex) {
		this(type, provider);
		this.vIndex = vIndex;
	}

	public final int compareTo(PrologTerm term) {

		int termType = term.getType();

		if ((type >> 8) < (termType >> 8)) {
			return -1;
		} else if ((type >> 8) > (termType >> 8)) {
			return 1;
		}

		switch (type) {
		case NIL_TYPE:
		case CUT_TYPE:
		case FAIL_TYPE:
		case TRUE_TYPE:
		case FALSE_TYPE:
		case ATOM_TYPE:

			// alphabetic functor comparison
			int result = value.node.toString().compareTo(term.getFunctor());
			if (result < 0) {
				return -1;
			} else if (result > 0) {
				return 1;
			}
			break;

		case FLOAT_TYPE:

			checkNumberType(term);
			float thisFloatValue = ((Number) value.node).floatValue();
			float otherFloatValue = ((PrologNumber) term).getFloatValue();

			if (thisFloatValue < otherFloatValue) {
				return -1;
			} else if (thisFloatValue > otherFloatValue) {
				return 1;
			}

			break;

		case LONG_TYPE:

			checkNumberType(term);
			long thisValue = value.longValue();
			long otherValue = ((PrologNumber) term).getLongValue();

			if (thisValue < otherValue) {
				return -1;
			} else if (thisValue > otherValue) {
				return 1;
			}

			break;

		case DOUBLE_TYPE:

			checkNumberType(term);
			double thisDoubleValue = ((Number) value.node).doubleValue();
			double otherDoubleValue = ((PrologNumber) term).getDoubleValue();

			if (thisDoubleValue < otherDoubleValue) {
				return -1;
			} else if (thisDoubleValue > otherDoubleValue) {
				return 1;
			}

			break;

		case INTEGER_TYPE:

			checkNumberType(term);
			int thisIntergerValue = value.intValue();
			int otherIntegerValue = ((PrologNumber) term).getIntValue();

			if (thisIntergerValue < otherIntegerValue) {
				return -1;
			} else if (thisIntergerValue > otherIntegerValue) {
				return 1;
			}

			break;

		case LIST_TYPE:
		case EMPTY_TYPE:
		case STRUCTURE_TYPE:

			PrologTerm thisCompound = this;
			PrologTerm otherCompound = term;

			// comparison by arity
			if (thisCompound.getArity() < otherCompound.getArity()) {
				return -1;
			} else if (thisCompound.getArity() > otherCompound.getArity()) {
				return 1;
			}

			// alphabetic functor comparison
			result = thisCompound.getFunctor().compareTo(otherCompound.getFunctor());
			if (result < 0) {
				return -1;
			} else if (result > 0) {
				return 1;
			}

			// arguments comparison
			PrologTerm[] thisArguments = thisCompound.getArguments();
			PrologTerm[] otherArguments = otherCompound.getArguments();

			for (int i = 0; i < thisArguments.length; i++) {
				PrologTerm thisArgument = thisArguments[i];
				PrologTerm otherArgument = otherArguments[i];
				if (thisArgument != null && otherArgument != null) {
					result = thisArgument.compareTo(otherArgument);
					if (result != 0) {
						return result;
					}
				}
			}
			break;

		case VARIABLE_TYPE:

			InterPrologTerm thisVariable = unwrap(InterPrologTerm.class);
			InterPrologTerm otherVariable = unwrap(term, InterPrologTerm.class);
			if (thisVariable.vIndex < otherVariable.vIndex) {
				return -1;
			} else if (thisVariable.vIndex > otherVariable.vIndex) {
				return 1;
			}
			break;

		default:
			return 0;

		}

		return 0;
	}

	public String getIndicator() {
		return value.getFunctorArity();
	}

	public boolean hasIndicator(String functor, int arity) {
		return getIndicator().equals(functor + "/" + arity);
	}

	public boolean isAtom() {
		return value.isAtom();
	}

	public boolean isNumber() {
		return value.isNumber();
	}

	public boolean isFloat() {
		return value.node instanceof Float;
	}

	public boolean isInteger() {
		return value.isInteger();
	}

	public boolean isDouble() {
		return value.node instanceof Double;
	}

	public boolean isLong() {
		return value.isLong();
	}

	public boolean isVariable() {
		return value.isVar();
	}

	public boolean isList() {
		return value.isList();
	}

	public boolean isStructure() {
		return isCompound() && !isList() && !isEmptyList();
	}

	public final boolean isNil() {
		if (!isVariable() && !isNumber()) {
			return hasIndicator("nil", 0);
		}
		return false;
	}

	public boolean isEmptyList() {
		return value.isListEnd();
	}

	public boolean isAtomic() {
		return !isCompound() && !isList();
	}

	public boolean isCompound() {
		return getArity() > 0;
	}

	public boolean isEvaluable() {
		// TODO Auto-generated method stub
		return false;
	}

	public int getArity() {
		return value.getChildCount();
	}

	public String getFunctor() {
		return "" + value.node + "";
	}

	public PrologTerm[] getArguments() {
		// TODO Auto-generated method stub
		return new PrologTerm[0];
	}

	public boolean unify(PrologTerm term) {
		return value.unifies(fromTerm(term, TermModel.class));
	}

	public Map<String, PrologTerm> match(PrologTerm term) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		return "" + value + "";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		InterPrologTerm other = (InterPrologTerm) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value)) {
			return false;
		}
		return true;
	}

}

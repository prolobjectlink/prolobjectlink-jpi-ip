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

import static org.prolobjectlink.prolog.PrologTermType.ATOM_TYPE;
import static org.prolobjectlink.prolog.PrologTermType.CUT_TYPE;
import static org.prolobjectlink.prolog.PrologTermType.DOUBLE_TYPE;
import static org.prolobjectlink.prolog.PrologTermType.EMPTY_TYPE;
import static org.prolobjectlink.prolog.PrologTermType.FAIL_TYPE;
import static org.prolobjectlink.prolog.PrologTermType.FALSE_TYPE;
import static org.prolobjectlink.prolog.PrologTermType.FLOAT_TYPE;
import static org.prolobjectlink.prolog.PrologTermType.INTEGER_TYPE;
import static org.prolobjectlink.prolog.PrologTermType.LIST_TYPE;
import static org.prolobjectlink.prolog.PrologTermType.LONG_TYPE;
import static org.prolobjectlink.prolog.PrologTermType.NIL_TYPE;
import static org.prolobjectlink.prolog.PrologTermType.STRUCTURE_TYPE;
import static org.prolobjectlink.prolog.PrologTermType.TRUE_TYPE;
import static org.prolobjectlink.prolog.PrologTermType.VARIABLE_TYPE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.prolobjectlink.prolog.AbstractTerm;
import org.prolobjectlink.prolog.PrologNumber;
import org.prolobjectlink.prolog.PrologProvider;
import org.prolobjectlink.prolog.PrologTerm;

import com.declarativa.interprolog.SolutionIterator;
import com.declarativa.interprolog.TermModel;

public abstract class InterPrologTerm extends AbstractTerm implements PrologTerm {

	protected int vIndex;
	protected TermModel value;
	protected PrologTerm vValue;
	protected static int vIndexer = 0;

	protected InterPrologTerm(int type, PrologProvider provider) {
		super(type, provider);
	}

	protected InterPrologTerm(int type, PrologProvider provider, TermModel value) {
		super(type, provider);
		this.value = value;
	}

	public String getIndicator() {
		return value.getFunctorArity();
	}

	public boolean hasIndicator(String functor, int arity) {
		return getIndicator().equals(functor + "/" + arity);
	}

	public final boolean isAtom() {
		return value.isAtom();
	}

	public final boolean isNumber() {
		return value.isNumber();
	}

	public final boolean isFloat() {
		return value.node instanceof Float;
	}

	public final boolean isInteger() {
		return value.isInteger();
	}

	public final boolean isDouble() {
		return value.node instanceof Double;
	}

	public final boolean isLong() {
		return value.isLong();
	}

	public final boolean isVariable() {
		return value.isVar();
	}

	public final boolean isList() {
		return value.isList();
	}

	public final boolean isStructure() {
		return !isVariable() && isCompound() && !isList() && !isEmptyList();
	}

	public final boolean isNil() {
		if (!isVariable() && !isNumber()) {
			return hasIndicator("nil", 0);
		}
		return false;
	}

	public final boolean isEmptyList() {
		return value.isListEnd();
	}

	public final boolean isAtomic() {
		return !isCompound() && !isList();
	}

	public final boolean isCompound() {
		return !isNumber() && getArity() > 0;
	}

	public final boolean isEvaluable() {
		if (isStructure()) {
			String key = "X";
			String stringQuery = "findall(P/S/O,current_op(P,S,O)," + key + "), buildTermModel(" + key + ",TM)";
			SolutionIterator si = InterPrologEngine.engine.goal(stringQuery, "[TM]");
			while (si.hasNext()) {
				Object[] bindings = si.next();
				for (Object object : bindings) {
					if (object instanceof TermModel) {
						TermModel list = (TermModel) object;
						while (list.getChildCount() > 0) {
							TermModel solvedTerm = (TermModel) list.getChild(0);
							String n = (String) solvedTerm.children[1].node;
							if (!isNumber() && getFunctor().equals(n)) {
								return true;
							}
							list = (TermModel) list.getChild(1);
						}
					}
				}
			}
		}
		return false;
	}

	public int getArity() {
		return value.getChildCount();
	}

	public String getFunctor() {
		return "" + value.node + "";
	}

	public PrologTerm[] getArguments() {
		return getArity() > 0 ? toTermArray(value.children, PrologTerm[].class) : new PrologTerm[0];
	}

	public final boolean unify(PrologTerm term) {
		return unify(fromTerm(term, TermModel.class));
	}

	protected final boolean unify(TermModel o) {
		return value.unifies(o);
	}

	public final Map<String, PrologTerm> match(PrologTerm term) {
		String key = "_KEY_";
		Map<String, PrologTerm> map = new HashMap<String, PrologTerm>();
		String string = "unify_with_occurs_check(" + value + "," + term + ")";

		// parse the string query and enumerates variable
		List<String> variables = new ArrayList<String>();
		InterPrologParser ip = new InterPrologParser();
		TermModel[] models = ip.parseTerms(string);
		for (TermModel termModels : models) {
			enumerateVariables(variables, termModels);
		}

		StringBuilder b = new StringBuilder();
		Iterator<?> j = variables.iterator();
		if (j.hasNext()) {
			while (j.hasNext()) {
				b.append(j.next());
				if (j.hasNext()) {
					b.append('/');
				}
			}
		} else {
			b.append('_');
		}

		String stringQuery = "findall(" + b + "," + string + "," + key + "), buildTermModel(" + key + ",TM)";

		// busy wait necessary to wait engine disposition
		if (!InterPrologEngine.engine.isIdle()) {
			InterPrologEngine.engine.waitUntilIdle();
		}

		SolutionIterator si = InterPrologEngine.engine.goal(stringQuery, "[TM]");
		while (si.hasNext()) {
			Object[] bindings = si.next();
			for (Object object : bindings) {
				if (object instanceof TermModel) {
					TermModel list = (TermModel) object;
					while (list.getChildCount() > 0) {
						if (variables.size() - 1 >= 0) {
							int index = variables.size() - 1;
							TermModel solvedTerm = (TermModel) list.getChild(0);
							map.put(variables.get(index), InterPrologUtil.toTerm(provider, solvedTerm));
						}
						list = (TermModel) list.getChild(1);
					}
				}
			}
		}
		return map;
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

	@Override
	public String toString() {
		return "" + value + "";
	}

	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public final boolean equals(Object obj) {
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
		} else if (value.toString().equals(other.value.toString())) {
			return true;
		} else if (!unify(other.value)) {
			return false;
		}
		return true;
	}

	private void enumerateVariables(List<String> vector, TermModel term) {
		if (!(term instanceof TermVariable)) {
			TermModel[] terms = term.children;
			if (terms != null && terms.length > 0) {
				for (TermModel t : terms) {
					enumerateVariables(vector, t);
				}
			}
		} else if (!vector.contains(((TermVariable) term).getName())) {
			vector.add(((TermVariable) term).getName());
		}
	}

}

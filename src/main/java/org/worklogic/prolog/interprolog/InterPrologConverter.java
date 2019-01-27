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

import java.math.BigDecimal;

import org.logicware.prolog.AbstractConverter;
import org.logicware.prolog.PrologAtom;
import org.logicware.prolog.PrologConverter;
import org.logicware.prolog.PrologDouble;
import org.logicware.prolog.PrologFloat;
import org.logicware.prolog.PrologInteger;
import org.logicware.prolog.PrologLong;
import org.logicware.prolog.PrologStructure;
import org.logicware.prolog.PrologTerm;
import org.logicware.prolog.PrologVariable;
import org.logicware.prolog.UnknownTermError;

import com.declarativa.interprolog.SolutionIterator;
import com.declarativa.interprolog.TermModel;

/**
 * 
 * @author Jose Zalacain
 * @since 1.0
 */
public abstract class InterPrologConverter extends AbstractConverter<TermModel> implements PrologConverter<TermModel> {

	public final PrologTerm toTerm(TermModel prologTerm) {
		if (prologTerm.isAtom()) {
			String functor = prologTerm.toString();
			if (functor.equals("nil")) {
				return new InterPrologNil(provider);
			} else if (functor.equals("!")) {
				return new InterPrologCut(createProvider());
			} else if (functor.equals("fail")) {
				return new InterPrologFail(provider);
			} else if (functor.equals("true")) {
				return new InterPrologTrue(provider);
			} else if (functor.equals("false")) {
				return new InterPrologFalse(provider);
			} else if (functor.equals("[]")) {
				return new InterPrologEmpty(provider);
			}
			return new InterPrologAtom(provider, functor);
		} else if (prologTerm.equals(InterPrologList.EMPTY)) {
			return new InterPrologEmpty(provider);
		} else if ((prologTerm.node instanceof Float) || (prologTerm.node instanceof BigDecimal)) {
			return new InterPrologFloat(provider, ((Number) prologTerm.node).floatValue());
		} else if ((prologTerm.node instanceof Double) || (prologTerm.node instanceof BigDecimal)) {
			return new InterPrologDouble(provider, ((Number) prologTerm.node).doubleValue());
		} else if (prologTerm.isInteger()) {
			return new InterPrologInteger(provider, prologTerm.intValue());
		} else if (prologTerm.isLong()) {
			return new InterPrologLong(provider, prologTerm.longValue());
		} else if (prologTerm.isVar()) {
			String name = ((TermVariable) prologTerm).getName();
			PrologVariable variable = sharedVariables.get(name);
			if (variable == null) {
				variable = new InterPrologVariable(provider, name);
				sharedVariables.put(variable.getName(), variable);
			}
			return variable;
		} else if (prologTerm.isList()) {
			return new InterPrologList(provider, prologTerm.flatList());
		} else if (prologTerm.getChildCount() > 0) {

			TermModel compound = prologTerm;
			int arity = compound.getChildCount();
			String functor = compound.node.toString();
			TermModel[] arguments = new TermModel[arity];

			if (arity == 2) {
				String key = "LIST";
				String stringQuery = "findall(P/S/O,current_op(P,S,O)," + key + "), buildTermModel(" + key + ",TM)";
				SolutionIterator si = provider.newEngine().unwrap(InterPrologEngine.class).engine.goal(stringQuery,
						"[TM]");
				while (si.hasNext()) {
					Object[] bindings = si.next();
					for (Object object : bindings) {
						if (object instanceof TermModel) {
							TermModel list = (TermModel) object;
							while (list.getChildCount() > 0) {
								TermModel solvedTerm = (TermModel) list.getChild(0);
								String n = (String) solvedTerm.children[1].node;
								if (n.equals(functor)) {
									TermModel left = (TermModel) compound.getChild(0);
									TermModel right = (TermModel) compound.getChild(1);
									return new InterPrologStructure(provider, left, functor, right);
								}
								list = (TermModel) list.getChild(1);
							}
						}
					}
				}
			}

			for (int i = 0; i < arity; i++) {
				arguments[i] = (TermModel) compound.getChild(i);
			}
			return new InterPrologStructure(provider, functor, arguments);

		}

		throw new UnknownTermError(prologTerm);
	}

	public final TermModel fromTerm(PrologTerm term) {
		switch (term.getType()) {
		case NIL_TYPE:
			return new TermModel("nil");
		case CUT_TYPE:
			return new TermModel("!");
		case FAIL_TYPE:
			return new TermModel("fail");
		case TRUE_TYPE:
			return new TermModel("true");
		case FALSE_TYPE:
			return new TermModel("false");
		case EMPTY_TYPE:
			return InterPrologEmpty.EMPTY;
		case ATOM_TYPE:
			return new TermModel(removeQuoted(((PrologAtom) term).getStringValue()));
		case FLOAT_TYPE:
			return new TermModel(((PrologFloat) term).getFloatValue());
		case INTEGER_TYPE:
			return new TermModel(((PrologInteger) term).getIntValue());
		case DOUBLE_TYPE:
			return new TermModel(((PrologDouble) term).getDoubleValue());
		case LONG_TYPE:
			return new TermModel(((PrologLong) term).getLongValue());
		case VARIABLE_TYPE:
			String name = ((PrologVariable) term).getName();
			int position = ((PrologVariable) term).getPosition();
			TermModel variable = sharedPrologVariables.get(name);
			if (variable == null) {
				variable = new TermVariable(name, position);
				sharedPrologVariables.put(name, variable);
			}
			return variable;
		case LIST_TYPE:
			PrologTerm[] array = term.getArguments();
			TermModel list = new TermModel(".", fromTermArray(array));
			return list;
		case STRUCTURE_TYPE:
			String functor = term.getFunctor();
			TermModel[] arguments = fromTermArray(((PrologStructure) term).getArguments());
			return new TermModel(functor, arguments);
		default:
			throw new UnknownTermError(term);
		}
	}

	public final TermModel[] fromTermArray(PrologTerm[] terms) {
		TermModel[] prologTerms = new TermModel[terms.length];
		for (int i = 0; i < terms.length; i++) {
			prologTerms[i] = fromTerm(terms[i]);
		}
		return prologTerms;
	}

	public final TermModel fromTerm(PrologTerm head, PrologTerm[] body) {
		TermModel clauseHead = fromTerm(head);
		if (body != null && body.length > 0) {
			TermModel clauseBody = fromTerm(body[body.length - 1]);
			for (int i = body.length - 2; i >= 0; --i) {
				clauseBody = new TermModel(",", new TermModel[] { fromTerm(body[i]), clauseBody });
			}
			return new TermModel(":-", new TermModel[] { clauseHead, clauseBody });
		}
		return clauseHead;
	}

}

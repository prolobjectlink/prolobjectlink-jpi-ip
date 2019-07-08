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

import static org.prolobjectlink.prolog.PrologTermType.STRUCTURE_TYPE;

import org.prolobjectlink.prolog.PrologProvider;
import org.prolobjectlink.prolog.PrologStructure;
import org.prolobjectlink.prolog.PrologTerm;

import com.declarativa.interprolog.TermModel;

/**
 * 
 * @author Jose Zalacain
 * @since 1.0
 */
class InterPrologStructure extends InterPrologTerm implements PrologStructure {

	InterPrologStructure(PrologProvider provider, String functor, PrologTerm... arguments) {
		super(STRUCTURE_TYPE, provider);
		TermModel[] terms = new TermModel[arguments.length];
		for (int i = 0; i < arguments.length; i++) {
			terms[i] = ((InterPrologTerm) arguments[i]).value;
		}
		value = new TermModel(functor, terms);
	}

	InterPrologStructure(PrologProvider provider, String functor, TermModel... arguments) {
		super(STRUCTURE_TYPE, provider);
		value = new TermModel(functor, arguments);
	}

	InterPrologStructure(PrologProvider provider, PrologTerm left, String operator, PrologTerm right) {
		super(STRUCTURE_TYPE, provider);
		TermModel leftOperand = ((InterPrologTerm) left).value;
		TermModel rightOperand = ((InterPrologTerm) right).value;
		value = new TermModel(operator, new TermModel[] { leftOperand, rightOperand });
	}

	InterPrologStructure(PrologProvider provider, TermModel left, String functor, TermModel right) {
		super(STRUCTURE_TYPE, provider, new TermModel(functor, new TermModel[] { left, right }));
	}

	public PrologTerm getArgument(int index) {
		checkIndex(index, getArity());
		return getArguments()[index];
	}

	public final PrologTerm getRight() {
		return getArgument(1);
	}

	public final PrologTerm getLeft() {
		return getArgument(0);
	}

	public final String getOperator() {
		return getFunctor();
	}

}

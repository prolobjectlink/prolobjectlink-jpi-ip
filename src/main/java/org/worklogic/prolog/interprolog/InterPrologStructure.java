/*
 * #%L
 * prolobjectlink-jpi-ip
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
package org.worklogic.prolog.interprolog;

import static org.logicware.prolog.PrologTermType.STRUCTURE_TYPE;

import org.logicware.prolog.PrologProvider;
import org.logicware.prolog.PrologStructure;
import org.logicware.prolog.PrologTerm;

import com.declarativa.interprolog.TermModel;

public class InterPrologStructure extends InterPrologTerm implements PrologStructure {

	InterPrologStructure(PrologProvider provider, String functor, PrologTerm... arguments) {
		super(STRUCTURE_TYPE, provider);
		TermModel[] terms = new TermModel[arguments.length];
		for (int i = 0; i < arguments.length; i++) {
			terms[i] = unwrap(arguments[i], InterPrologTerm.class).value;
		}
		value = new TermModel(functor, terms);
	}

	InterPrologStructure(PrologProvider provider, String functor, TermModel... arguments) {
		super(STRUCTURE_TYPE, provider);
		value = new TermModel(functor, arguments);
	}

	InterPrologStructure(PrologProvider provider, PrologTerm left, String operator, PrologTerm right) {
		super(STRUCTURE_TYPE, provider);
		TermModel leftOperand = left.unwrap(InterPrologTerm.class).value;
		TermModel rightOperand = right.unwrap(InterPrologTerm.class).value;
		value = new TermModel(operator, new TermModel[] { leftOperand, rightOperand });
	}

	InterPrologStructure(PrologProvider provider, TermModel left, String functor, TermModel right) {
		super(STRUCTURE_TYPE, provider, new TermModel(functor, new TermModel[] { left, right }));
	}

	public PrologTerm getArgument(int index) {
		checkIndex(index, getArity());
		return getArguments()[index];
	}

}

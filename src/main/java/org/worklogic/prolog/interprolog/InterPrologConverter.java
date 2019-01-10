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

import org.logicware.prolog.AbstractConverter;
import org.logicware.prolog.PrologConverter;
import org.logicware.prolog.PrologTerm;

import com.declarativa.interprolog.TermModel;

/**
 * 
 * @author Jose Zalacain
 * @since 1.0
 */
public abstract class InterPrologConverter extends AbstractConverter<TermModel> implements PrologConverter<TermModel> {

	public final PrologTerm toTerm(TermModel prologTerm) {
		// TODO Auto-generated method stub
		return null;
	}

	public final TermModel fromTerm(PrologTerm term) {
		// TODO Auto-generated method stub
		return null;
	}

	public final TermModel[] fromTermArray(PrologTerm[] terms) {
		TermModel[] prologTerms = new TermModel[terms.length];
		for (int i = 0; i < terms.length; i++) {
			prologTerms[i] = fromTerm(terms[i]);
		}
		return prologTerms;
	}

	public final TermModel fromTerm(PrologTerm head, PrologTerm[] body) {
		// TODO Auto-generated method stub
		return null;
	}

}

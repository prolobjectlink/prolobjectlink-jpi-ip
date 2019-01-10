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

import static org.logicware.prolog.PrologTermType.INTEGER_TYPE;

import org.logicware.prolog.PrologInteger;
import org.logicware.prolog.PrologProvider;
import org.logicware.prolog.PrologTerm;

import com.declarativa.interprolog.TermModel;

public class InterPrologInteger extends InterPrologNumber implements PrologInteger {

	public InterPrologInteger(PrologProvider provider) {
		super(INTEGER_TYPE, provider, new TermModel(0));
	}

	public InterPrologInteger(PrologProvider provider, Number value) {
		super(INTEGER_TYPE, provider, new TermModel(value.intValue()));
	}

	public InterPrologInteger(int type, PrologProvider provider, TermModel value) {
		super(type, provider, value);
	}

	

	public final PrologTerm[] getArguments() {
		return new InterPrologInteger[0];
	}

}

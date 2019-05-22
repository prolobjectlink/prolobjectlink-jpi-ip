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

import static org.prolobjectlink.prolog.PrologTermType.LONG_TYPE;

import org.prolobjectlink.prolog.PrologLong;
import org.prolobjectlink.prolog.PrologProvider;

import com.declarativa.interprolog.TermModel;

/**
 * 
 * @author Jose Zalacain
 * @since 1.0
 */
final class InterPrologLong extends InterPrologInteger implements PrologLong {

	InterPrologLong(PrologProvider provider) {
		super(LONG_TYPE, provider, new TermModel(0L));
	}

	InterPrologLong(PrologProvider provider, Number value) {
		super(LONG_TYPE, provider, new TermModel(value.longValue()));
	}

}

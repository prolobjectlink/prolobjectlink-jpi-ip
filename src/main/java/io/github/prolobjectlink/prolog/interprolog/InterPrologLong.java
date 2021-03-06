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
package io.github.prolobjectlink.prolog.interprolog;

import static io.github.prolobjectlink.prolog.PrologTermType.LONG_TYPE;

import com.declarativa.interprolog.TermModel;

import io.github.prolobjectlink.prolog.PrologLong;
import io.github.prolobjectlink.prolog.PrologProvider;

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

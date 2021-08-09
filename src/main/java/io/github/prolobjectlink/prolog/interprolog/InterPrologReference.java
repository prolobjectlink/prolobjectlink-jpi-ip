/*-
 * #%L
 * prolobjectlink-jpi-ip
 * %%
 * Copyright (C) 2020 - 2021 Prolobjectlink Project
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

import static io.github.prolobjectlink.prolog.PrologTermType.OBJECT_TYPE;

import com.declarativa.interprolog.TermModel;
import com.declarativa.interprolog.util.ObjectRegistry;

import io.github.prolobjectlink.prolog.PrologProvider;
import io.github.prolobjectlink.prolog.PrologReference;

public class InterPrologReference extends InterPrologTerm implements PrologReference {

	private static final ObjectRegistry register = new ObjectRegistry();

	InterPrologReference(PrologProvider provider, TermModel reference) {
		super(OBJECT_TYPE, provider, reference);
	}

	InterPrologReference(PrologProvider provider, Object reference) {
		super(OBJECT_TYPE, provider, set(reference));
	}

	public Class<?> getReferenceType() {
		return getObject().getClass();
	}

	public Object getObject() {
		return get(value);
	}

	static TermModel set(Object reference) {
		if (reference != null) {
			return new TermModel(register.registerJavaObject(reference));
		}
		return new TermModel(0);
	}

	static Object get(TermModel id) {
		if (id.intValue() != 0) {
			return register.getRealJavaObject(id.intValue());
		}
		return null;
	}

}

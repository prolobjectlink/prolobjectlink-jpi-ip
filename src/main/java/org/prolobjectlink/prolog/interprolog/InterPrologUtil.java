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

import static org.prolobjectlink.prolog.AbstractConverter.SIMPLE_ATOM_REGEX;

import java.util.HashMap;
import java.util.Map;

import org.prolobjectlink.prolog.PrologProvider;
import org.prolobjectlink.prolog.PrologTerm;

import com.declarativa.interprolog.TermModel;

/**
 * Util class for {@link InterPrologTerm}
 * 
 * @author Jose Zalacain
 * @since 1.0
 *
 */
final class InterPrologUtil {

	private InterPrologUtil() {
	}

	static final Map<String, String> FUNCTORS = new HashMap<String, String>();

	static final PrologTerm toTerm(PrologProvider provider, Object object) {

		if (object instanceof TermModel) {
			TermModel xt = (TermModel) object;
			object = xt.node;
		}

		// null pointer
		if (object == null) {
			return new InterPrologNil(provider);
		}

		// string data type
		else if (object instanceof String) {
			String string = (String) object;
			if (!string.matches(SIMPLE_ATOM_REGEX)) {
				return new InterPrologAtom(provider, "'" + string + "'");
			}
			return new InterPrologAtom(provider, string);
		}

		// primitives and wrappers data types
		else if (object.getClass() == boolean.class || object instanceof Boolean) {
			return (Boolean) object ? new InterPrologTrue(provider) : new InterPrologFalse(provider);
		} else if (object.getClass() == int.class || object instanceof Integer) {
			return new InterPrologInteger(provider, (Integer) object);
		} else if (object.getClass() == float.class || object instanceof Float) {
			return new InterPrologFloat(provider, (Float) object);
		} else if (object.getClass() == long.class || object instanceof Long) {
			return new InterPrologLong(provider, (Long) object);
		} else if (object.getClass() == double.class || object instanceof Double) {
			// return new InterPrologDouble(provider, (Double) object);
			return new InterPrologFloat(provider, (Double) object);
		}

		//
		else if (object instanceof Object[]) {
			Object[] objects = (Object[]) object;
			PrologTerm[] terms = new PrologTerm[objects.length];
			for (int i = 0; i < objects.length; i++) {
				terms[i] = toTerm(provider, objects[i]);
			}
			return new InterPrologList(provider, terms);
		} else if (object instanceof TermModel) {
			return toTerm(provider, (TermModel) object);
		}
		return null;
	}

}

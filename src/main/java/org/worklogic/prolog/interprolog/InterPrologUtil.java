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

import static org.logicware.prolog.AbstractConverter.SIMPLE_ATOM_REGEX;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.logicware.prolog.PrologProvider;
import org.logicware.prolog.PrologTerm;

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

		// null pointer
		if (object == null) {
			return new InterPrologNil(provider);
		}

		// string data type
		else if (object instanceof String) {
			String string = (String) object;
			int index = string.indexOf('(');
			if (index > -1) {
				String functor = string.substring(0, index);
				String arguments = string.substring(index);
				if (!functor.matches(SIMPLE_ATOM_REGEX)) {
					StringBuilder buffer = new StringBuilder();
					buffer.append('\'');
					buffer.append(functor);
					buffer.append('\'');
					String quoted = "" + buffer + "";
					buffer.append(arguments);
					string = "" + buffer + "";
					FUNCTORS.put(functor, quoted);

					// interprolog need treatment for complex functors
					for (Entry<String, String> entry : FUNCTORS.entrySet()) {

						// retrieve cached functors
						String key = entry.getKey();
						String value = entry.getValue();

						// first and unique term pattern
						String firstRegex = "(" + key + "";
						if (string.contains(firstRegex)) {
							string = string.replaceAll(key, value);
						}

						// non-first term pattern
						String nonFirstRegex = "," + key + "";
						if (string.contains(nonFirstRegex)) {
							string = string.replaceAll(key, value);
						}

					}
				}
			}

			return provider.parseTerm(string);
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
			return new InterPrologDouble(provider, (Double) object);
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

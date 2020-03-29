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

import static io.github.prolobjectlink.prolog.AbstractConverter.SIMPLE_ATOM_REGEX;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.declarativa.interprolog.SolutionIterator;
import com.declarativa.interprolog.TermModel;

import io.github.prolobjectlink.prolog.PrologProvider;
import io.github.prolobjectlink.prolog.PrologTerm;

/**
 * Util class for {@link InterPrologTerm}
 * 
 * @author Jose Zalacain
 * @since 1.0
 *
 */
final class InterPrologUtil {

	static final Map<String, String> FUNCTORS = new HashMap<String, String>();
	static final InterPrologParser PARSER = new InterPrologParser();

	static final PrologTerm toTerm(PrologProvider provider, Object object) {

		if (object instanceof TermModel) {
			TermModel xt = (TermModel) object;
			if (xt.getChildCount() > 0) {
				if (xt.isList()) {
					return new InterPrologList(provider, xt.flatList());
				}

				int arity = xt.getChildCount();
				String functor = xt.node.toString();
				TermModel[] arguments = new TermModel[arity];

				if (arity == 2) {
					String key = "LIST";
					String stringQuery = "findall(P/S/O,current_op(P,S,O)," + key + "), buildTermModel(" + key + ",TM)";

					// busy wait necessary to wait engine disposition
					if (!InterPrologEngine.engine.isIdle()) {
						InterPrologEngine.engine.waitUntilIdle();
					}

					SolutionIterator si = InterPrologEngine.engine.goal(stringQuery, "[TM]");
					while (si.hasNext()) {
						Object[] bindings = si.next();
						for (Object o : bindings) {
							if (o instanceof TermModel) {
								TermModel list = (TermModel) o;
								while (list.getChildCount() > 0) {
									TermModel solvedTerm = (TermModel) list.getChild(0);
									String n = (String) solvedTerm.children[1].node;
									if (functor.contains("'" + n + "'")) {
										TermModel left = (TermModel) xt.getChild(0);
										TermModel right = (TermModel) xt.getChild(1);
										return new InterPrologStructure(provider, left, n, right);
									}
									list = (TermModel) list.getChild(1);
								}
							}
						}
					}
				}

				for (int i = 0; i < arity; i++) {
					arguments[i] = (TermModel) xt.getChild(i);
				}
				return new InterPrologStructure(provider, functor, arguments);

			}
			object = xt.node;
		}

		// null pointer
		if (object == null) {
			return new InterPrologNil(provider);
		}

		// string data type
		else if (object instanceof String) {
			String string = (String) object;
			if (string.equals("[]")) {
				return new InterPrologEmpty(provider);
			} else if (!string.matches(SIMPLE_ATOM_REGEX)) {
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

	static final TermModel parseTerm(String term) {
		return new InterPrologParser().parseTerm(term);
	}

	static final TermModel[] parseTerms(String stringTerms) {
		return new InterPrologParser().parseTerms(stringTerms);
	}

	/**
	 * replace cached inter-prolog variables with real variable name
	 * 
	 * @param string with inter-prolog variables
	 * @return string with variable replacement
	 * @since 1.0
	 */
	static final String replace(String string) {
		for (Entry<String, String> entry : InterPrologProvider.varCache.entrySet()) {
			if (string.contains(entry.getKey())) {
				string = string.replace(entry.getKey(), entry.getValue());
			}
		}
		return string;
	}

	private InterPrologUtil() {
	}

}

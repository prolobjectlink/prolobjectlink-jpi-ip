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

import static org.logicware.prolog.PrologTermType.LIST_TYPE;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.logicware.prolog.PrologList;
import org.logicware.prolog.PrologProvider;
import org.logicware.prolog.PrologTerm;
import org.worklogic.AbstractIterator;

import com.declarativa.interprolog.TermModel;

public class InterPrologList extends InterPrologTerm implements PrologList {

	public static final TermModel EMPTY = new TermModel("[]", true);
	public static final String LIST_FUNCTOR = ".";

	protected InterPrologList(PrologProvider provider) {
		super(LIST_TYPE, provider, EMPTY);
	}

	protected InterPrologList(PrologProvider provider, TermModel[] arguments) {
		super(LIST_TYPE, provider);
		value = EMPTY;
		for (int i = arguments.length - 1; i >= 0; --i) {
			value = new TermModel(LIST_FUNCTOR, new TermModel[] { arguments[i], value });
		}
	}

	protected InterPrologList(PrologProvider provider, PrologTerm[] arguments) {
		super(LIST_TYPE, provider);
		value = EMPTY;
		for (int i = arguments.length - 1; i >= 0; --i) {
			value = new TermModel(LIST_FUNCTOR,
					new TermModel[] { unwrap(arguments[i], InterPrologTerm.class).value, value });
		}
	}

	protected InterPrologList(PrologProvider provider, PrologTerm head, PrologTerm tail) {
		super(LIST_TYPE, provider);
		TermModel h = unwrap(head, InterPrologTerm.class).value;
		TermModel t = unwrap(tail, InterPrologTerm.class).value;
		value = new TermModel(LIST_FUNCTOR, new TermModel[] { h, t });
	}

	protected InterPrologList(PrologProvider provider, PrologTerm[] arguments, PrologTerm tail) {
		super(LIST_TYPE, provider);
		value = fromTerm(tail, TermModel.class);
		for (int i = arguments.length - 1; i >= 0; --i) {
			TermModel[] args = { fromTerm(arguments[i], TermModel.class), value };
			value = new TermModel(LIST_FUNCTOR, args);
		}
	}

	public int size() {
		return value.getChildCount();
	}

	public void clear() {
		value = EMPTY;
	}

	public boolean isEmpty() {
		return value.isListEnd();
	}

	public Iterator<PrologTerm> iterator() {
		return new SwiPrologListIter(value);
	}

	public PrologTerm getHead() {
		return provider.toTerm(value.getChild(1), PrologTerm.class);
	}

	public PrologTerm getTail() {
		return provider.toTerm(value.getChild(2), PrologTerm.class);
	}

	public int getArity() {
		return value.getChildCount();
	}

	public String getFunctor() {
		return ".";
	}

	public String getIndicator() {
		return getFunctor() + "/" + getArity();
	}

	public boolean hasIndicator(String functor, int arity) {
		return getFunctor().equals(functor) && getArity() == arity;
	}

	public PrologTerm[] getArguments() {
		return toTermArray(value.flatList(), PrologTerm[].class);
	}

	public String toString() {
		StringBuilder string = new StringBuilder("[");
		Iterator<PrologTerm> i = iterator();
		if (i.hasNext()) {
			string.append(i.next());
		}
		while (i.hasNext()) {
			string.append(", ");
			string.append(i.next());
		}
		return string.append("]").toString();
	}

	private class SwiPrologListIter extends AbstractIterator<PrologTerm> implements Iterator<PrologTerm> {

		private TermModel ptr;
		private int index;
		private int length;

		private SwiPrologListIter(TermModel l) {
			ptr = l;
			if (l.isList()) {
				length = l.getChildCount();
			}
		}

		public boolean hasNext() {
			return index < length;
		}

		public PrologTerm next() {

			if (!hasNext()) {
				throw new NoSuchElementException();
			}

			PrologTerm term = toTerm(ptr.getChild(1), PrologTerm.class);
			ptr = (TermModel) ptr.getChild(2);
			index++;
			return term;
		}

	}

}

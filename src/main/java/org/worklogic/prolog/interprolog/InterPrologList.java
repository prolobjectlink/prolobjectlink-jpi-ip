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
		value = new TermModel(LIST_FUNCTOR, arguments, true);
	}

	protected InterPrologList(PrologProvider provider, PrologTerm[] arguments) {
		super(LIST_TYPE, provider);
		value = new TermModel(LIST_FUNCTOR, fromTermArray(arguments, TermModel[].class), true);
	}

	protected InterPrologList(PrologProvider provider, PrologTerm head, PrologTerm tail) {
		super(LIST_TYPE, provider);
		TermModel h = unwrap(head, InterPrologTerm.class).value;
		TermModel t = unwrap(tail, InterPrologTerm.class).value;
		value = new TermModel(LIST_FUNCTOR, new TermModel[] { h, t }, true);
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
		return new InterPrologListIter(value);
	}

	public PrologTerm getHead() {
		return provider.toTerm(value.getChild(0), PrologTerm.class);
	}

	public PrologTerm getTail() {
		return provider.toTerm(value.getChild(2), PrologTerm.class);
	}

	public int getArity() {
		return 2;
	}

	public String getFunctor() {
		return LIST_FUNCTOR;
	}

	@Override
	public String getIndicator() {
		return getFunctor() + "/" + getArity();
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

	private class InterPrologListIter extends AbstractIterator<PrologTerm> implements Iterator<PrologTerm> {

		private final TermModel[] a;
		private int index;

		private InterPrologListIter(TermModel l) {
			a = l.children;
		}

		public boolean hasNext() {
			return index < a.length;
		}

		public PrologTerm next() {

			if (!hasNext()) {
				throw new NoSuchElementException();
			}

			return toTerm(a[index++], PrologTerm.class);
		}

	}

}
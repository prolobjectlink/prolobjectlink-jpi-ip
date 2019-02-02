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

import static org.prolobjectlink.prolog.PrologTermType.LIST_TYPE;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.prolobjectlink.AbstractIterator;
import org.prolobjectlink.prolog.PrologList;
import org.prolobjectlink.prolog.PrologProvider;
import org.prolobjectlink.prolog.PrologTerm;

import com.declarativa.interprolog.TermModel;

public class InterPrologList extends InterPrologTerm implements PrologList {

	public static final TermModel EMPTY = new TermModel("[]", true);
	public static final String EMPTY_FUNCTOR = "[]";
	public static final String LIST_FUNCTOR = ".";

	protected InterPrologList(PrologProvider provider) {
		super(LIST_TYPE, provider, EMPTY);
	}

	protected InterPrologList(PrologProvider provider, TermModel[] arguments) {
		super(LIST_TYPE, provider);
		if (arguments == null || arguments.length == 0) {
			value = new TermModel(EMPTY_FUNCTOR, arguments, true);
		} else {
			value = new TermModel(LIST_FUNCTOR, arguments, true);
		}
	}

	protected InterPrologList(PrologProvider provider, PrologTerm[] arguments) {
		super(LIST_TYPE, provider);
		if (arguments == null || arguments.length == 0) {
			value = new TermModel(EMPTY_FUNCTOR, null, true);
		} else {
			value = new TermModel(LIST_FUNCTOR, fromTermArray(arguments, TermModel[].class), true);
		}
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
		int size = 0;
		TermModel list = value;
		while (list.getChildCount() > 0) {
			list = (TermModel) list.getChild(1);
			size++;
		}
		return size;
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

	public final int getArity() {
		return value.children == null || value.children.length == 0 ? 0 : 2;
	}

	public final String getFunctor() {
		return "" + value.node + "";
	}

	@Override
	public final String getIndicator() {
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
			a = l.getChildCount() > 0 ? l.children : new TermModel[0];
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

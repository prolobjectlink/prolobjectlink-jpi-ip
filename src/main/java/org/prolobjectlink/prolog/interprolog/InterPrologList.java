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

import org.prolobjectlink.prolog.AbstractIterator;
import org.prolobjectlink.prolog.PrologList;
import org.prolobjectlink.prolog.PrologProvider;
import org.prolobjectlink.prolog.PrologTerm;

import com.declarativa.interprolog.TermModel;

/**
 * 
 * @author Jose Zalacain
 * @since 1.0
 */
class InterPrologList extends InterPrologTerm implements PrologList {

	static final TermModel EMPTY = new TermModel("[]", true);
	private static final String EMPTY_FUNCTOR = "[]";
	private static final String LIST_FUNCTOR = ".";

	protected InterPrologList(PrologProvider provider) {
		super(LIST_TYPE, provider, EMPTY);
	}

	protected InterPrologList(PrologProvider provider, TermModel[] arguments) {
		super(LIST_TYPE, provider);
		if (arguments == null || arguments.length == 0) {
			value = new TermModel(EMPTY_FUNCTOR, null, true);
		} else {
			value = TermModel.makeList(arguments);
		}
	}

	protected InterPrologList(PrologProvider provider, PrologTerm[] arguments) {
		super(LIST_TYPE, provider);
		if (arguments == null || arguments.length == 0) {
			value = new TermModel(EMPTY_FUNCTOR, null, true);
		} else {
			value = TermModel.makeList(fromTermArray(arguments, TermModel[].class));
		}
	}

	protected InterPrologList(PrologProvider provider, PrologTerm head, PrologTerm tail) {
		super(LIST_TYPE, provider);
		TermModel h = ((InterPrologTerm) head).value;
		TermModel t = ((InterPrologTerm) tail).value;
		value = TermModel.makeList(new TermModel[] { h, t });
	}

	protected InterPrologList(PrologProvider provider, PrologTerm[] arguments, PrologTerm tail) {
		super(LIST_TYPE, provider);
		TermModel[] terms = new TermModel[arguments.length + 1];
		terms[arguments.length] = fromTerm(tail, TermModel.class);
		for (int i = 0; i < arguments.length; i++) {
			terms[i] = fromTerm(arguments[i], TermModel.class);
		}
		value = TermModel.makeList(terms);
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
		return provider.toTerm(value.getChild(1), PrologTerm.class);
	}

	public final int getArity() {
		return value.children == null || value.children.length == 0 || value.children[0] == EMPTY
				|| value.children[1] == EMPTY ? 0 : 2;
	}

	public final String getFunctor() {
		return value.children == null || value.children.length == 0 || value.children[0] == EMPTY
				|| value.children[1] == EMPTY ? "[]" : "" + value.node + "";
	}

	@Override
	public PrologTerm[] getArguments() {
		return toTermArray(value.flatList(), PrologTerm[].class);
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

		private TermModel ptr;

		private InterPrologListIter(TermModel l) {
			ptr = l;
		}

		public boolean hasNext() {
			return ptr.getChildCount() > 0;
		}

		public PrologTerm next() {

			if (!hasNext()) {
				throw new NoSuchElementException();
			}

			TermModel t = (TermModel) ptr.getChild(0);
			ptr = (TermModel) ptr.getChild(1);
			return toTerm(t, PrologTerm.class);

		}

	}

}

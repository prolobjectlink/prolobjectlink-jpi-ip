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

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import io.github.prolobjectlink.prolog.AbstractCompounds;
import io.github.prolobjectlink.prolog.AbstractIterator;
import io.github.prolobjectlink.prolog.PrologMap;
import io.github.prolobjectlink.prolog.PrologProvider;
import io.github.prolobjectlink.prolog.PrologTerm;
import io.github.prolobjectlink.prolog.PrologTermType;

/**
 * A PrologTerm that maps PrologTerm keys to PrologTerm values. A map cannot
 * contain duplicate keys. Each key can map to at most one value.
 * 
 * @author Jose Zalacain
 * @since 1.1
 */
public final class InterPrologMap extends AbstractCompounds implements PrologMap {

	private Map<PrologTerm, PrologTerm> map;

	InterPrologMap(PrologProvider provider, int size) {
		super(PrologTermType.MAP_TYPE, provider);
		map = new LinkedHashMap<PrologTerm, PrologTerm>(size);
	}

	InterPrologMap(PrologProvider provider, Map<? extends PrologTerm, ? extends PrologTerm> m) {
		this(provider);
		putAll(m);
	}

	InterPrologMap(PrologProvider provider) {
		this(provider, 16);
	}

	public boolean isList() {
		return true;
	}

	public boolean isStructure() {
		return false;
	}

	public boolean isEmptyList() {
		return map.size() == 0;
	}

	public String getFunctor() {
		return ".";
	}

	public int getArity() {
		if (map.size() > 0) {
			return 2;
		}
		return 0;
	}

	public PrologTerm[] getArguments() {
		PrologProvider p = getProvider();
		PrologTerm[] args = new PrologTerm[map.size()];
		Set<Entry<PrologTerm, PrologTerm>> s = entrySet();
		Iterator<Entry<PrologTerm, PrologTerm>> i = s.iterator();
		for (int j = 0; j < args.length && i.hasNext(); j++) {
			Entry<PrologTerm, PrologTerm> e = i.next();
			args[j] = new InterPrologEntry(p, e.getKey(), e.getValue());
		}
		return args;
	}

	public PrologTerm getArgument(int index) {
		int idx = 0;
		PrologTerm term = null;
		checkIndex(index, size());
		Iterator<PrologTerm> i = iterator();
		for (; i.hasNext() && idx <= index; idx++) {
			term = i.next();
			if (idx == index) {
				return term;
			}
		}
		return term;
	}

	public int hashCode() {
		int result = 0;
		final int prime = 31;
		result = prime * result + ((map == null) ? 0 : map.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InterPrologMap other = (InterPrologMap) obj;
		if (map == null) {
			if (other.map != null)
				return false;
		} else if (!map.equals(other.map)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		Set<Entry<PrologTerm, PrologTerm>> set = entrySet();
		Iterator<Entry<PrologTerm, PrologTerm>> i = set.iterator();
		b.append('[');
		while (i.hasNext()) {
			Entry<PrologTerm, PrologTerm> entry = i.next();
			b.append(entry.getKey());
			b.append('-');
			b.append(entry.getValue());
			if (i.hasNext()) {
				b.append(',');
				b.append(' ');
			}
		}
		b.append(']');
		return "" + b + "";
	}

	@Override
	public Iterator<PrologTerm> iterator() {
		return new PrologMapIterator();
	}

	@Override
	public PrologTerm getHead() {
		return iterator().next();
	}

	@Override
	public PrologTerm getTail() {
		InterPrologMap m = new InterPrologMap(provider, map);
		m.remove(((Entry<?, ?>) getHead()).getKey());
		return m;
	}

	private class PrologMapIterator extends AbstractIterator<PrologTerm> implements Iterator<PrologTerm> {

		private final Set<PrologTerm> set;
		private final Iterator<PrologTerm> itr;

		private PrologMapIterator() {
			set = new LinkedHashSet<PrologTerm>(map.size());
			for (Iterator<Entry<PrologTerm, PrologTerm>> i = map.entrySet().iterator(); i.hasNext();) {
				Entry<PrologTerm, PrologTerm> e = i.next();
				PrologTerm t = new InterPrologEntry(provider, e.getKey(), e.getValue());
				set.add(t);
			}
			itr = set.iterator();
		}

		@Override
		public boolean hasNext() {
			return itr.hasNext();
		}

		@Override
		public PrologTerm next() {
			return itr.next();
		}

	}

	public PrologTerm put(PrologTerm key, PrologTerm value) {
		return map.put(key, value);
	}

	public Set<Entry<PrologTerm, PrologTerm>> entrySet() {
		return map.entrySet();
	}

	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	@Override
	public PrologTerm get(Object key) {
		return map.get(key);
	}

	@Override
	public PrologTerm remove(Object key) {
		return map.remove(key);
	}

	@Override
	public void putAll(Map<? extends PrologTerm, ? extends PrologTerm> m) {
		map.putAll(m);
	}

	@Override
	public Set<PrologTerm> keySet() {
		return map.keySet();
	}

	@Override
	public Collection<PrologTerm> values() {
		return map.values();
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public int size() {
		return map.size();
	}

}

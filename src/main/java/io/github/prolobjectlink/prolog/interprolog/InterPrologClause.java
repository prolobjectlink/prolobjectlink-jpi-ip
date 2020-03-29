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

import java.util.Iterator;

import io.github.prolobjectlink.prolog.AbstractClause;
import io.github.prolobjectlink.prolog.PrologClause;
import io.github.prolobjectlink.prolog.PrologIndicator;
import io.github.prolobjectlink.prolog.PrologProvider;
import io.github.prolobjectlink.prolog.PrologTerm;

/**
 * 
 * @author Jose Zalacain
 * @since 1.0
 */
final class InterPrologClause extends AbstractClause implements PrologClause {

	private final PrologIndicator indicator;

	protected InterPrologClause(PrologProvider provider, PrologTerm head, boolean dynamic, boolean multifile,
			boolean discontiguous) {
		super(provider, head, dynamic, multifile, discontiguous);
		this.indicator = new InterPrologIndicator(head.getFunctor(), head.getArity());
	}

	protected InterPrologClause(PrologProvider provider, PrologTerm head, PrologTerm body, boolean dynamic,
			boolean multifile, boolean discontiguous) {
		super(provider, head, body, dynamic, multifile, discontiguous);
		this.indicator = new InterPrologIndicator(head.getFunctor(), head.getArity());
	}

	public PrologIndicator getPrologIndicator() {
		return indicator;
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(getHead());
		if (isRule()) {
			b.append(":-\n\t");
			Iterator<PrologTerm> i = getBodyIterator();
			while (i.hasNext()) {
				b.append(i.next());
				if (i.hasNext()) {
					b.append(",\n\t");
				}
			}
		}
		b.append('.');
		String c = b.toString();
		c = InterPrologUtil.replace(c);
		return "" + c + "";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((indicator == null) ? 0 : indicator.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		InterPrologClause other = (InterPrologClause) obj;
		if (indicator == null) {
			if (other.indicator != null)
				return false;
		} else if (!indicator.equals(other.indicator)) {
			return false;
		}
		return true;
	}

}

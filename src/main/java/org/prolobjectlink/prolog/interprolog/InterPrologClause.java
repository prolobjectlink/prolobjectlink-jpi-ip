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

import java.util.Iterator;

import org.prolobjectlink.prolog.AbstractClause;
import org.prolobjectlink.prolog.PrologClause;
import org.prolobjectlink.prolog.PrologProvider;
import org.prolobjectlink.prolog.PrologTerm;

public final class InterPrologClause extends AbstractClause implements PrologClause {

	protected InterPrologClause(PrologProvider provider, PrologTerm head) {
		super(provider, head, false, false, false);
	}

	protected InterPrologClause(PrologProvider provider, PrologTerm head, PrologTerm body) {
		super(provider, head, body, false, false, false);
	}

	protected InterPrologClause(PrologProvider provider, PrologTerm head, boolean dynamic, boolean multifile,
			boolean discontiguous) {
		super(provider, head, dynamic, multifile, discontiguous);
	}

	protected InterPrologClause(PrologProvider provider, PrologTerm head, PrologTerm body, boolean dynamic,
			boolean multifile, boolean discontiguous) {
		super(provider, head, body, dynamic, multifile, discontiguous);
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
}

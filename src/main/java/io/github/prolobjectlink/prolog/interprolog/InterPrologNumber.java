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

import com.declarativa.interprolog.TermModel;

import io.github.prolobjectlink.prolog.ArityError;
import io.github.prolobjectlink.prolog.FunctorError;
import io.github.prolobjectlink.prolog.PrologDouble;
import io.github.prolobjectlink.prolog.PrologFloat;
import io.github.prolobjectlink.prolog.PrologInteger;
import io.github.prolobjectlink.prolog.PrologLong;
import io.github.prolobjectlink.prolog.PrologNumber;
import io.github.prolobjectlink.prolog.PrologProvider;

/**
 * @author Jose Zalacain
 * @since 1.0
 */
abstract class InterPrologNumber extends InterPrologTerm implements PrologNumber {

	protected InterPrologNumber(int type, PrologProvider provider, TermModel value) {
		super(type, provider, value);
	}

	public final PrologInteger getPrologInteger() {
		return new InterPrologInteger(provider, getIntegerValue());
	}

	public final PrologFloat getPrologFloat() {
		return new InterPrologFloat(provider, getFloatValue());
	}

	public final PrologDouble getPrologDouble() {
		return new InterPrologDouble(provider, getDoubleValue());
	}

	public final PrologLong getPrologLong() {
		return new InterPrologLong(provider, getLongValue());
	}

	public final int getArity() {
		throw new ArityError(this);
	}

	public final String getFunctor() {
		throw new FunctorError(this);
	}

	public final long getLongValue() {
		return value.longValue();
	}

	public final double getDoubleValue() {
		return ((Number) value.node).doubleValue();
	}

	public final int getIntegerValue() {
		return value.isLong() ? (int) value.longValue() : value.intValue();
	}

	public final float getFloatValue() {
		return ((Number) value.node).floatValue();
	}

}

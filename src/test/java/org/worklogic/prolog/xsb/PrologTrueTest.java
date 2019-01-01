/*
 * #%L
 * prolobjectlink-jpi-tuprolog
 * %%
 * Copyright (C) 2012 - 2018 WorkLogic Project
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
package org.worklogic.prolog.xsb;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.logicware.prolog.PrologAtom;
import org.logicware.prolog.PrologDouble;
import org.logicware.prolog.PrologFloat;
import org.logicware.prolog.PrologInteger;
import org.logicware.prolog.PrologList;
import org.logicware.prolog.PrologLong;
import org.logicware.prolog.PrologStructure;
import org.logicware.prolog.PrologTerm;
import org.logicware.prolog.PrologVariable;

public class PrologTrueTest extends PrologBaseTest {

	private PrologTerm t = provider.prologTrue();

	@Test
	public void testGetArguments() {
		assertArrayEquals(new PrologTerm[0], t.getArguments());
	}

	@Test
	public void testGetArity() {
		assertEquals(0, t.getArity());
	}

	@Test
	public void testGetFunctor() {
		assertEquals("true", t.getFunctor());
	}

	@Test
	public void testGetIndicator() {
		assertEquals("true/0", t.getIndicator());
	}

	@Test
	public void testHasIndicator() {
		assertTrue(t.hasIndicator("true", 0));
	}

	@Test
	public void testHashCode() {
		assertFalse(provider.prologCut().hashCode() == t.hashCode());
		assertFalse(provider.prologFail().hashCode() == t.hashCode());
		assertEquals(provider.prologTrue().hashCode(), t.hashCode());
	}

	@Test
	public void testIsAtom() {
		assertTrue(t.isAtom());
	}

	@Test
	public void testIsNumber() {
		assertFalse(t.isNumber());
	}

	@Test
	public final void testIsFloat() {
		assertFalse(t.isFloat());
	}

	@Test
	public final void testIsDouble() {
		assertFalse(t.isDouble());
	}

	@Test
	public final void testIsInteger() {
		assertFalse(t.isInteger());
	}

	@Test
	public final void testIsLong() {
		assertFalse(t.isLong());
	}

	@Test
	public final void testIsVariable() {
		assertFalse(t.isVariable());
	}

	@Test
	public final void testIsList() {
		assertFalse(t.isList());
	}

	@Test
	public final void testIsStructure() {
		assertFalse(t.isStructure());
	}

	@Test
	public final void testIsNil() {
		assertFalse(t.isNil());
	}

	@Test
	public final void testIsEmptyList() {
		assertFalse(t.isEmptyList());
	}

	@Test
	public final void testIsEvaluable() {
		assertFalse(t.isEvaluable());
	}

	@Test
	public void testIsAtomic() {
		assertTrue(t.isAtomic());
	}

	@Test
	public void testIsCompound() {
		assertFalse(t.isCompound());
	}

	@Test
	public final void testUnify() {

		// with atom
		PrologTerm t = provider.prologTrue();
		PrologAtom atom = provider.newAtom("John Doe");
		assertFalse(t.unify(atom));

		// with integer
		PrologInteger iValue = provider.newInteger(36);
		assertFalse(t.unify(iValue));

		// with long
		PrologLong lValue = provider.newLong(28);
		assertFalse(t.unify(lValue));

		// with float
		PrologFloat fValue = provider.newFloat(36.47);
		assertFalse(t.unify(fValue));

		// with double
		PrologDouble dValue = provider.newDouble(36.47);
		assertFalse(t.unify(dValue));

		// with variable
		PrologVariable variable = provider.newVariable("X", 0);
		// true. case [] and variable
		assertTrue(t.unify(variable));

		// with predicate
		PrologStructure structure = provider.parseStructure("some_predicate(a,b,c)");
		assertFalse(t.unify(structure));

		// with list
		PrologList list = provider.parseList("[a,b,c]");
		assertFalse(t.unify(list));
		assertTrue(t.unify(t));

		// with expression
		PrologTerm expression = provider.parseTerm("58+93*10");
		assertFalse(t.unify(expression));

	}

	@Test
	public final void testCompareTo() {

		// with atom
		PrologTerm t = provider.prologTrue();
		PrologAtom atom = provider.newAtom("John Doe");
		assertEquals(1, t.compareTo(atom));

		// with integer
		PrologInteger iValue = provider.newInteger(36);
		assertEquals(1, t.compareTo(iValue));

		// with long
		PrologLong lValue = provider.newLong(28);
		assertEquals(1, t.compareTo(lValue));

		// with float
		PrologFloat fValue = provider.newFloat(36.47);
		assertEquals(1, t.compareTo(fValue));

		// with double
		PrologDouble dValue = provider.newDouble(36.47);
		assertEquals(1, t.compareTo(dValue));

		// with variable
		PrologVariable variable = provider.newVariable("X", 0);
		assertEquals(1, t.compareTo(variable));

		// with predicate
		PrologStructure structure = provider.parseStructure("some_predicate(a,b,c)");
		assertEquals(-1, t.compareTo(structure));

		// with list
		PrologList list = provider.parseList("[a,b,c]");
		assertEquals(-1, t.compareTo(list));
		assertEquals(0, t.compareTo(t));

		// with expression
		PrologTerm expression = provider.parseTerm("58+93*10");
		assertEquals(-1, t.compareTo(expression));

	}

	@Test
	public void testEqualsObject() {
		assertFalse(t.equals(provider.prologCut()));
		assertFalse(t.equals(provider.prologFail()));
		assertTrue(t.equals(provider.prologTrue()));
	}

	@Test
	public void testToString() {
		assertEquals("true", t.toString());
	}

}

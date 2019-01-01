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

public class PrologFailTest extends PrologBaseTest {

	private PrologTerm fail = provider.prologFail();

	@Test
	public void testGetArguments() {
		assertArrayEquals(new PrologTerm[0], fail.getArguments());
	}

	@Test
	public void testGetArity() {
		assertEquals(0, fail.getArity());
	}

	@Test
	public void testGetFunctor() {
		assertEquals("fail", fail.getFunctor());
	}

	@Test
	public void testGetIndicator() {
		assertEquals("fail/0", fail.getIndicator());
	}

	@Test
	public void testHasIndicator() {
		assertTrue(fail.hasIndicator("fail", 0));
	}

	@Test
	public void testHashCode() {
		assertFalse(provider.prologCut().hashCode() == fail.hashCode());
		assertEquals(provider.prologFail().hashCode(), fail.hashCode());
	}

	@Test
	public void testIsAtom() {
		assertTrue(fail.isAtom());
	}

	@Test
	public void testIsNumber() {
		assertFalse(fail.isNumber());
	}

	@Test
	public final void testIsFloat() {
		assertFalse(fail.isFloat());
	}

	@Test
	public final void testIsDouble() {
		assertFalse(fail.isDouble());
	}

	@Test
	public final void testIsInteger() {
		assertFalse(fail.isInteger());
	}

	@Test
	public final void testIsLong() {
		assertFalse(fail.isLong());
	}

	@Test
	public final void testIsVariable() {
		assertFalse(fail.isVariable());
	}

	@Test
	public final void testIsList() {
		assertFalse(fail.isList());
	}

	@Test
	public final void testIsStructure() {
		assertFalse(fail.isStructure());
	}

	@Test
	public final void testIsNil() {
		assertFalse(fail.isNil());
	}

	@Test
	public final void testIsEmptyList() {
		assertFalse(fail.isEmptyList());
	}

	@Test
	public final void testIsEvaluable() {
		assertFalse(fail.isEvaluable());
	}

	@Test
	public void testIsAtomic() {
		assertTrue(fail.isAtomic());
	}

	@Test
	public void testIsCompound() {
		assertFalse(fail.isCompound());
	}

	@Test
	public final void testUnify() {

		// with atom
		PrologTerm fail = provider.prologFail();
		PrologAtom atom = provider.newAtom("John Doe");
		assertFalse(fail.unify(atom));

		// with integer
		PrologInteger iValue = provider.newInteger(36);
		assertFalse(fail.unify(iValue));

		// with long
		PrologLong lValue = provider.newLong(28);
		assertFalse(fail.unify(lValue));

		// with float
		PrologFloat fValue = provider.newFloat(36.47);
		assertFalse(fail.unify(fValue));

		// with double
		PrologDouble dValue = provider.newDouble(36.47);
		assertFalse(fail.unify(dValue));

		// with variable
		PrologVariable variable = provider.newVariable("X", 0);
		// true. case [] and variable
		assertTrue(fail.unify(variable));

		// with predicate
		PrologStructure structure = provider.parseStructure("some_predicate(a,b,c)");
		assertFalse(fail.unify(structure));

		// with list
		PrologList list = provider.parseList("[a,b,c]");
		assertFalse(fail.unify(list));
		assertTrue(fail.unify(fail));

		// with expression
		PrologTerm expression = provider.parseTerm("58+93*10");
		assertFalse(fail.unify(expression));

	}

	@Test
	public final void testCompareTo() {

		// with atom
		PrologTerm fail = provider.prologFail();
		PrologAtom atom = provider.newAtom("John Doe");
		assertEquals(1, fail.compareTo(atom));

		// with integer
		PrologInteger iValue = provider.newInteger(36);
		assertEquals(1, fail.compareTo(iValue));

		// with long
		PrologLong lValue = provider.newLong(28);
		assertEquals(1, fail.compareTo(lValue));

		// with float
		PrologFloat fValue = provider.newFloat(36.47);
		assertEquals(1, fail.compareTo(fValue));

		// with double
		PrologDouble dValue = provider.newDouble(36.47);
		assertEquals(1, fail.compareTo(dValue));

		// with variable
		PrologVariable variable = provider.newVariable("X",0);
		assertEquals(1, fail.compareTo(variable));

		// with predicate
		PrologStructure structure = provider.parseStructure("some_predicate(a,b,c)");
		assertEquals(fail.compareTo(structure), -1);

		// with list
		PrologList list = provider.parseList("[a,b,c]");
		assertEquals(-1, fail.compareTo(list));
		assertEquals(0, fail.compareTo(fail));

		// with expression
		PrologTerm expression = provider.parseTerm("58+93*10");
		assertEquals(-1, fail.compareTo(expression));

	}

	@Test
	public void testEqualsObject() {
		assertFalse(fail.equals(provider.prologCut()));
		assertTrue(fail.equals(provider.prologFail()));
	}

	@Test
	public void testToString() {
		assertEquals("fail", fail.toString());
	}

}

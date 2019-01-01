/*
 * #%L
 * prolobjectlink-jpi-tuprolog
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
package org.worklogic.prolog.xsb;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.logicware.prolog.PrologTermType.LIST_TYPE;

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

public class PrologEmptyTest extends PrologBaseTest {

	private PrologTerm empty = provider.prologEmpty();

	@Test
	public final void testGetKey() {
		assertEquals("[]/0", empty.getIndicator());
	}

	@Test
	public final void testGetArity() {
		assertEquals(0, empty.getArity());
	}

	@Test
	public final void testGetFunctor() {
		assertEquals("[]", empty.getFunctor());
	}

	@Test
	public final void testGetArguments() {
		assertArrayEquals(new PrologTerm[0], empty.getArguments());
	}

	@Test
	public final void testToString() {
		assertEquals("[]", empty.toString());
	}

	@Test
	public final void testHashCode() {
		assertEquals(provider.prologEmpty().hashCode(), empty.hashCode());
	}

	@Test
	public final void testGetType() {
		assertEquals(LIST_TYPE, empty.getType());
	}

	@Test
	public final void testIsAtom() {
		assertTrue(empty.isAtom());
	}

	@Test
	public final void testIsNumber() {
		assertFalse(empty.isNumber());
	}

	@Test
	public final void testIsFloat() {
		assertFalse(empty.isFloat());
	}

	@Test
	public final void testIsDouble() {
		assertFalse(empty.isDouble());
	}

	@Test
	public final void testIsInteger() {
		assertFalse(empty.isInteger());
	}

	@Test
	public final void testIsLong() {
		assertFalse(empty.isLong());
	}

	@Test
	public final void testIsVariable() {
		assertFalse(empty.isVariable());
	}

	@Test
	public final void testIsList() {
		assertTrue(empty.isList());
	}

	@Test
	public final void testIsStructure() {
		assertFalse(empty.isStructure());
	}

	@Test
	public final void testIsNil() {
		assertFalse(empty.isNil());
	}

	@Test
	public final void testIsEmptyList() {
		assertTrue(empty.isEmptyList());
	}

	@Test
	public final void testIsExpression() {
		assertFalse(empty.isEvaluable());
	}

	@Test
	public final void testUnify() {

		// with atom
		PrologTerm empty = provider.prologEmpty();
		PrologAtom atom = provider.newAtom("John Doe");
		assertFalse(empty.unify(atom));

		// with integer
		PrologInteger iValue = provider.newInteger(36);
		assertFalse(empty.unify(iValue));

		// with long
		PrologLong lValue = provider.newLong(28);
		assertFalse(empty.unify(lValue));

		// with float
		PrologFloat fValue = provider.newFloat(36.47);
		assertFalse(empty.unify(fValue));

		// with double
		PrologDouble dValue = provider.newDouble(36.47);
		assertFalse(empty.unify(dValue));

		// with variable
		PrologVariable variable = provider.newVariable("X", 0);
		// true. case [] and variable
		assertTrue(empty.unify(variable));

		// with predicate
		PrologStructure structure = provider.parseStructure("some_predicate(a,b,c)");
		assertFalse(empty.unify(structure));

		// with list
		PrologList list = provider.parseList("[a,b,c]");
		assertFalse(empty.unify(list));
		assertTrue(empty.unify(empty));

		// with expression
		PrologTerm expression = provider.parseTerm("58+93*10");
		assertFalse(empty.unify(expression));

	}

	@Test
	public final void testCompareTo() {

		// with atom
		PrologTerm empty = provider.prologEmpty();
		PrologAtom atom = provider.newAtom("John Doe");
		assertEquals(1, empty.compareTo(atom));

		// with integer
		PrologInteger iValue = provider.newInteger(36);
		assertEquals(1, empty.compareTo(iValue));

		// with long
		PrologLong lValue = provider.newLong(28);
		assertEquals(1, empty.compareTo(lValue));

		// with float
		PrologFloat fValue = provider.newFloat(36.47);
		assertEquals(1, empty.compareTo(fValue));

		// with double
		PrologDouble dValue = provider.newDouble(36.47);
		assertEquals(1, empty.compareTo(dValue));

		// with variable
		PrologVariable variable = provider.newVariable("X",0);
		// true. case [] and variable
		assertEquals(1, empty.compareTo(variable));

		// with predicate
		PrologStructure structure = provider.parseStructure("some_predicate(a,b,c)");
		assertEquals(-1, empty.compareTo(structure));

		// with list
		PrologList list = provider.parseList("[a,b,c]");
		assertEquals(-1, empty.compareTo(list));
		assertEquals(0, empty.compareTo(empty));

		// with expression
		PrologTerm expression = provider.parseTerm("58+93*10");
		assertEquals(-1, empty.compareTo(expression));

	}

	@Test
	public final void testEqualsObject() {
		assertEquals(provider.prologEmpty(), empty);
	}

}

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
import static org.logicware.prolog.PrologTermType.INTEGER_TYPE;

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.logicware.prolog.ArityError;
import org.logicware.prolog.FunctorError;
import org.logicware.prolog.IndicatorError;
import org.logicware.prolog.PrologAtom;
import org.logicware.prolog.PrologDouble;
import org.logicware.prolog.PrologFloat;
import org.logicware.prolog.PrologInteger;
import org.logicware.prolog.PrologList;
import org.logicware.prolog.PrologLong;
import org.logicware.prolog.PrologStructure;
import org.logicware.prolog.PrologTerm;
import org.logicware.prolog.PrologVariable;

public class PrologIntegerTest extends PrologBaseTest {

	private PrologInteger integer;

	@Before
	public void setUp() throws Exception {
		integer = provider.newInteger(100);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testGetArguments() {
		assertArrayEquals(new PrologInteger[0], integer.getArguments());
	}

	@Test
	public final void testGetPrologInteger() {
		assertEquals(provider.newInteger(100), integer.getPrologInteger());
	}

	@Test
	public final void testGetPrologFloat() {
		assertEquals(provider.newFloat(100.0), integer.getPrologFloat());
	}

	@Test
	public final void testGetPrologLong() {
		assertEquals(provider.newLong(100), integer.getPrologLong());
	}

	@Test
	public final void testGetPrologDouble() {
		assertEquals(provider.newDouble(100.0), integer.getPrologDouble());
	}

	@Test
	public final void testGetLongValue() {
		assertEquals(100, integer.getLongValue());
	}

	@Test
	public final void testGetDoubleValue() {
		assertEquals(100.0, integer.getDoubleValue(), 0);
	}

	@Test
	public final void testGetIntValue() {
		assertEquals(100, integer.getIntValue());
	}

	@Test
	public final void testGetFloatValue() {
		assertEquals(100.0, integer.getFloatValue(), 0);
	}

	@Test(expected = IndicatorError.class)
	public final void testGetKey() {
		integer.getIndicator();
	}

	@Test
	public final void testGetType() {
		assertEquals(INTEGER_TYPE, integer.getType());
	}

	@Test
	public final void testIsAtom() {
		assertFalse(integer.isAtom());
	}

	@Test
	public final void testIsNumber() {
		assertTrue(integer.isNumber());
	}

	@Test
	public final void testIsFloat() {
		assertFalse(integer.isFloat());
	}

	@Test
	public final void testIsInteger() {
		assertTrue(integer.isInteger());
	}

	@Test
	public void testIsDouble() {
		assertFalse(integer.isDouble());
	}

	@Test
	public void testIsLong() {
		assertFalse(integer.isLong());
	}

	@Test
	public final void testIsVariable() {
		assertFalse(integer.isVariable());
	}

	@Test
	public final void testIsList() {
		assertFalse(integer.isList());
	}

	@Test
	public final void testIsStructure() {
		assertFalse(integer.isStructure());
	}

	@Test
	public final void testIsNil() {
		assertFalse(integer.isNil());
	}

	@Test
	public final void testIsEmptyList() {
		assertFalse(integer.isEmptyList());
	}

	@Test
	public final void testIsExpression() {
		assertFalse(integer.isEvaluable());
	}

	@Test(expected = ArityError.class)
	public final void testGetArity() {
		integer.getArity();
	}

	@Test(expected = FunctorError.class)
	public final void testGetFunctor() {
		integer.getFunctor();
	}

	@Test
	public final void testUnify() {

		// with atom
		PrologInteger iValue = provider.newInteger(28);
		PrologAtom atom = provider.newAtom("John Doe");
		assertFalse(iValue.unify(atom));

		// with integer
		PrologInteger iValue1 = provider.newInteger(36);
		// true because are equals
		assertTrue(iValue.unify(iValue));
		// false because they are different
		assertFalse(iValue.unify(iValue1));

		// with long
		PrologLong lValue = provider.newLong(28);
		PrologLong lValue1 = provider.newLong(100);
		// true because are equals
		assertTrue(iValue.unify(lValue));
		// false because they are different
		assertFalse(iValue.unify(lValue1));

		// with float
		PrologFloat fValue = provider.newFloat(36.47);
		assertFalse(iValue.unify(fValue));

		// with double
		PrologDouble dValue = provider.newDouble(36.47);
		assertFalse(iValue.unify(dValue));

		// with variable
		PrologVariable variable = provider.newVariable("X", 0);
		// true. case atom and variable
		assertTrue(iValue.unify(variable));

		// with predicate
		PrologStructure structure = provider.parseStructure("some_predicate(a,b,c)");
		assertFalse(iValue.unify(structure));

		// with list
		PrologList list = provider.parseList("[a,b,c]");
		assertFalse(iValue.unify(list));

		// with expression
		PrologTerm expression = provider.parseTerm("58+93*10");
		assertFalse(iValue.unify(expression));
	}

	@Test
	public final void testCompareTo() {

		// with atom
		PrologInteger iValue = provider.newInteger(28);
		PrologAtom atom = provider.newAtom("John Doe");
		assertEquals(-1, iValue.compareTo(atom));

		// with integer
		PrologInteger iValue1 = provider.newInteger(36);
		// true because are equals
		assertEquals(0, iValue.compareTo(iValue));
		// false because they are different
		assertEquals(-1, iValue.compareTo(iValue1));

		// with long
		PrologLong lValue = provider.newLong(28);
		PrologLong lValue1 = provider.newLong(100);
		// true because are equals
		assertEquals(0, iValue.compareTo(lValue));
		// false because they are different
		assertEquals(-1, iValue.compareTo(lValue1));

		// with float
		PrologFloat fValue = provider.newFloat(36.47);
		assertEquals(-1, iValue.compareTo(fValue));

		// with double
		PrologDouble dValue = provider.newDouble(36.47);
		assertEquals(-1, iValue.compareTo(dValue));

		// with variable
		PrologVariable variable = provider.newVariable("X", 0);
		// true. case atom and variable
		assertEquals(1, iValue.compareTo(variable));

		// with predicate
		PrologStructure structure = provider.parseStructure("some_predicate(a,b,c)");
		assertEquals(-1, iValue.compareTo(structure));

		// with list
		PrologList list = provider.parseList("[a,b,c]");
		assertEquals(-1, iValue.compareTo(list));

		// with expression
		PrologTerm expression = provider.parseTerm("58+93*10");
		assertEquals(-1, iValue.compareTo(expression));
	}

	@Test
	public final void testMatch() {

		// with atom
		PrologInteger iValue = provider.newInteger(28);
		PrologAtom atom = provider.newAtom("John Doe");
		assertEquals(new HashMap<String, PrologTerm>(), iValue.match(atom));

		// with integer
		PrologInteger iValue1 = provider.newInteger(36);
		// true because are equals
		assertEquals(new HashMap<String, PrologTerm>(), iValue.match(iValue));
		// false because they are different
		assertEquals(new HashMap<String, PrologTerm>(), iValue.match(iValue1));

		// with float
		PrologFloat fValue = provider.newFloat(36.47);
		assertEquals(new HashMap<String, PrologTerm>(), iValue.match(fValue));

		// with variable
		HashMap<String, PrologTerm> substitution = new HashMap<String, PrologTerm>(1);
		substitution.put("X", provider.newInteger(28));
		PrologVariable variable = provider.newVariable("X", 0);
		// true. case atom and variable
		assertEquals(substitution, iValue.match(variable));

		// with predicate
		PrologStructure structure = provider.parseStructure("some_predicate(a,b,c)");
		assertEquals(new HashMap<String, PrologTerm>(), iValue.match(structure));

		// with list
		PrologList list = provider.parseList("[a,b,c]");
		assertEquals(new HashMap<String, PrologTerm>(), iValue.match(list));

		// with expression
		PrologTerm expression = provider.parseTerm("58+93*10");
		assertEquals(new HashMap<String, PrologTerm>(), iValue.match(expression));
	}

}

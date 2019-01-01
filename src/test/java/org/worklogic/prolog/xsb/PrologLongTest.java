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
import static org.logicware.prolog.PrologTermType.LONG_TYPE;

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

import com.declarativa.interprolog.TermModel;

public class PrologLongTest extends PrologBaseTest {

	private PrologLong long1 = provider.newLong(100);

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetLongValue() {
		assertEquals(100, long1.getLongValue());
	}

	@Test
	public void testGetDoubleValue() {
		assertEquals(100.0, long1.getDoubleValue(), 0);
	}

	@Test
	public void testGetPrologInteger() {
		assertEquals(provider.newInteger(100), long1.getPrologInteger());
	}

	@Test
	public void testGetPrologFloat() {
		assertEquals(provider.newFloat(100.0), long1.getPrologFloat());
	}

	@Test
	public final void testGetPrologLong() {
		assertEquals(provider.newLong(100), long1.getPrologLong());
	}

	@Test
	public final void testGetPrologDouble() {
		assertEquals(provider.newDouble(100.0), long1.getPrologDouble());
	}

	@Test
	public void testPrologInteger() {
		TermModel integer = new TermModel(0);
		assertEquals(0, integer.longValue());
	}

	@Test
	public void testPrologIntegerLong() {
		assertEquals(100, new TermModel(100).longValue());
	}

	@Test
	public void testEqualsObject() {
		assertTrue(long1.equals(provider.newLong(100)));
	}

	@Test
	public void testToString() {
		assertEquals("100", long1.toString());
	}

	@Test(expected = ArityError.class)
	public final void testGetArity() {
		long1.getArity();
	}

	@Test(expected = FunctorError.class)
	public final void testGetFunctor() {
		long1.getFunctor();
	}

	@Test
	public void testGetArguments() {
		assertArrayEquals(new PrologTerm[0], long1.getArguments());
	}

	@Test
	public void testGetType() {
		assertEquals(LONG_TYPE, long1.getType());
	}

	@Test
	public void testIsAtom() {
		assertFalse(long1.isAtom());
	}

	@Test
	public void testIsNumber() {
		assertTrue(long1.isNumber());
	}

	@Test
	public void testIsFloat() {
		assertFalse(long1.isFloat());
	}

	@Test
	public void testIsInteger() {
		assertFalse(long1.isInteger());
	}

	@Test
	public final void testIsDouble() {
		assertFalse(long1.isDouble());
	}

	@Test
	public final void testIsLong() {
		assertTrue(long1.isLong());
	}

	@Test
	public void testIsVariable() {
		assertFalse(long1.isVariable());
	}

	@Test
	public void testIsList() {
		assertFalse(long1.isList());
	}

	@Test
	public void testIsStructure() {
		assertFalse(long1.isStructure());
	}

	@Test
	public void testIsNil() {
		assertFalse(long1.isNil());
	}

	@Test
	public void testIsEmptyList() {
		assertFalse(long1.isEmptyList());
	}

	@Test
	public void testIsExpression() {
		assertFalse(long1.isEvaluable());
	}

	@Test(expected = IndicatorError.class)
	public void testGetKey() {
		long1.getIndicator();
	}

	@Test
	public void testUnify() {

		// with atom
		PrologLong lValue = provider.newLong(28);
		PrologAtom atom = provider.newAtom("John Doe");
		assertFalse(lValue.unify(atom));

		// with integer
		PrologInteger iValue = provider.newInteger(36);
		// false because they are different
		assertFalse(lValue.unify(iValue));

		// with long
		PrologLong lValue1 = provider.newLong(36);
		// true because are equals
		assertTrue(lValue.unify(lValue));
		// false because they are different
		assertFalse(lValue.unify(lValue1));

		// with float
		PrologFloat fValue = provider.newFloat(36.47);
		assertFalse(lValue.unify(fValue));

		// with double
		PrologDouble dValue = provider.newDouble(36.47);
		assertFalse(lValue.unify(dValue));

		// with variable
		PrologVariable variable = provider.newVariable("X", 0);
		// true. case atom and variable
		assertTrue(lValue.unify(variable));

		// with predicate
		PrologStructure structure = provider.parseStructure("some_predicate(a,b,c)");
		assertFalse(lValue.unify(structure));

		// with list
		PrologList list = provider.parseList("[a,b,c]");
		assertFalse(lValue.unify(list));

		// with expression
		PrologTerm expression = provider.parseTerm("58+93*10");
		assertFalse(lValue.unify(expression));
	}

	@Test
	public void testCompareTo() {

		// with atom
		PrologLong lValue = provider.newLong(28);
		PrologAtom atom = provider.newAtom("John Doe");
		assertEquals(-1, lValue.compareTo(atom));

		// with integer
		PrologInteger iValue = provider.newInteger(36);
		// false because they are different
		assertEquals(-1, lValue.compareTo(iValue));

		// with long
		PrologLong lValue1 = provider.newLong(36);
		// true because are equals
		assertEquals(0, lValue.compareTo(lValue));
		// false because they are different
		assertEquals(-1, lValue.compareTo(lValue1));

		// with float
		PrologFloat fValue = provider.newFloat(36.47);
		assertEquals(-1, lValue.compareTo(fValue));

		// with double
		PrologDouble dValue = provider.newDouble(36.47);
		assertEquals(-1, lValue.compareTo(dValue));

		// with variable
		PrologVariable variable = provider.newVariable("X", 0);
		assertEquals(1, lValue.compareTo(variable));

		// with predicate
		PrologStructure structure = provider.parseStructure("some_predicate(a,b,c)");
		assertEquals(-1, lValue.compareTo(structure));

		// with list
		PrologList list = provider.parseList("[a,b,c]");
		assertEquals(-1, lValue.compareTo(list));

		// with expression
		PrologTerm expression = provider.parseTerm("58+93*10");
		assertEquals(-1, lValue.compareTo(expression));
	}

}

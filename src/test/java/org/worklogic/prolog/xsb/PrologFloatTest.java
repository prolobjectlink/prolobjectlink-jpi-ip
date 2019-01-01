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
import static org.logicware.prolog.PrologTermType.FLOAT_TYPE;

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

public class PrologFloatTest extends PrologBaseTest {

	private PrologFloat f;

	@Before
	public void setUp() throws Exception {
		f = provider.newFloat(3.14);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testGetArguments() {
		assertArrayEquals(new PrologInteger[0], f.getArguments());
	}

	@Test
	public final void testGetPrologInteger() {
		assertEquals(provider.newInteger(3), f.getPrologInteger());
	}

	@Test
	public final void testGetPrologFloat() {
		assertEquals(provider.newFloat(3.14), f.getPrologFloat());
	}

	@Test
	public final void testGetPrologLong() {
		assertEquals(provider.newLong(3), f.getPrologLong());
	}

	@Test
	public final void testGetPrologDouble() {
		assertEquals(provider.newDouble(3.14F), f.getPrologDouble());
	}

	@Test
	public final void testGetLongValue() {
		assertEquals(3, f.getLongValue());
	}

	@Test
	public final void testGetDoubleValue() {
		assertEquals(3.14F, f.getDoubleValue(), 0);
	}

	@Test
	public final void testGetIntValue() {
		assertEquals(3, f.getIntValue());
	}

	@Test
	public final void testGetFloatValue() {
		assertEquals(3.140000104904175, f.getFloatValue(), 0);
	}

	@Test(expected = IndicatorError.class)
	public final void testGetKey() {
		f.getIndicator();
	}

	@Test
	public final void testGetType() {
		assertEquals(FLOAT_TYPE, f.getType());
	}

	@Test
	public final void testIsAtom() {
		assertFalse(f.isAtom());
	}

	@Test
	public final void testIsNumber() {
		assertTrue(f.isNumber());
	}

	@Test
	public final void testIsFloat() {
		assertTrue(f.isFloat());
	}

	@Test
	public final void testIsInteger() {
		assertFalse(f.isInteger());
	}

	@Test
	public final void testIsVariable() {
		assertFalse(f.isVariable());
	}

	@Test
	public final void testIsList() {
		assertFalse(f.isList());
	}

	@Test
	public final void testIsStructure() {
		assertFalse(f.isStructure());
	}

	@Test
	public final void testIsNil() {
		assertFalse(f.isNil());
	}

	@Test
	public final void testIsEmptyList() {
		assertFalse(f.isEmptyList());
	}

	@Test
	public final void testIsExpression() {
		assertFalse(f.isEvaluable());
	}

	@Test(expected = ArityError.class)
	public final void testGetArity() {
		f.getArity();
	}

	@Test(expected = FunctorError.class)
	public final void testGetFunctor() {
		f.getFunctor();
	}

	@Test
	public final void testUntify() {

		// with atom
		PrologFloat fValue = provider.newFloat(36.47);
		PrologAtom atom = provider.newAtom("doe");
		assertFalse(fValue.unify(atom));

		// with integer
		PrologInteger iValue = provider.newInteger(28);
		assertFalse(fValue.unify(iValue));

		// with long
		PrologLong lValue = provider.newLong(28);
		assertFalse(fValue.unify(lValue));

		// with float
		PrologFloat fValue1 = provider.newFloat(100.98);
		// true because are equals
		assertTrue(fValue.unify(fValue));
		// false because are different
		assertFalse(fValue.unify(fValue1));

		// with double
		PrologDouble dValue = provider.newDouble(36.47);
		PrologDouble dValue1 = provider.newDouble(100.98);
		// true because are equals
		assertTrue(fValue.unify(dValue));
		// false because are different
		assertFalse(fValue.unify(dValue1));

		// with variable
		PrologVariable variable = provider.newVariable("X", 0);
		// true. case float and variable
		assertTrue(fValue.unify(variable));

		// with predicate
		PrologStructure structure = provider.parseStructure("some_predicate(a,b,c)");
		assertFalse(fValue.unify(structure));

		// with list
		PrologList flattenedList = provider.parseList("[a,b,c]");
		assertFalse(fValue.unify(flattenedList));

		// with expression
		PrologTerm expression = provider.parseTerm("58+93*10");
		assertFalse(fValue.unify(expression));

	}

	@Test
	public final void testCompareTo() {

		// with atom
		PrologFloat fValue = provider.newFloat(36.47);
		PrologAtom atom = provider.newAtom("doe");
		assertEquals(-1, fValue.compareTo(atom));

		// with integer
		PrologInteger iValue = provider.newInteger(28);
		assertEquals(1, fValue.compareTo(iValue));

		// with long
		PrologLong lValue = provider.newLong(28);
		assertEquals(1, fValue.compareTo(lValue));

		// with float
		PrologFloat fValue1 = provider.newFloat(100.98);
		// true because are equals
		assertEquals(0, fValue.compareTo(fValue));
		// false because are different
		assertEquals(-1, fValue.compareTo(fValue1));

		// with double
		PrologDouble dValue = provider.newDouble(36.47);
		PrologDouble dValue1 = provider.newDouble(100.98);
		// true because are equals
		assertEquals(0, fValue.compareTo(dValue));
		// false because are different
		assertEquals(-1, fValue.compareTo(dValue1));

		// with variable
		PrologVariable variable = provider.newVariable("X", 0);
		// true. case float and variable
		assertEquals(1, fValue.compareTo(variable));

		// with predicate
		PrologStructure structure = provider.parseStructure("some_predicate(a,b,c)");
		assertEquals(-1, fValue.compareTo(structure));

		// with list
		PrologList flattenedList = provider.parseList("[a,b,c]");
		assertEquals(-1, fValue.compareTo(flattenedList));

		// with expression
		PrologTerm expression = provider.parseTerm("58+93*10");
		assertEquals(-1, fValue.compareTo(expression));

	}

	@Test
	public final void testMatch() {

		// with atom
		PrologFloat fValue = provider.newFloat(36.47);
		PrologAtom atom = provider.newAtom("doe");
		assertEquals(new HashMap<String, PrologTerm>(), fValue.match(atom));

		// with integer
		PrologInteger iValue = provider.newInteger(28);
		assertEquals(new HashMap<String, PrologTerm>(), fValue.match(iValue));

		// with float
		PrologFloat fValue1 = provider.newFloat(100.98);
		// true because are equals
		assertEquals(new HashMap<String, PrologTerm>(), fValue.match(fValue));
		// false because are different
		assertEquals(new HashMap<String, PrologTerm>(), fValue.match(fValue1));

		// with variable
		HashMap<String, PrologTerm> substitution = new HashMap<String, PrologTerm>(1);
		substitution.put("X", provider.newFloat(36.47));
		PrologVariable variable = provider.newVariable("X", 0);
		assertEquals(substitution, fValue.match(variable));

		// with predicate
		PrologStructure structure = provider.parseStructure("some_predicate(a,b,c)");
		assertEquals(new HashMap<String, PrologTerm>(), fValue.match(structure));

		// with list
		PrologList flattenedList = provider.parseList("[a,b,c]");
		assertEquals(new HashMap<String, PrologTerm>(), fValue.match(flattenedList));

		// with expression
		PrologTerm expression = provider.parseTerm("58+93*10");
		assertEquals(new HashMap<String, PrologTerm>(), fValue.match(expression));

	}

}

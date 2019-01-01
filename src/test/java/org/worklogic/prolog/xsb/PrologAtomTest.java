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
import static org.logicware.prolog.PrologTermType.ATOM_TYPE;

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
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

public class PrologAtomTest extends PrologBaseTest {

	private PrologAtom atom;

	@Before
	public void setUp() throws Exception {
		atom = provider.newAtom("an_atom");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testGetArguments() {
		assertArrayEquals(new PrologAtom[0], atom.getArguments());
	}

	@Test
	public final void testGetValue() {
		assertEquals("an_atom", atom.getStringValue());
	}

	@Test
	public final void testSetValue() {
		assertEquals("an_atom", atom.getStringValue());
		atom.setStringValue("other_atom_value");
		assertEquals("other_atom_value", atom.getStringValue());
	}

	@Test
	public final void testGetKey() {
		assertEquals("an_atom/0", atom.getIndicator());
	}

	@Test
	public final void testGetType() {
		assertEquals(ATOM_TYPE, atom.getType());
	}

	@Test
	public final void testIsAtom() {
		assertTrue(atom.isAtom());
	}

	@Test
	public final void testIsNumber() {
		assertFalse(atom.isNumber());
	}

	@Test
	public final void testIsFloat() {
		assertFalse(atom.isFloat());
	}

	@Test
	public final void testIsInteger() {
		assertFalse(atom.isInteger());
	}

	@Test
	public final void testIsVariable() {
		assertFalse(atom.isVariable());
	}

	@Test
	public final void testIsList() {
		assertFalse(atom.isList());
	}

	@Test
	public final void testIsStructure() {
		assertFalse(atom.isStructure());
	}

	@Test
	public final void testIsNil() {
		assertFalse(atom.isNil());
	}

	@Test
	public final void testIsEmptyList() {
		assertFalse(atom.isEmptyList());
	}

	@Test
	public final void testIsExpression() {
		assertFalse(atom.isEvaluable());
	}

	@Test
	public final void testIsAtomic() {
		assertTrue(atom.isAtomic());
	}

	@Test
	public final void testIsCompound() {
		assertFalse(atom.isCompound());
	}

	@Test
	public final void testGetArity() {
		assertEquals(0, atom.getArity());
	}

	@Test
	public final void testGetFunctor() {
		assertEquals("an_atom", atom.getFunctor());
	}

	@Test
	public final void testUnify() {
		// with atom
		PrologAtom atom = provider.newAtom("smith");
		PrologAtom atom1 = provider.newAtom("doe");
		// true because the atoms are equals
		assertTrue(atom.unify(atom));
		// false because the atoms are different
		assertFalse(atom.unify(atom1));

		// with integer
		PrologInteger iValue = provider.newInteger(28);
		assertFalse(atom.unify(iValue));

		// with long
		PrologLong lValue = provider.newLong(28);
		assertFalse(atom.unify(lValue));

		// with float
		PrologFloat fValue = provider.newFloat(36.47);
		assertFalse(atom.unify(fValue));

		// with double
		PrologDouble dValue = provider.newDouble(36.47);
		assertFalse(atom.unify(dValue));

		// with variable
		PrologVariable variable = provider.newVariable("X", 0);
		// true. case atom and variable
		assertTrue(atom.unify(variable));

		// with predicate
		PrologStructure structure = provider.parseStructure("some_predicate(a,b,c)");
		assertFalse(atom.unify(structure));

		// with list
		PrologList flattenedList = provider.parseList("[a,b,c]");
		assertFalse(atom.unify(flattenedList));

		// with expression
		PrologTerm expression = provider.parseTerm("58+93*10");
		assertFalse(atom.unify(expression));
	}

	@Test
	public final void testCompareTo() {

		// with atom
		PrologAtom atom = provider.newAtom("smith");
		PrologAtom atom1 = provider.newAtom("doe");
		// true because the atoms are equals
		assertEquals(0, atom.compareTo(atom));
		// false because the atoms are different
		assertEquals(1, atom.compareTo(atom1));

		// with integer
		PrologInteger iValue = provider.newInteger(28);
		assertEquals(1, atom.compareTo(iValue));

		// with long
		PrologLong lValue = provider.newLong(28);
		assertEquals(1, atom.compareTo(lValue));

		// with float
		PrologFloat fValue = provider.newFloat(36.47);
		assertEquals(1, atom.compareTo(fValue));

		// with double
		PrologDouble dValue = provider.newDouble(36.47);
		assertEquals(1, atom.compareTo(dValue));

		// with variable
		PrologVariable variable = provider.newVariable("X", 0);
		// true. case atom and variable
		assertEquals(1, atom.compareTo(variable));

		// with predicate
		PrologStructure structure = provider.parseStructure("some_predicate(a,b,c)");
		assertEquals(-1, atom.compareTo(structure));

		// with list
		PrologList flattenedList = provider.parseList("[a,b,c]");
		assertEquals(-1, atom.compareTo(flattenedList));

		// with expression
		PrologTerm expression = provider.parseTerm("58+93*10");
		assertEquals(-1, atom.compareTo(expression));

	}

	@Test
	public final void testMatch() {

		// with atom
		PrologAtom atom = provider.newAtom("smith");
		PrologAtom atom1 = provider.newAtom("doe");
		assertEquals(new HashMap<String, PrologTerm>(), atom.match(atom));
		assertEquals(new HashMap<String, PrologTerm>(), atom.match(atom1));

		// with integer
		PrologInteger iValue = provider.newInteger(28);
		assertEquals(new HashMap<String, PrologTerm>(), atom.match(iValue));

		// with float
		PrologFloat fValue = provider.newFloat(36.47);
		assertEquals(new HashMap<String, PrologTerm>(), atom.match(fValue));

		// with variable
		HashMap<String, PrologTerm> substitution = new HashMap<String, PrologTerm>(1);
		substitution.put("X", provider.newAtom("smith"));
		PrologVariable variable = provider.newVariable("X", 0);
		assertEquals(substitution, atom.match(variable));

		// with predicate
		PrologStructure structure = provider.parseStructure("some_predicate(a,b,c)");
		assertEquals(new HashMap<String, PrologTerm>(), atom.match(structure));

		// with list
		PrologList flattenedList = provider.parseList("[a,b,c]");
		assertEquals(new HashMap<String, PrologTerm>(), atom.match(flattenedList));

		// with expression
		PrologTerm expression = provider.parseTerm("58+93*10");
		assertEquals(new HashMap<String, PrologTerm>(), atom.match(expression));
	}

}

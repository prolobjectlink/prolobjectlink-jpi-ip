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
import static org.junit.Assert.assertNotNull;
import static org.logicware.prolog.PrologTermType.ATOM_TYPE;
import static org.logicware.prolog.PrologTermType.FLOAT_TYPE;
import static org.logicware.prolog.PrologTermType.INTEGER_TYPE;
import static org.logicware.prolog.PrologTermType.LIST_TYPE;
import static org.logicware.prolog.PrologTermType.STRUCTURE_TYPE;
import static org.logicware.prolog.PrologTermType.VARIABLE_TYPE;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.logicware.prolog.PrologAtom;
import org.logicware.prolog.PrologDouble;
import org.logicware.prolog.PrologFloat;
import org.logicware.prolog.PrologInteger;
import org.logicware.prolog.PrologList;
import org.logicware.prolog.PrologStructure;
import org.logicware.prolog.PrologTerm;
import org.logicware.prolog.PrologVariable;

public class PrologProviderTest extends PrologBaseTest {

	@Before
	public void setUp() throws Exception {

		solution[0][0] = mcardon;
		solution[0][1] = one;
		solution[0][2] = five;
		solution[0][3] = board;
		solution[0][4] = threeThousand;

		solution[1][0] = treeman;
		solution[1][1] = two;
		solution[1][2] = three;
		solution[1][3] = human_resources;
		solution[1][4] = twoThousand;

		solution[2][0] = chapman;
		solution[2][1] = one;
		solution[2][2] = two;
		solution[2][3] = board;
		solution[2][4] = thousandFiveHundred;

		solution[3][0] = claessen;
		solution[3][1] = four;
		solution[3][2] = one;
		solution[3][3] = technical_services;
		solution[3][4] = thousand;

		solution[4][0] = petersen;
		solution[4][1] = five;
		solution[4][2] = eight;
		solution[4][3] = administration;
		solution[4][4] = fourThousandFiveHundred;

		solution[5][0] = cohn;
		solution[5][1] = one;
		solution[5][2] = seven;
		solution[5][3] = board;
		solution[5][4] = fourThousand;

		solution[6][0] = duffy;
		solution[6][1] = one;
		solution[6][2] = nine;
		solution[6][3] = board;
		solution[6][4] = fiveThousand;

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testNewEngine() {
		assertNotNull(provider.newEngine());
		// assertEquals(new EngineAdapter(), factory.newEngine());
	}

	@Test
	public final void testIsCompilant() {
		assertFalse(provider.isCompliant());
	}

	@Test
	public final void testNewPrologAtom() {
		PrologAtom atom = provider.newAtom("an_atom");
		assertEquals(ATOM_TYPE, atom.getType());
		assertEquals(provider.newAtom("an_atom"), atom);
		assertEquals("an_atom", atom.getFunctor());
		assertEquals("an_atom/0", atom.getIndicator());
		assertEquals(0, atom.getArity());
	}

	@Test
	public final void testNewPrologFloat() {
		PrologFloat f = provider.newFloat(3.14);
		assertEquals(FLOAT_TYPE, f.getType());
		assertEquals(provider.newFloat(3.14), f);
		assertEquals(3.14F, f.getFloatValue(), 0);
	}

	@Test
	public final void testNewPrologInteger() {
		PrologInteger integer = provider.newInteger(100);
		assertEquals(INTEGER_TYPE, integer.getType());
		assertEquals(provider.newInteger(100), integer);
		assertEquals(100, integer.getIntValue());
	}

	@Test
	public final void testNewPrologVariable() {
		PrologVariable variable = provider.newVariable(0);
		assertEquals(VARIABLE_TYPE, variable.getType());
		assertEquals(provider.newVariable(0), variable);
		assertEquals("_", variable.getName());
	}

	@Test
	public final void testNewPrologVariableString() {
		PrologVariable variable = provider.newVariable("X", 0);
		assertEquals(VARIABLE_TYPE, variable.getType());
		assertEquals(provider.newVariable("X", 0), variable);
		assertEquals("X", variable.getName());
	}

	@Test
	public final void testNewPrologVariableInt() {
		PrologVariable variable = provider.newVariable(0);
		assertEquals(VARIABLE_TYPE, variable.getType());
		assertEquals(provider.newVariable(0), variable);
		assertEquals("_", variable.getName());
	}

	@Test
	public final void testNewPrologVariableStringInt() {
		PrologVariable variable = provider.newVariable("X", 0);
		assertEquals(VARIABLE_TYPE, variable.getType());
		assertEquals(provider.newVariable("X", 0), variable);
		assertEquals("X", variable.getName());
	}

	@Test
	public final void testNewPrologList() {
		PrologList list = provider.newList();
		assertEquals(provider.newList(), list);
		assertEquals(LIST_TYPE, list.getType());
		assertEquals("[]", list.getFunctor());
		assertEquals("[]/0", list.getIndicator());
		assertEquals(0, list.getArity());
	}

	@Test
	public final void testNewPrologListPrologTermArray() {

		PrologList list = provider
				.newList(new PrologTerm[] { zero, one, two, three, four, five, six, seven, eight, nine });
		assertEquals(provider.newList(new PrologTerm[] { zero, one, two, three, four, five, six, seven, eight, nine }),
				list);
		assertEquals(LIST_TYPE, list.getType());
		assertEquals(".", list.getFunctor());
		assertEquals("./2", list.getIndicator());
		assertEquals(2, list.getArity());

		list = provider.newList(new PrologTerm[0]);
		assertEquals(provider.newList(new PrologTerm[0]), list);
		assertEquals(LIST_TYPE, list.getType());
		assertEquals("[]", list.getFunctor());
		assertEquals("[]/0", list.getIndicator());
		assertEquals(0, list.getArity());

		list = provider.newList(null);
		assertEquals(provider.newList(null), list);
		assertEquals(LIST_TYPE, list.getType());
		assertEquals("[]", list.getFunctor());
		assertEquals("[]/0", list.getIndicator());
		assertEquals(0, list.getArity());

	}

	@Test
	public final void testNewPrologListPrologTermPrologTerm() {

		PrologList list = provider.newList(zero, one);
		assertEquals(provider.newList(zero, one), list);
		assertEquals(LIST_TYPE, list.getType());
		assertEquals(".", list.getFunctor());
		assertEquals("./2", list.getIndicator());
		assertEquals(2, list.getArity());

		list = provider.newList(zero, provider.prologEmpty());
		assertEquals(provider.newList(zero, provider.prologEmpty()), list);
		assertEquals(LIST_TYPE, list.getType());
		assertEquals(".", list.getFunctor());
		assertEquals("./2", list.getIndicator());
		assertEquals(2, list.getArity());

	}

	@Test
	public final void testNewPrologListPrologTermArrayPrologTerm() {

		PrologList list = provider.newList(
				new PrologTerm[] { zero, one, two, three, four, five, six, seven, eight, nine },
				provider.prologEmpty());
		assertEquals(provider.newList(new PrologTerm[] { zero, one, two, three, four, five, six, seven, eight, nine },
				provider.prologEmpty()), list);
		assertEquals(LIST_TYPE, list.getType());
		assertEquals(".", list.getFunctor());
		assertEquals("./2", list.getIndicator());
		assertEquals(2, list.getArity());

		list = provider.newList(new PrologTerm[0], provider.prologEmpty());
		assertEquals(provider.newList(new PrologTerm[0], provider.prologEmpty()), list);
		assertEquals(LIST_TYPE, list.getType());
		assertEquals("[]", list.getFunctor());
		assertEquals("[]/0", list.getIndicator());
		assertEquals(0, list.getArity());

	}

	@Test
	public final void testNewPrologStructure() {
		PrologStructure structure = provider.newStructure("digits", zero, one, two, three, four, five, six, seven,
				eight, nine);
		assertEquals(provider.newStructure("digits", zero, one, two, three, four, five, six, seven, eight, nine),
				structure);
		assertEquals(STRUCTURE_TYPE, structure.getType());
		assertEquals("digits", structure.getFunctor());
		assertEquals("digits/10", structure.getIndicator());
		assertEquals(10, structure.getArity());
	}

	@Test
	public final void testNewPrologExpression() {
		PrologTerm expression = provider.newStructure(x, "+", y);
		assertEquals(provider.newStructure(x, "+", y), expression);
		assertEquals(STRUCTURE_TYPE, expression.getType());
		assertEquals("+", expression.getFunctor());
		assertEquals("+/2", expression.getIndicator());
		assertEquals(2, expression.getArity());
	}

	@Test
	public final void testParseTerms() {

		PrologTerm employeeStructure = provider.newStructure(employee, name, dpto, scale);
		PrologTerm departmentStructure = provider.newStructure(department, dpto, dptoName);
		PrologTerm salaryStructure = provider.newStructure(salary, scale, money);

		PrologTerm expression = provider.newStructure(money, "<", provider.newInteger(2000));

		PrologTerm[] structures = new PrologTerm[] { employeeStructure, departmentStructure, salaryStructure,
				expression };
		assertArrayEquals(structures, provider.parseTerms(
				"employee(Name,Dpto,Scale),department(Dpto,DepartmentName),salary(Scale,Money),Money < 2000"));

		assertArrayEquals(new PrologTerm[] { provider.newStructure(employee, name, dpto, scale) },
				provider.parseTerms("employee(Name,Dpto,Scale)"));
		assertArrayEquals(new PrologTerm[] { name }, provider.parseTerms("','(Name,Dpto)"));
		assertArrayEquals(new PrologTerm[] { provider.newStructure("','", name) }, provider.parseTerms("','(Name)"));
		assertArrayEquals(new PrologTerm[0], provider.parseTerms("15"));
		assertArrayEquals(new PrologTerm[0], provider.parseTerms(""));

	}

	// @Ignore
	@Test
	public final void testParseTerm() {

		assertEquals(provider.prologCut(), provider.parseTerm("!"));
		assertEquals(provider.prologNil(), provider.parseTerm("nil"));
		assertEquals(provider.prologTrue(), provider.parseTerm("true"));
		assertEquals(provider.prologFalse(), provider.parseTerm("false"));
		assertEquals(provider.prologFail(), provider.parseTerm("fail"));
		assertEquals(provider.prologEmpty(), provider.parseTerm("[]"));

		PrologAtom atom = (PrologAtom) provider.parseTerm("an_atom");
		assertEquals(provider.newAtom("an_atom"), atom);

		// be careful this engine remove quotes and rise an exception if not
		PrologAtom complex_atom = (PrologAtom) provider.parseTerm("'an complex atom'");
		assertEquals(provider.newAtom("an complex atom"), complex_atom);

		// IPrologFloat f = (IPrologFloat) factory.parseTerm("3.14");
		// assertEquals(new FloatAdapter(3.14), f);

		PrologDouble d = (PrologDouble) provider.parseTerm("3.14");
		assertEquals(provider.newDouble(3.14), d);

		PrologList list = (PrologList) provider.parseTerm("[0,1,2,3,4,5,6,7,8,9]");
		assertEquals(provider.newList(new PrologTerm[] { zero, one, two, three, four, five, six, seven, eight, nine }),
				list);

		PrologStructure structure = (PrologStructure) provider.parseTerm("digits(0,1,2,3,4,5,6,7,8,9)");
		assertEquals(provider.newStructure("digits", zero, one, two, three, four, five, six, seven, eight, nine),
				structure);

		PrologStructure complex_structure = (PrologStructure) provider.parseTerm("'digits'(0,1,2,3,4,5,6,7,8,9)");
		assertEquals(provider.newStructure("digits", zero, one, two, three, four, five, six, seven, eight, nine),
				complex_structure);

		// PrologExpression expression = (PrologExpression)
		// provider.parsePrologTerm("X+Y");
		// assertEquals(provider.newExpression(x, "+", y), expression);

	}

	@Test
	public final void testToString() {

		assertEquals(XsbProlog.class.getSimpleName() + " [converter=" + XsbPrologConverter.class.getSimpleName() + "]",
				provider.toString());

	}

}

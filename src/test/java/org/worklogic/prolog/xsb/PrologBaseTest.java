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

import java.util.HashMap;
import java.util.Map;

import org.logicware.prolog.Prolog;
import org.logicware.prolog.PrologAtom;
import org.logicware.prolog.PrologDouble;
import org.logicware.prolog.PrologFloat;
import org.logicware.prolog.PrologInteger;
import org.logicware.prolog.PrologProvider;
import org.logicware.prolog.PrologTerm;
import org.logicware.prolog.PrologVariable;

public abstract class PrologBaseTest {

	protected String male = "male";
	protected String parent = "parent";
	protected String female = "female";
	protected String mother = "mother";
	protected String sister = "sister";
	protected String different = "different";
	protected String offspring = "offspring";
	protected String grandparent = "grandparent";
	protected String predecessor = "predecessor";

	protected String salary = "salary";
	protected String employee = "employee";
	protected String department = "department";

	protected static final PrologProvider provider = Prolog.newProvider(XsbProlog.class);

	protected PrologTerm nil = provider.prologNil();
	protected PrologTerm empty = provider.prologEmpty();

	protected PrologVariable x = provider.newVariable("X", 0);
	protected PrologVariable y = provider.newVariable("Y", 1);
	protected PrologVariable z = provider.newVariable("Z", 2);

	protected PrologVariable name = provider.newVariable("Name", 0);
	protected PrologVariable dpto = provider.newVariable("Dpto", 1);
	protected PrologVariable scale = provider.newVariable("Scale", 2);
	protected PrologVariable dptoName = provider.newVariable("DepartmentName", 3);
	protected PrologVariable money = provider.newVariable("Money", 4);

	protected PrologAtom pam = provider.newAtom("pam");
	protected PrologAtom bob = provider.newAtom("bob");
	protected PrologAtom tom = provider.newAtom("tom");
	protected PrologAtom liz = provider.newAtom("liz");
	protected PrologAtom ann = provider.newAtom("ann");
	protected PrologAtom pat = provider.newAtom("pat");
	protected PrologAtom jim = provider.newAtom("jim");

	protected PrologInteger zero = provider.newInteger();
	protected PrologInteger one = provider.newInteger(1);
	protected PrologInteger two = provider.newInteger(2);
	protected PrologInteger three = provider.newInteger(3);
	protected PrologInteger four = provider.newInteger(4);
	protected PrologInteger five = provider.newInteger(5);
	protected PrologInteger six = provider.newInteger(6);
	protected PrologInteger seven = provider.newInteger(7);
	protected PrologInteger eight = provider.newInteger(8);
	protected PrologInteger nine = provider.newInteger(9);
	protected PrologInteger ten = provider.newInteger(10);

	protected PrologDouble gauss = provider.newDouble(.8346268);
	protected PrologDouble aureo = provider.newDouble(1.6180339887);
	protected PrologDouble pi = provider.newDouble(3.14159);
	protected PrologFloat euler = provider.newFloat(2.71828);

	protected PrologAtom cat = provider.newAtom("cat");
	protected PrologAtom bear = provider.newAtom("bear");
	protected PrologAtom elephant = provider.newAtom("elephant");

	protected PrologAtom mcardon = provider.newAtom("mcardon");
	protected PrologAtom treeman = provider.newAtom("treeman");
	protected PrologAtom chapman = provider.newAtom("chapman");
	protected PrologAtom claessen = provider.newAtom("claessen");
	protected PrologAtom petersen = provider.newAtom("petersen");
	protected PrologAtom cohn = provider.newAtom("cohn");
	protected PrologAtom duffy = provider.newAtom("duffy");

	protected PrologAtom board = provider.newAtom("board");
	protected PrologAtom human_resources = provider.newAtom("human_resources");
	protected PrologAtom production = provider.newAtom("production");
	protected PrologAtom technical_services = provider.newAtom("technical_services");
	protected PrologAtom administration = provider.newAtom("administration");

	protected PrologInteger thousand = provider.newInteger(1000);
	protected PrologInteger thousandFiveHundred = provider.newInteger(1500);
	protected PrologInteger twoThousand = provider.newInteger(2000);
	protected PrologInteger twoThousandFiveHundred = provider.newInteger(2500);
	protected PrologInteger threeThousand = provider.newInteger(3000);
	protected PrologInteger threeThousandFiveHundred = provider.newInteger(3500);
	protected PrologTerm fourThousand = provider.newInteger(4000);
	protected PrologTerm fourThousandFiveHundred = provider.newInteger(4500);
	protected PrologTerm fiveThousand = provider.newInteger(5000);

	protected PrologTerm[] expecteds0 = new PrologTerm[] { mcardon, one, five, board, threeThousand };
	protected PrologTerm[] expecteds1 = new PrologTerm[] { treeman, two, three, human_resources, twoThousand };
	protected PrologTerm[] expecteds2 = new PrologTerm[] { chapman, one, two, board, thousandFiveHundred };
	protected PrologTerm[] expecteds3 = new PrologTerm[] { claessen, four, one, technical_services, thousand };
	protected PrologTerm[] expecteds4 = new PrologTerm[] { petersen, five, eight, administration,
			fourThousandFiveHundred };
	protected PrologTerm[] expecteds5 = new PrologTerm[] { cohn, one, seven, board, fourThousand };
	protected PrologTerm[] expecteds6 = new PrologTerm[] { duffy, one, nine, board, fiveThousand };

	protected PrologTerm[][] solution = new PrologTerm[7][5];

	protected Map<String, PrologTerm> solutionMap;
	protected Map<String, PrologTerm>[] allSolutionMap;

	protected Map<String, PrologTerm>[] famillyAllSolutionMap;
	protected Map<String, PrologTerm> famillySolutionMap = new HashMap<String, PrologTerm>();

}

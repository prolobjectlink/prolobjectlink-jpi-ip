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

import org.logicware.prolog.AbstractConsole;
import org.logicware.prolog.PrologConsole;

import com.declarativa.interprolog.PrologEngine;
import com.declarativa.interprolog.XSBSubprocessEngine;
import com.xsb.interprolog.NativeEngine;

public class XsbPrologConsole extends AbstractConsole implements PrologConsole {

	public XsbPrologConsole() {
		super(null/* new XsbProlog() */);
	}

	public static void main(String[] args) {
//		new XsbPrologConsole().run(args);
		subprocessEngine("C:/Program Files (x86)/XSB");
		nativeEngine("/path/to/libxsb.so/parent/dir");
	}

	private static void subprocessEngine(String xsbPath) {
		PrologEngine engine = new XSBSubprocessEngine(xsbPath);
		if (engine.deterministicGoal(
				"writeln('Hello XSB'), javaMessage('java.lang.System'-out,println(string('Hi Java, I am a sub-process!')))")) {
			System.out.println("Subprocess engine is OK!");
		} else {
			System.out.println("Subprocess engine is NOT OK!");
		}
		engine.shutdown();
	}

	private static void nativeEngine(String xsbPath) {
		PrologEngine engine = new NativeEngine(xsbPath);
		if (engine.deterministicGoal(
				"writeln('Hello XSB'), javaMessage('java.lang.System'-out,println(string('Hi Java, I am native!')))")) {
			System.out.println("Native engine is OK!");
		} else {
			System.out.println("Native engine is NOT OK!");
		}
		engine.shutdown();
	}
}

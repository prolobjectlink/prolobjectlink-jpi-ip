package org.worklogic.prolog.xsb;

import com.declarativa.interprolog.PrologEngine;
import com.declarativa.interprolog.XSBSubprocessEngine;
import com.xsb.interprolog.NativeEngine;

public class Main {

	public static void main(String[] args) {
		subprocessEngine("C:/Program Files (x86)/XSB");
//		nativeEngine("/path/to/libxsb.so/parent/dir");
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
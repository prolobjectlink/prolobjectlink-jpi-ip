/*
 * #%L
 * prolobjectlink-jpi-ip
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
package org.worklogic.prolog.interprolog;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;

import org.logicware.prolog.UnknownTermError;
import org.worklogic.logging.LoggerConstants;
import org.worklogic.logging.LoggerUtils;

import com.declarativa.interprolog.TermModel;
import com.igormaznitsa.prologparser.DefaultParserContext;
import com.igormaznitsa.prologparser.GenericPrologParser;
import com.igormaznitsa.prologparser.ParserContext;
import com.igormaznitsa.prologparser.PrologParser;
import com.igormaznitsa.prologparser.terms.PrologAtom;
import com.igormaznitsa.prologparser.terms.PrologCompound;
import com.igormaznitsa.prologparser.terms.PrologFloat;
import com.igormaznitsa.prologparser.terms.PrologInt;
import com.igormaznitsa.prologparser.terms.PrologTerm;
import com.igormaznitsa.prologparser.terms.PrologVar;
import com.igormaznitsa.prologparser.tokenizer.Op;

public final class InterPrologParser {

	protected final HashMap<String, TermModel> sharedPrologVariables;

	protected InterPrologParser() {
		sharedPrologVariables = new HashMap<String, TermModel>();
	}

	public TermModel parseTerm(final String term) {
		TermModel result = null;
		try {
			String temp = term;
			if (temp.lastIndexOf('.') != temp.length() - 1) {
				temp += ".";
			}
			Reader reader = new StringReader(temp);
			PrologParser parser = new GenericPrologParser(reader,
					new DefaultParserContext(ParserContext.FLAG_CURLY_BRACKETS, Op.SWI));
			if (parser.hasNext()) {
				PrologTerm t = parser.next();
				result = fromTerm(t);
			}
			parser.close();
		} catch (IOException e) {
			LoggerUtils.error(getClass(), LoggerConstants.IO, e);
		}
		return result;
	}

	public TermModel[] parseTerms(TermModel term) {
		return parseTerms("" + term + "");
	}

	public TermModel[] parseTerms(String stringTerms) {
		TermModel[] result = new TermModel[0];
		LinkedList<TermModel> list = new LinkedList<TermModel>();
		try {
			Reader reader = new StringReader(stringTerms);
			PrologParser parser = new GenericPrologParser(reader,
					new DefaultParserContext(ParserContext.FLAG_CURLY_BRACKETS, Op.SWI));
			for (PrologTerm prologTerm : parser) {
				list.add(fromTerm(prologTerm));
			}
			parser.close();
		} catch (IOException e) {
			LoggerUtils.error(getClass(), LoggerConstants.IO, e);
		}
		return list.toArray(result);
	}

	public InterPrologProgram parseProgram(String file) {
		return parseProgram(new File(file));
	}

	public InterPrologProgram parseProgram(File in) {
		PrologParser parser = null;
		InterPrologProgram program = new InterPrologProgram();
		try {
			Reader reader = new FileReader(in);
			parser = new GenericPrologParser(reader,
					new DefaultParserContext(ParserContext.FLAG_CURLY_BRACKETS, Op.SWI));
			for (PrologTerm prologTerm : parser) {
				program.add(fromTerm(prologTerm));
			}
		} catch (IOException e) {
			LoggerUtils.error(getClass(), LoggerConstants.IO, e);
		} finally {
			if (parser != null) {
				try {
					parser.close();
				} catch (IOException e) {
					LoggerUtils.error(getClass(), LoggerConstants.IO, e);
				}
			}
		}
		return program;
	}

	public TermModel fromTerm(PrologTerm term) {
		switch (term.getType()) {
		case ATOM:
			if (term instanceof PrologInt) {
				PrologInt i = (PrologInt) term;
				return new TermModel(i.getNumber());
			} else if (term instanceof PrologFloat) {
				PrologFloat f = (PrologFloat) term;
				return new TermModel(f.getNumber());
			} else {
				String functor = term.getText();
				if (functor.equals("nil")) {
					return new TermModel("nil");
				} else if (functor.equals("!")) {
					return new TermModel("!");
				} else if (functor.equals("fail")) {
					return new TermModel("fail");
				} else if (functor.equals("true")) {
					return new TermModel("true");
				} else if (functor.equals("false")) {
					return new TermModel("false");
				} else if (functor.equals("[]")) {
					return new TermModel("[]");
				} else {
					return new TermModel(((PrologAtom) term).getText());
				}
			}
		case VAR:
			String name = ((PrologVar) term).getText();
			int position = ((PrologVar) term).getPos();// FIXME CATCH THE VAR POSITION IN STRUCTURE ???
			TermModel variable = sharedPrologVariables.get(name);
			if (variable == null) {
				variable = new TermVariable(name, position);
				sharedPrologVariables.put(name, variable);
			}
			return variable;
		case LIST:
			PrologCompound compound = (PrologCompound) term;
			PrologTerm[] array = new PrologTerm[compound.getArity()];
			for (int i = 0; i < array.length; i++) {
				array[i] = compound.getTermAt(i);
			}
			TermModel list = new TermModel(".", fromTermArray(array));
			return list;
		case STRUCT:
			compound = (PrologCompound) term;
			array = new PrologTerm[compound.getArity()];
			String functor = term.getFunctor().toString();
			for (int i = 0; i < array.length; i++) {
				array[i] = compound.getTermAt(i);
			}
			TermModel[] arguments = fromTermArray(array);
			return new TermModel(functor, arguments);
		default:
			throw new UnknownTermError(term);
		}
	}

	public TermModel[] fromTermArray(PrologTerm[] terms) {
		TermModel[] prologTerms = new TermModel[terms.length];
		for (int i = 0; i < terms.length; i++) {
			prologTerms[i] = fromTerm(terms[i]);
		}
		return prologTerms;
	}

}

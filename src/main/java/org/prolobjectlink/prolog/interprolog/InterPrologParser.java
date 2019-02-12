/*
 * #%L
 * prolobjectlink-jpi-ip
 * %%
 * Copyright (C) 2019 Prolobjectlink Project
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
package org.prolobjectlink.prolog.interprolog;

import static org.prolobjectlink.prolog.AbstractConverter.SIMPLE_ATOM_REGEX;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import org.prolobjectlink.logging.LoggerConstants;
import org.prolobjectlink.logging.LoggerUtils;
import org.prolobjectlink.prolog.UnknownTermError;

import com.declarativa.interprolog.TermModel;
import com.igormaznitsa.prologparser.DefaultParserContext;
import com.igormaznitsa.prologparser.GenericPrologParser;
import com.igormaznitsa.prologparser.ParserContext;
import com.igormaznitsa.prologparser.PrologParser;
import com.igormaznitsa.prologparser.terms.PrologAtom;
import com.igormaznitsa.prologparser.terms.PrologCompound;
import com.igormaznitsa.prologparser.terms.PrologFloat;
import com.igormaznitsa.prologparser.terms.PrologInt;
import com.igormaznitsa.prologparser.terms.PrologStruct;
import com.igormaznitsa.prologparser.terms.PrologTerm;
import com.igormaznitsa.prologparser.terms.PrologVar;
import com.igormaznitsa.prologparser.tokenizer.Op;

public final class InterPrologParser {

	private int varIndex;
	private static final PrologAtom op = new PrologAtom("op");
	protected final HashMap<String, TermVariable> sharedPrologVariables;

	protected InterPrologParser() {
		sharedPrologVariables = new HashMap<String, TermVariable>();
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
		varIndex = 0;
		return result;
	}

	public TermModel[] parseTerms(TermModel term) {
		return parseTerms("" + term + "");
	}

	public TermModel[] parseTerms(final String stringTerms) {
		TermModel[] result = new TermModel[0];
		LinkedList<TermModel> list = new LinkedList<TermModel>();
		try {
			String temp = stringTerms;
			if (temp.lastIndexOf('.') != temp.length() - 1) {
				temp += ".";
			}
			Reader reader = new StringReader(temp);
			PrologParser parser = new GenericPrologParser(reader,
					new DefaultParserContext(ParserContext.FLAG_CURLY_BRACKETS, Op.SWI));
			for (PrologTerm ptr : parser) {
				while (ptr instanceof PrologStruct) {
					PrologStruct struct = (PrologStruct) ptr;
					if (struct.getText().equals(",") && struct.getArity() == 2) {
						list.add(fromTerm(struct.getTermAt(0)));
						ptr = struct.getTermAt(1);
					} else {
						list.add(fromTerm(ptr));
						break;
					}
				}
			}
			parser.close();
		} catch (IOException e) {
			LoggerUtils.error(getClass(), LoggerConstants.IO, e);
		}
		varIndex = 0;
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
				} else if (!functor.matches(SIMPLE_ATOM_REGEX)) {
					return new TermModel("'" + ((PrologAtom) term).getText() + "'");
				} else {
					return new TermModel(((PrologAtom) term).getText());
				}
			}
		case VAR:
			String name = ((PrologVar) term).getText();
//			int position = ((PrologVar) term).getPos();// FIXME CATCH THE VAR POSITION IN STRUCTURE ???
			TermVariable variable = sharedPrologVariables.get(name);
			if (variable == null) {
				variable = new TermVariable(name, varIndex++);
				sharedPrologVariables.put(name, variable);
			}
			return variable;
		case LIST:
			PrologCompound l = (PrologCompound) term;
			if (l.getArity() < 1) {
				return new TermModel("[]", new TermModel[0]);
			}
			ArrayList<TermModel> arrayList = new ArrayList<TermModel>();
			while (l.getArity() > 0) {
				arrayList.add(fromTerm(l.getTermAt(0)));
				l = (PrologCompound) l.getTermAt(1);
			}
			return TermModel.makeList(arrayList);
		case STRUCT:
			PrologCompound compound = (PrologCompound) term;
			PrologTerm[] args = new PrologTerm[compound.getArity()];
			String functor = term.getFunctor().toString();
			for (int i = 0; i < args.length; i++) {
				args[i] = compound.getTermAt(i);
			}
			TermModel[] arguments = fromTermArray(args);
			if (compound.getFunctor().equals(op) && compound.getArity() == 3) {
				String operator = compound.getTermAt(2).toString();
				return new TermModel(operator, arguments);
			} else if (compound.getFunctor() instanceof Op) {
				String operator = compound.getFunctor().getText();
				return new TermModel(operator, arguments);
			}
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

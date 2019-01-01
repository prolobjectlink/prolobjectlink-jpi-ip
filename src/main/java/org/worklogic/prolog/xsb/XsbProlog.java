package org.worklogic.prolog.xsb;

import org.logicware.prolog.AbstractProvider;
import org.logicware.prolog.PrologAtom;
import org.logicware.prolog.PrologConverter;
import org.logicware.prolog.PrologDouble;
import org.logicware.prolog.PrologEngine;
import org.logicware.prolog.PrologFloat;
import org.logicware.prolog.PrologInteger;
import org.logicware.prolog.PrologList;
import org.logicware.prolog.PrologLong;
import org.logicware.prolog.PrologProvider;
import org.logicware.prolog.PrologStructure;
import org.logicware.prolog.PrologTerm;
import org.logicware.prolog.PrologVariable;

public class XsbProlog extends AbstractProvider implements PrologProvider {

	public XsbProlog(PrologConverter<?> converter) {
		super(converter);
	}

	public PrologTerm parseTerm(String term) {
		// TODO Auto-generated method stub
		return null;
	}

	public PrologTerm[] parseTerms(String stringTerms) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isCompliant() {
		// TODO Auto-generated method stub
		return false;
	}

	public PrologTerm prologNil() {
		// TODO Auto-generated method stub
		return null;
	}

	public PrologTerm prologCut() {
		// TODO Auto-generated method stub
		return null;
	}

	public PrologTerm prologFail() {
		// TODO Auto-generated method stub
		return null;
	}

	public PrologTerm prologTrue() {
		// TODO Auto-generated method stub
		return null;
	}

	public PrologTerm prologFalse() {
		// TODO Auto-generated method stub
		return null;
	}

	public PrologTerm prologEmpty() {
		// TODO Auto-generated method stub
		return null;
	}

	public PrologEngine newEngine() {
		// TODO Auto-generated method stub
		return null;
	}

	public PrologAtom newAtom(String functor) {
		// TODO Auto-generated method stub
		return null;
	}

	public PrologFloat newFloat(Number value) {
		// TODO Auto-generated method stub
		return null;
	}

	public PrologDouble newDouble(Number value) {
		// TODO Auto-generated method stub
		return null;
	}

	public PrologInteger newInteger(Number value) {
		// TODO Auto-generated method stub
		return null;
	}

	public PrologLong newLong(Number value) {
		// TODO Auto-generated method stub
		return null;
	}

	public PrologVariable newVariable(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	public PrologVariable newVariable(String name, int position) {
		// TODO Auto-generated method stub
		return null;
	}

	public PrologList newList() {
		// TODO Auto-generated method stub
		return null;
	}

	public PrologList newList(PrologTerm[] arguments) {
		// TODO Auto-generated method stub
		return null;
	}

	public PrologList newList(PrologTerm head, PrologTerm tail) {
		// TODO Auto-generated method stub
		return null;
	}

	public PrologList newList(PrologTerm[] arguments, PrologTerm tail) {
		// TODO Auto-generated method stub
		return null;
	}

	public PrologStructure newStructure(String functor, PrologTerm... arguments) {
		// TODO Auto-generated method stub
		return null;
	}

	public PrologTerm newStructure(PrologTerm left, String operator, PrologTerm right) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}

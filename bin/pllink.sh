#!/bin/sh

if [ "$1" != "" ] ; then
  cd $1
fi

java -cp ../lib/commons-lang3-3.0.jar;../lib/prolobjectlink-jap-1.0.0.jar;../lib/prolobjectlink-jpi-1.0.0.jar;../lib/prolobjectlink-jpi-tuprolog-1.0.0.jar;../lib/tuprolog-3.1.jar: org.logicware.prolog.tuprolog.TuPrologConsole.java

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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.prolobjectlink.prolog.PrologFormatter;

/**
 * 
 * @author Jose Zalacain
 * @since 1.0
 */
class InterPrologFormatter extends Formatter implements PrologFormatter {

	private static final String ERROR = "ERROR";
	private static final String WARN = "WARN";
	private static final String INFO = "INFO";
	private static final String DEBUG = "DEBUG";
	private static final String TRACE = "TRACE";

	@Override
	public String format(LogRecord record) {
		StringBuilder buffer = new StringBuilder();

		// retrieve record data
		String levelString = "";
		Level level = record.getLevel();
		if (level.intValue() == Level.SEVERE.intValue()) {
			levelString = ERROR;
		} else if (level.intValue() == Level.WARNING.intValue()) {
			levelString = WARN;
		} else if (level.intValue() == Level.INFO.intValue()) {
			levelString = INFO;
		} else if (level.intValue() == Level.CONFIG.intValue()) {
			levelString = DEBUG;
		} else if (level.intValue() == Level.FINE.intValue()) {
			levelString = DEBUG;
		} else if (level.intValue() == Level.FINER.intValue()) {
			levelString = DEBUG;
		} else if (level.intValue() == Level.FINEST.intValue()) {
			levelString = TRACE;
		}

		long millisecs = record.getMillis();
		String name = record.getLoggerName();
		String message = record.getMessage();
		Throwable throwable = record.getThrown();
		SimpleDateFormat f = new SimpleDateFormat("MMM-dd-yyyy HH:mm");
		Date resultdate = new Date(millisecs);
		String date = f.format(resultdate);

		// create log line
		buffer.append('[');
		buffer.append(name);
		buffer.append(']');
		buffer.append(' ');
		buffer.append(levelString);
		buffer.append(' ');
		buffer.append(date);
		buffer.append(' ');
		buffer.append(message);
		if (throwable != null) {
			buffer.append('\n');
			buffer.append(throwable);
			buffer.append('\n');
			StackTraceElement[] s = throwable.getStackTrace();
			for (StackTraceElement stackTraceElement : s) {
				buffer.append(stackTraceElement);
				buffer.append('\n');
			}
		}
		buffer.append('\n');
		return "" + buffer + "";
	}

}

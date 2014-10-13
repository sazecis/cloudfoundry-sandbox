package hu.evosoft.logger;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.Date;

/**
 * Internal logger file. Sores the logs in the memory in a static variable.
 * 
 * @author Csaba.Szegedi
 *
 */
public class MyLogger {

	private static StringBuilder log = new StringBuilder();
	
	/**
	 * Log a new entry.
	 * 
	 * @param pattern a patter form java.text.MessageFormat
	 * @param params the parameters which will be used in the pattern.
	 */
	public static void appendLog(String pattern, Object... params) {
		log.append(new Timestamp(new Date().getTime()) + " ");
		log.append(MessageFormat.format(pattern, params));
		log.append("<br/>");
		
	}
	
	/**
	 * Log an exception
	 * 
	 * @param exMessage the message from the exception
	 * @param stackTraces the stack trace from the exception
	 */
	public static void appendLog(String exMessage, StackTraceElement[] stackTraces) {
		log.append(new Timestamp(new Date().getTime()) + " ");
		log.append(exMessage + " ");
		for (StackTraceElement stackTrace : stackTraces) {
			log.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
			log.append(stackTrace.toString());
			log.append("<br/>");		
		}
	}

	/**
	 * Return all logs.
	 * @return the logs as a string separated with line breaks.
	 */
	public static String getLogs() {
		return log.toString();
	}
	
	/**
	 * Delete all logs.
	 */
	public static void clearLogs() {
		log = new StringBuilder();
	}
	
}

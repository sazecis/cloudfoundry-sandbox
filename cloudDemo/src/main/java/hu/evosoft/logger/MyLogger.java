package hu.evosoft.logger;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.Date;

public class MyLogger {

	private static StringBuilder log = new StringBuilder();
	
	public static void appendLog(String pattern, Object... params) {
		log.append(new Timestamp(new Date().getTime()) + " ");
		log.append(MessageFormat.format(pattern, params));
		log.append("<br/>");
		
	}
	
	public static void appendLog(String line, StackTraceElement[] stackTraces) {
		log.append(new Timestamp(new Date().getTime()) + " ");
		log.append(line + " ");
		for (StackTraceElement stackTrace : stackTraces) {
			log.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
			log.append(stackTrace.toString());
			log.append("<br/>");		
		}
	}

	public static String getLogs() {
		return log.toString();
	}
	
	public static void clearLogs() {
		log = new StringBuilder();
	}
	
}

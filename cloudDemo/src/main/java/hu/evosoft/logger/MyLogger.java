package hu.evosoft.logger;

import java.sql.Timestamp;
import java.util.Date;

public class MyLogger {

	private static StringBuilder log = new StringBuilder();
	
	public static void appendLog(String... lines) {
		log.append(new Timestamp(new Date().getTime()) + " ");
		for (String line : lines) {
			log.append(line + " ");
		}
		log.append("<br/>");
	}
	
	public static String getLogs() {
		return log.toString();
	}
	
	public static void clearLogs() {
		log = new StringBuilder();
	}
	
}

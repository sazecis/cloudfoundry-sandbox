package hu.evosoft.parser;

import hu.evosoft.logger.MyLogger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetStatsParser {

	private static final short TIMESTAMP_INDEX = 0;
	private static final short DEST_HOST_INDEX = 4;
	private static final long TEN_MINUTES_IN_MILIS = 600000;
	private static final String SPLITTER = ";";
	
	
	public void formatFile(Path in, Path out) {
		try (BufferedWriter writer = Files.newBufferedWriter(out)) {
			writer.append(readFileContent(in));
		} catch (IOException x) {
			MyLogger.appendLog("formatFile ", x.toString());
			MyLogger.appendLog("formatFile ", x.getStackTrace());
			System.err.format("IOException: %s%n", x);
		}
	}
	
	public String readFileContent(Path path) {
		try (BufferedReader reader = Files.newBufferedReader(path, Charset.defaultCharset())) {
			return bufferedRead(reader);
		} catch (IOException x) {
			MyLogger.appendLog("formatFile ", x.toString());
			MyLogger.appendLog("formatFile ", x.getStackTrace());
			System.err.format("IOException: %s%n", x);
		}
		return null;
	}

	public String correctLine(String line) {
		return replaceUnwantedSemicolonsWithComa(line);
	}
	
	private String bufferedRead(BufferedReader reader) throws IOException{
		StringBuilder strBuilder = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			strBuilder.append(replaceUnwantedSemicolonsWithComa(line));
			strBuilder.append("\n");
		}
		return strBuilder.toString();		
	}
	
	private String replaceUnwantedSemicolonsWithComa(String line) {
		Matcher matcher = Pattern.compile("\\(([^\\)]+)").matcher(line);
		int pos = -1;
		while (matcher.find(pos + 1)) {
			pos = matcher.start();
			String match = matcher.group(0) + ")";
			if (match.contains(";")) {
				String replacer = match.replace(";", ",");
				line = line.replace(match, replacer);
			}
		}

		return line;
	}

	public static boolean isNetStatLog(String line) {
		return line.split(SPLITTER).length > 4 ? true : false; 
	}
	
	public static String getDestinationHost(String[] line) throws InvalidNetStatLineException {
		try {
			return line[DEST_HOST_INDEX];
		}
		catch (ArrayIndexOutOfBoundsException x) {
			throw new InvalidNetStatLineException(String.format("Cannot get DestinationHost from line \"%s\"", line.toString()));
		}
	}
	
	public static Long getTimeStamp(String[] line) throws InvalidNetStatLineException {
		String date = null;
		try {
			date = line[TIMESTAMP_INDEX];
			return convertToTenMinutesInterval(Timestamp.valueOf(date)).getTime();
		}
		catch (Exception x) {
			throw new InvalidNetStatLineException(String.format("Cannot get Timestamp from line \"%s\"", line.toString()));
		}
	}
	
	private static Timestamp convertToTenMinutesInterval(Timestamp timestamp) {
		return new Timestamp(timestamp.getTime() - timestamp.getTime() % TEN_MINUTES_IN_MILIS);
	}
	
	public static boolean isValidDateFormat(String date) {
		return Pattern.compile("(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3})").matcher(date).matches() ||
			   Pattern.compile("(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{1})").matcher(date).matches();
	}

	public static boolean isDateInMilisecond(String milis) {
		try {
			new Date(Long.parseLong(milis));
			return true;			
		}
		catch (Exception ex) {
			return false;
		}
	}

	public static String[] splitLine(String message) {		
		return message.split(SPLITTER);
	}
}

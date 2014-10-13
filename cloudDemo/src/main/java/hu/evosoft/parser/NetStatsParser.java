package hu.evosoft.parser;

import java.sql.Timestamp;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Tools to handle the raw statistical files
 * 
 * @author Csaba.Szegedi
 *
 */
public class NetStatsParser {

	private static final short TIMESTAMP_INDEX = 0;
	private static final short DEST_HOST_INDEX = 4;
	private static final long TEN_MINUTES_IN_MILIS = 600000;
	private static final String CURRENT_YEAR = "2014-01-01 00:00:00.0";
	private static final String SPLITTER = ";";
	
	/**
	 * Some lines in the raw files are not in correct format. In many cases the ";" separator is used for two different separations.
	 * These secondary separation are exchanged with ",". 
	 * @param line the incorrect line 
	 * @return the corrected line
	 */
	public String correctLine(String line) {
		return replaceUnwantedSemicolonsWithComa(line);
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

	/**
	 * Check if the provided line can be used as we expect, 
	 * i.e. can be split against ";" and the split string arrays length is more than 4.
	 * @param line the line which is checked
	 * @return true is the line can be used
	 */
	public static boolean isNetStatLog(String line) {
		return line.split(SPLITTER).length > 4 ? true : false; 
	}
	
	/**
	 * Get the destination host name from the provided line.
	 * @param line to be processed as an array
	 * @return the DestinationHost
	 * @throws InvalidNetStatLineException if the array length is less than 4 
	 */
	public static String getDestinationHost(String[] line) throws InvalidNetStatLineException {
		try {
			return line[DEST_HOST_INDEX];
		}
		catch (ArrayIndexOutOfBoundsException x) {
			throw new InvalidNetStatLineException(String.format("Cannot get DestinationHost from line \"%s\"", line.toString()));
		}
	}
	
	/**
	 * Get the timestamp when a web access was entered into the log.
	 * @param line the log line as an array
	 * @return the timestamp as a millisecond
	 * @throws InvalidNetStatLineException if the array length is less than 1
	 */
	public static Long getTimeStamp(String[] line) throws InvalidNetStatLineException {
		String date = null;
		try {
			date = line[TIMESTAMP_INDEX];
			long timeStamp = convertToTenMinutesInterval(Timestamp.valueOf(date)).getTime();
			if (isOldDate(timeStamp)) {
				throw new InvalidNetStatLineException();
			}
			return timeStamp;
		}
		catch (Exception x) {
			throw new InvalidNetStatLineException(String.format("Cannot get Timestamp from line \"%s\"", line.toString()));
		}
	}
	
	/**
	 * Check if the date is older than 2014
	 * @param timeStamp millisecond to be checked
	 * @return true if the provided millisecond is from before 2014
	 */
	public static boolean isOldDate(long timeStamp) {
		if (timeStamp < Timestamp.valueOf(CURRENT_YEAR).getTime()) {
			return true;
		}		
		return false;
	}
	
	private static Timestamp convertToTenMinutesInterval(Timestamp timestamp) {
		return new Timestamp(timestamp.getTime() - timestamp.getTime() % TEN_MINUTES_IN_MILIS);
	}
	
	/**
	 * Check if a provided string has the following format: yyyy-mm-dd hh:mm:ss.000
	 * @param date
	 * @return
	 */
	public static boolean isValidDateFormat(String date) {
		return Pattern.compile("(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3})").matcher(date).matches() ||
			   Pattern.compile("(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{1})").matcher(date).matches();
	}

	/**
	 * Checks if the provided string can be parsed as a data (it must be a long value)
	 * @param milis
	 * @return
	 */
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

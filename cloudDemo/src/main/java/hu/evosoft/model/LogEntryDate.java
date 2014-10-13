package hu.evosoft.model;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.UUID;

/**
 * 
 * MongoModel to handle the statistical entries according to their creation time.
 * Contains the timestamp and the count of the entries from that period.
 * 
 * @author Csaba.Szegedi
 *
 */
public class LogEntryDate extends AbstractMongoModel {
	
	/**
	 * generated UID 
	 */
	private static final long serialVersionUID = -6694410718921929863L;
	
	private Long timeStamp;

	/**
	 * Default ctr. generating default values.
	 */
	public LogEntryDate() {
		this.timeStamp = 0L;
	}
	
	/**
	 * Contsructor with a timestamp and value 
	 * 
	 * @param timeStamp
	 * @param value
	 */
	public LogEntryDate(Long timeStamp, int value) {
		this.timeStamp = timeStamp;
		setValue(value);
	}
	
	/**
	 * Timestamp as milliseconds
	 * @return
	 */
	public Long getTimeStamp() {
		return timeStamp;
	}
		
	/**
	 * Transforms the miliseconds to a readable form: e.g. "2014-10-13 18:43:25.125"
	 * 
	 * @return the formated timestamp
	 */
	public String getTimeStampAsString() {
		return new Timestamp(getTimeStamp()).toString();
	}
	
	/**
	 * Timestamp as milliseconds
	 * @param timeStamp
	 */
	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	@Override
	public String toString() {
		return String.format("{id: %s}, {timeStamp: %s}, {value: %s}", 
				getId(), getTimeStampAsString(), getValue());
	}
	
	@Override
	public int hashCode() {
		return timeStamp.hashCode();
	}
	
	@Override
	public boolean equals(Object logEntry) {
		return this.getTimeStamp().equals(((LogEntryDate) logEntry).getTimeStamp());
	}

	/**
	 * Ascending order using the timeStamp as comparator
	 */
	public static Comparator<LogEntryDate> MongoModelComparator = new Comparator<LogEntryDate>() {

		public int compare(LogEntryDate model1, LogEntryDate model2) {
			Long timeStamp1 = model1.getTimeStamp();
			Long timeStamp2 = model2.getTimeStamp();
			// ascending order
			return timeStamp1.compareTo(timeStamp2);
		}

	};

	/**
	 * After the MapReduce in the MongoDb the id will be filled with the content so to get back to the original state
	 * a new ID will be generated and the value from the ID will be moved back to the content.
	 */
	@Override
	public void moveIdToContent() {
		this.timeStamp = Long.parseLong(getId());
		setId(UUID.randomUUID().toString());		
	}
	
	/**
	 * The location of the mapper java script function which will be used at Mapping in MapReduce phase.
	 * 
	 * @return the location of the javaScrip file.
	 */
	@Override
	public String mapper() {
		return "classpath:js/mapLogEntryDate.js";
	}
	
}

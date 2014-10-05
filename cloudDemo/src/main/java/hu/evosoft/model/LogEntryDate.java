package hu.evosoft.model;

import hu.evosoft.logger.MyLogger;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.UUID;

public class LogEntryDate extends AbstractMongoModel {
	
	/**
	 * generated UID 
	 */
	private static final long serialVersionUID = -6694410718921929863L;
	
	private Long timeStamp;

	public LogEntryDate() {
		
	}
	
	public LogEntryDate(Long timeStamp, int value) {
		this.timeStamp = timeStamp;
		setValue(value);
	}
	
	public Long getTimeStamp() {
		return timeStamp;
	}
		
	public String getTimeStampAsString() {
		return new Timestamp(getTimeStamp()).toString();
	}
	
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
		if (timeStamp != null) {
			return timeStamp.hashCode();
		}
		return this.getClass().hashCode();
	}
	
	@Override
	public boolean equals(Object logEntry) {
		return this.getTimeStamp().equals(((LogEntryDate) logEntry).getTimeStamp());
	}
	
	public static Comparator<LogEntryDate> MongoModelComparator = new Comparator<LogEntryDate>() {

		public int compare(LogEntryDate model1, LogEntryDate model2) {

			Long timeStamp1 = model1.getTimeStamp();
			Long timeStamp2 = model2.getTimeStamp();

			// ascending order
			return timeStamp1.compareTo(timeStamp2);

		}

	};

	@Override
	public void exchangeInnerItems() {
		MyLogger.appendLog("LogEntryDate.exchangeInnerItems ", getId());
		this.timeStamp = Long.parseLong(getId());
		setId(UUID.randomUUID().toString());		
	}
	
	@Override
	public String mapper() {
		return "classpath:js/mapLogEntryDate.js";
	}
	
}

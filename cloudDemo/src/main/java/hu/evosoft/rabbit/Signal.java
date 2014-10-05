package hu.evosoft.rabbit;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class Signal implements Serializable {

	/**
	 * generated ID 
	 */
	private static final long serialVersionUID = 5217599240436916382L;
	
	private Timestamp timeStamp;
	private SignalType type;
	
	public Signal(SignalType type) {
		this.timeStamp = new Timestamp(new Date().getTime());
		this.type = type;
	}
	
	public Timestamp getTimeStamp() {
		return timeStamp;
	}

	public SignalType getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return String.format("[%s]: %s", timeStamp, type);
	}
}

package hu.evosoft.rabbit;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Used when signaling the beginning and the end of the net statistic file. 
 * @author Csaba.Szegedi
 *
 */
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
	
	/**
	 * When the event occured.
	 * @return
	 */
	public Timestamp getTimeStamp() {
		return timeStamp;
	}

	/**
	 * The type of the event: BEGIN or END or CHUNK
	 * @return
	 */
	public SignalType getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return String.format("[%s]: %s", timeStamp, type);
	}
}

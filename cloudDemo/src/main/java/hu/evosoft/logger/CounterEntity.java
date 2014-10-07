package hu.evosoft.logger;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class CounterEntity implements Serializable{
	
	/**
	 * Generated UID 
	 */
	private static final long serialVersionUID = -576656026123490465L;
	
	private CounterId id;
	private long value;

	public CounterEntity() {
		
	}
	
	public CounterEntity(CounterId id, long value) {
		this.id = id;
		this.value = value;
	}
	
	public CounterId getId() {
		return id;
	}

	public void setId(CounterId id) {
		this.id = id;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

}

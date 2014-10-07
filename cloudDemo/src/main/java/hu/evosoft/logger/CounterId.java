package hu.evosoft.logger;

import java.io.Serializable;

public class CounterId implements Serializable {

	/**
	 * Generated UID 
	 */
	private static final long serialVersionUID = 7583525793902299004L;
	
	private CounterCategory category;
	private CounterType counerType;
	private String id;

	public CounterId(CounterCategory category, CounterType type, String id) {
		this.category = category;
		this.counerType = type;
		this.id = id;
	}

	public CounterCategory getCategory() {
		return category;
	}

	public CounterType getType() {
		return counerType;
	}

	public String getId() {
		return id;
	}

}

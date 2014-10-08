package hu.evosoft.logger;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class CounterEntity implements Serializable{
	
	/**
	 * Generated UID 
	 */
	private static final long serialVersionUID = -576656026123490465L;
	
	@Id
	private String id = UUID.randomUUID().toString(); 
	private CounterCategory category;
	private CounterType counterType;
	private String name;
	private long value;

	public CounterEntity() {
		
	}
	
	public CounterEntity(CounterCategory category, CounterType counterType, String name, long value) {
		this.setCategory(category);
		this.setCounterType(counterType);
		this.setName(name);
		this.value = value;
	}
	
	public String getId() {
		return id;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

	public CounterCategory getCategory() {
		return category;
	}

	public void setCategory(CounterCategory category) {
		this.category = category;
	}

	public CounterType getCounterType() {
		return counterType;
	}

	public void setCounterType(CounterType counterType) {
		this.counterType = counterType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return MessageFormat.format("{0} | {1} | {2} | {3} | {4}", id, name, category, counterType, Long.toString(value));
	}
}

package hu.evosoft.model;

public class DestinationHost extends AbstractMongoModel {
	
	/**
	 * Generated UID
	 */
	private static final long serialVersionUID = 1679793298745891714L;
	
	private String name;
	private int value;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	
	public int increaseCount()
	{
		return value++;
	}
	
	@Override
	public String toString() {
		return String.format("{[name: %s][value: %s]}", name, value);
	}
	
}

package hu.evosoft.model;

public class DestinationHost extends AbstractMongoModel {
	
	/**
	 * Generated UID
	 */
	private static final long serialVersionUID = 1679793298745891714L;
	
	private String name;
	private int count = 1;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	public int increaseCount()
	{
		return count++;
	}
	
	@Override
	public String toString() {
		return String.format("{[name: %s][count: %s]}", name, count);
	}
	
}

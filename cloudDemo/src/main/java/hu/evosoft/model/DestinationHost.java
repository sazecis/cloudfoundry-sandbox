package hu.evosoft.model;

import java.io.Serializable;

public class DestinationHost implements Serializable {

	/**
	 * Generated version UID
	 */
	private static final long serialVersionUID = 3470990918034540477L;
	
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

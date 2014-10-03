package hu.evosoft.model;

public class DestinationHost extends AbstractMongoModel {
	
	/**
	 * Generated UID
	 */
	private static final long serialVersionUID = 1679793298745891714L;
	
	private String name;

	public DestinationHost() {
		
	}
	
	public DestinationHost(String name, int value) {
		this.name = name;
		setValue(value);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return String.format("{id: %s}, {name: %s}, {value: %s}", 
				getId(), getName(), getValue());
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	@Override
	public boolean equals(Object destHost) {
		return this.getName().equals(((DestinationHost) destHost).getName());
	}
	
}

package hu.evosoft.model;

import java.util.Comparator;
import java.util.UUID;

public class DestinationHost extends AbstractMongoModel {
	
	/**
	 * Generated UID
	 */
	private static final long serialVersionUID = 1679793298745891714L;
	
	private String name;

	public DestinationHost() {
		this.name = "";
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
	
	/**
	 * Ascending order using the name as comparator
	 */
	public static Comparator<DestinationHost> MongoModelComparator = new Comparator<DestinationHost>() {

		public int compare(DestinationHost model1, DestinationHost model2) {
			String name1 = model1.getName().toUpperCase();
			String name2 = model2.getName().toUpperCase();
			// ascending order
			return name1.compareTo(name2);
		}

	};

	@Override
	public void moveIdToContent() {
		this.name = getId();
		setId(UUID.randomUUID().toString());
	}

	@Override
	public String mapper() {
		return "classpath:js/mapDestinationHost.js";
	}

}

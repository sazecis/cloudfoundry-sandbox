package hu.evosoft.model;

import java.util.Comparator;
import java.util.UUID;

/**
 * 
 * MongoModel to handle the statistical entries according to their destination host (domain).
 * Contains the destination host name and the count of the entries from that domain.
 * 
 * @author Csaba.Szegedi
 *
 */
public class DestinationHost extends AbstractMongoModel {
	
	/**
	 * Generated UID
	 */
	private static final long serialVersionUID = 1679793298745891714L;
	
	private String name;

	/**
	 * Default ctr.
	 */
	public DestinationHost() {
		this.name = "";
	}
	
	/**
	 * Constructor with destination host name the the count of accesses
	 * @param name
	 * @param value
	 */
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

	/**
	 * After the MapReduce in the MongoDb the id will be filled with the content so to get back to the original state
	 * a new ID will be generated and the value from the ID will be moved back to the content.
	 */
	@Override
	public void moveIdToContent() {
		this.name = getId();
		setId(UUID.randomUUID().toString());
	}

	/**
	 * The location of the mapper java script function which will be used at Mapping in MapReduce phase.
	 * 
	 * @return the location of the javaScrip file.
	 */
	@Override
	public String mapper() {
		return "classpath:js/mapDestinationHost.js";
	}

}

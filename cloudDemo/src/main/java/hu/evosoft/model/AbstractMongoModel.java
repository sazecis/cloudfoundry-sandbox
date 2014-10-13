package hu.evosoft.model;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Abstract class containing the main implementations of IMongoModel interface. All models which will be harvested have to extend this class. 
 * 
 * @author Csaba.Szegedi
 *
 */
@Document
public abstract class AbstractMongoModel implements Serializable,
		Comparable<AbstractMongoModel>, IMongoModel {

	/**
	 * Generated UID
	 */
	private static final long serialVersionUID = 4120933676365616922L;

	@Id
	private String id = UUID.randomUUID().toString();
	private int value = 1;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * By default the list will be sorted decreasingly.
	 * The value is used for comparison.
	 */
	public int compareTo(AbstractMongoModel model) {
		int compareValue = ((AbstractMongoModel) model).getValue();
		return compareValue - this.value;
	}
	
	/**
	 * The name of the collection in which the document is stored in the MongoDb
	 */
	@Override
	public String collectionName() {
		return this.getClass().getSimpleName();
	}
	

}

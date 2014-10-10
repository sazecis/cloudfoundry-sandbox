package hu.evosoft.model;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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

	public int compareTo(AbstractMongoModel model) {
		int compareValue = ((AbstractMongoModel) model).getValue();
		return compareValue - this.value;
	}
	
	@Override
	public String collectionName() {
		return this.getClass().getSimpleName();
	}
	

}

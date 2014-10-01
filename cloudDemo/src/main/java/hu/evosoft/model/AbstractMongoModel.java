package hu.evosoft.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public abstract class AbstractMongoModel  implements Serializable, IMongoModel{

	/**
	 * Generated UID
	 */
	private static final long serialVersionUID = 4120933676365616922L;
	
	@Id
	private String id;	
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
}

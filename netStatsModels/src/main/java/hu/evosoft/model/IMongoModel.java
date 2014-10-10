package hu.evosoft.model;

public interface IMongoModel {

	String getId();
	
	void setId(String id);
	
	int getValue();
	
	void setValue(int value);
	
	String collectionName();
	
	void moveIdToContent();
	
	String mapper();

}

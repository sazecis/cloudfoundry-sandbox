package hu.evosoft.model;

/**
 * 
 * Interface for Models which are handled by the system. All Models which are to be mined have to implement this Interface.
 * 
 * @author Csaba.Szegedi
 *
 */
public interface IMongoModel {

	/**
	 * Id for the models. This id will be used in the MongoDB as the identifier.
	 * 
	 * @return the id
	 */
	String getId();

	/**
	 * Id for the models. This id will be used in the MongoDB as the identifier.
	 * 
	 * @param id the id to be set
	 */
	void setId(String id);
	
	/**
	 * Each MongoModel will have a value containing the counted data.
	 * 
	 * @return the value for the current Model
	 */
	int getValue();
	
	/**
	 * Each MongoModel will have a value containing the counted data.
	 * 
	 * @param value the value to be set
	 */
	void setValue(int value);
	
	/**
	 * The name of the name of the collection where the Model will be stored in the MongoDb.
	 * 
	 * @return the name of the MongoDb collection
	 */
	String collectionName();
	
	/**
	 * After the MapReduce in the MongoDb the id will be filled with the content so to get back to the original state
	 * a new ID will be generated and the value from the ID will be moved back to the content.
	 */
	void moveIdToContent();
	
	/**
	 * The location of the mapper java script function which will be used at Mapping in MapReduce phase.
	 * 
	 * @return the location of the javaScrip file.
	 */
	String mapper();

}

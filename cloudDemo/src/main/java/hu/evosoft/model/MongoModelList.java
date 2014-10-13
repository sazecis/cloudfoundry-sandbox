package hu.evosoft.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Collection of all available IMongoModel. All the models have to be registered in this "static" class.
 * 
 * @author Csaba.Szegedi
 *
 */
public class MongoModelList {

	private static List<IMongoModel> myModelSet = new ArrayList<>();
		
	/**
	 * list of the registered Models
	 */
	static
	{
		myModelSet.add(new DestinationHost());
		myModelSet.add(new LogEntryDate());
	}
	
	private MongoModelList() {		
	}
	
	/**
	 * The list of the registered Modes
	 * @return List of IMongoModels
	 */
	public static List<IMongoModel> getModelSet() {
		return myModelSet;
	}
	
}

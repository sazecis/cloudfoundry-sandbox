package hu.evosoft.model;

import java.util.ArrayList;
import java.util.List;

public class MongoModelList {

	private static List<IMongoModel> myModelSet = new ArrayList<>();
		
	static
	{
		myModelSet.add(new DestinationHost());
		myModelSet.add(new LogEntryDate());
	}
	
	private MongoModelList() {		
	}
	
	public static List<IMongoModel> getModelSet() {
		return myModelSet;
	}
	
}

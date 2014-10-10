package hu.evosoft.model;

import java.util.HashSet;
import java.util.Set;

public class MongoModelList {

	private static Set<IMongoModel> myModelSet = new HashSet<>();
		
	static
	{
		//myModelList.add(new Data());
		myModelSet.add(new DestinationHost());
		myModelSet.add(new LogEntryDate());
	}
	
	private MongoModelList() {		
	}
	
	public static Set<IMongoModel> getModelSet() {
		return myModelSet;
	}
	
}

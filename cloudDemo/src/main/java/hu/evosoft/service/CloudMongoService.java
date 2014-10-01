package hu.evosoft.service;

import hu.evosoft.model.Data;
import hu.evosoft.model.DestinationHost;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
 
@Repository
public class CloudMongoService {
     
    @Autowired 
    private MongoTemplate mongoTemplate;
     
    public static final String SIMPLE_COLLECTION_NAME = "data";
    public static final String DEST_HOST_COLLECTION_NAME = "destHost";
     
    public void addData(Data data) {
        if (!mongoTemplate.collectionExists(Data.class)) {
            mongoTemplate.createCollection(Data.class);
        }       
        data.setId(UUID.randomUUID().toString());
        mongoTemplate.insert(data, SIMPLE_COLLECTION_NAME);
    }
     
    public List<Data> listData() {
        return mongoTemplate.findAll(Data.class, SIMPLE_COLLECTION_NAME);
    }
     
    public void deleteData(Data data) {
        mongoTemplate.remove(data, SIMPLE_COLLECTION_NAME);
    }
     
    public void updateData(Data data) {
        mongoTemplate.insert(data, SIMPLE_COLLECTION_NAME);      
    }

	public void clearData() {
		mongoTemplate.dropCollection(SIMPLE_COLLECTION_NAME);
	}
	
	public void addAndAggregateDestHosts(DestinationHost destHost) {
		// TODO
		//mongoTemplate.mapReduce(inputCollectionName, mapFunction, reduceFunction, entityClass)
	}
	
}

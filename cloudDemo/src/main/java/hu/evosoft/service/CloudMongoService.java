package hu.evosoft.service;

import hu.evosoft.logger.MyLogger;
import hu.evosoft.model.Data;
import hu.evosoft.model.DestinationHost;
import hu.evosoft.model.IMongoModel;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapreduce.MapReduceOptions;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.stereotype.Repository;
 
@Repository
public class CloudMongoService {
     
    @Autowired 
    private MongoTemplate mongoTemplate;
     
    public static final String SIMPLE_COLLECTION_NAME = "data";
    public static final String DEST_HOST_COLLECTION_NAME = "destHost";
     
    public void addDocument(IMongoModel data) {
    	String collectionName = null;
    	MyLogger.appendLog(CloudMongoService.class.getSimpleName(), data.toString());
    	if (data instanceof Data) {
        	MyLogger.appendLog(CloudMongoService.class.getSimpleName(), "Data instance");
    		if (!mongoTemplate.collectionExists(Data.class)) {
	            mongoTemplate.createCollection(Data.class);
    		}
            collectionName = SIMPLE_COLLECTION_NAME;
        } 
    	else if (data instanceof DestinationHost) { 
        	MyLogger.appendLog(CloudMongoService.class.getSimpleName(), "DestinationHost instance");
    		if (!mongoTemplate.collectionExists(DestinationHost.class)) {
	            mongoTemplate.createCollection(DestinationHost.class);
    		}
            collectionName = DEST_HOST_COLLECTION_NAME;
        }       
        data.setId(UUID.randomUUID().toString());
    	MyLogger.appendLog(CloudMongoService.class.getSimpleName(), collectionName);
        if (collectionName != null) {
        	mongoTemplate.insert(data, collectionName);
        }
    }
     
    public List<Data> listData() {
        return mongoTemplate.findAll(Data.class, SIMPLE_COLLECTION_NAME);
    }

    public List<DestinationHost> listDestinationHosts() {
        return mongoTemplate.findAll(DestinationHost.class, DEST_HOST_COLLECTION_NAME);
    }

    public void deleteDocument(Data data) {
        mongoTemplate.remove(data, SIMPLE_COLLECTION_NAME);
    }
    
    public void deleteDocument(DestinationHost destHost) {
        mongoTemplate.remove(destHost, DEST_HOST_COLLECTION_NAME);
    }

    public void updateDocument(Data data) {
        mongoTemplate.insert(data, SIMPLE_COLLECTION_NAME);      
    }

	public void clearAllDocuments() {
		mongoTemplate.dropCollection(SIMPLE_COLLECTION_NAME);
		mongoTemplate.dropCollection(DEST_HOST_COLLECTION_NAME);
	}
	
	public void mapReduceDestinationHost() {
		/*MapReduceOptions options = MapReduceOptions.options();
		options.outputCollection(DEST_HOST_COLLECTION_NAME);*/
		MapReduceResults<DestinationHost> results = 
				mongoTemplate.mapReduce(DEST_HOST_COLLECTION_NAME, "classpath:js/map.js", "classpath:js/reduce.js", DestinationHost.class);
		mongoTemplate.dropCollection(DEST_HOST_COLLECTION_NAME);
		for (DestinationHost dest : results) {
			mongoTemplate.insert(dest, DEST_HOST_COLLECTION_NAME);			
		}
	}
	
}

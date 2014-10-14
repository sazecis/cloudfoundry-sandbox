package hu.evosoft.service;

import hu.evosoft.logger.CounterEntity;
import hu.evosoft.logger.MyLogger;
import hu.evosoft.model.IMongoModel;
import hu.evosoft.model.MongoModelList;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.stereotype.Repository;

/**
 * Handles the calls from the controller to the MongoDB.
 * 
 * @author Csaba.Szegedi
 *
 */
@Repository
public class CloudMongoService {

	/**
	 * Auto wired via dispatcher bean to org.springframework.data.mongodb.core.MongoTemplate. 
	 */
	@Autowired
	private MongoTemplate mongoTemplate;

	/**
	 * Default ctr.
	 */
	public CloudMongoService() {
		
	}

	/**
	 * Constructor with initialize the MongoTemplate. Needed for unit testing.
	 * 
	 * @param template
	 */
	public CloudMongoService(MongoTemplate template) {
		this.mongoTemplate = template;
	}

	/**
	 * Add the provided IMongoModel document to the MongoDB. 
	 * The collection name in the MongoDB will be the document.collectionName.
	 * 
	 * @param document the document which will be inserted into the MongoDB.
	 */
	public void addDocument(IMongoModel document) {
		MyLogger.appendLog(CloudMongoService.class.getSimpleName(),
				document.toString(), document.getClass().getSimpleName(),
				document.collectionName());
		if (!mongoTemplate.collectionExists(document.collectionName())) {
			mongoTemplate.createCollection(document.collectionName());
		}
		document.setId(UUID.randomUUID().toString());
		mongoTemplate.insert(document, document.collectionName());
	}

	public void addCollection(List<IMongoModel> list, String collectionName) {
		if (!mongoTemplate.collectionExists(collectionName)) {
			mongoTemplate.createCollection(collectionName);
		}
		mongoTemplate.insert(list, collectionName);
	}
	
	/**
	 * Insert a new CounterEntity (used for performance measurements) into the MongoDB.
	 * 
	 * @param entity the measured counter.
	 */
	public void addCounter(CounterEntity entity) {
		if (!mongoTemplate.collectionExists(entity.getClass())) {
			mongoTemplate.createCollection(entity.getClass());
		}
		mongoTemplate.insert(entity);	
	}
	
	/**
	 * Delete a document entry from the MongoDB.
	 * 
	 * @param document the document to be deleted.
	 */
	public void deleteDocument(IMongoModel document) {
		mongoTemplate.remove(document, document.collectionName());
	}

	/**
	 * Get the list of the documents with the given entity type.
	 * 
	 * @param type the class of the document. E.g. DestinationHost.class
	 * @return the list of the documents with the given type
	 */
	public <T> List<T> listDocuments(Class<T> type) {
		return mongoTemplate.findAll(type, type.getSimpleName());
	}

	/**
	 * Get the list of the performance counters.
	 * 
	 * @return the list of the measured counters.
	 */
	public List<CounterEntity> listPerformanceCounters() {
		return mongoTemplate.findAll(CounterEntity.class);
	}
	
	/**
	 * Remove from the MongoDB the collection with the given entity type. 
	 * 
	 * @param type e.g. DestinationHost.class
	 */
	public <T> void clearDocuments(Class<T> type) {
		mongoTemplate.dropCollection(type);
	}

	/**
	 * Remove from the MongoDB all the documents which are registered to the MongoModelList
	 */
	public void clearAllDocuments() {
		for (IMongoModel type : MongoModelList.getModelSet()) {
			mongoTemplate.dropCollection(type.collectionName());
		}
	}

	/**
	 * Does a MapReduce procedure on the MongoDB for the given document type. 
	 * 
	 * @param type the document type which will be used at the procedure, e.g. DestinationHost.class
	 * @param mapper the location of the mapper javascript, e.g. "classpath:mapDestinationHost.js"
	 */
	public <T extends IMongoModel> void mapReduce(Class<T> type, String mapper) {
		MyLogger.appendLog("Collection exists?", type.getSimpleName(), Boolean.toString(mongoTemplate.collectionExists(type)));
		// the result of the MapReduce will be stored in the MapReduceResults
		MapReduceResults<T> mapReduceResults = mongoTemplate.mapReduce(
				type.getSimpleName(), mapper,
				"classpath:js/reduce.js", type);
		// we need to drop the collection which was used for MapResult
		mongoTemplate.dropCollection(type.getSimpleName());
		MyLogger.appendLog("Collection exists after drop too?", type.getSimpleName(), 
				Boolean.toString(mongoTemplate.collectionExists(type)));	
		// enter the MapReduced list back to the MongoDB
		for (IMongoModel result : mapReduceResults) {
			result.moveIdToContent();
			mongoTemplate.insert(result, result.collectionName());
		}
	}

}

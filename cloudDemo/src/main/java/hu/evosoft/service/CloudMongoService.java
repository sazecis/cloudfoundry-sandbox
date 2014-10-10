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

@Repository
public class CloudMongoService {

	@Autowired
	private MongoTemplate mongoTemplate;

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

	public void addCounter(CounterEntity entity) {
		if (!mongoTemplate.collectionExists(entity.getClass())) {
			mongoTemplate.createCollection(entity.getClass());
		}
		mongoTemplate.insert(entity);	
	}
	
	public void deleteDocument(IMongoModel document) {
		mongoTemplate.remove(document, document.collectionName());
	}

	public void updateDocument(IMongoModel document) {
		mongoTemplate.insert(document, document.collectionName());
	}

	public <T> List<T> listDocuments(Class<T> type) {
		return mongoTemplate.findAll(type, type.getSimpleName());
	}

	public List<CounterEntity> listPerformanceCounters() {
		return mongoTemplate.findAll(CounterEntity.class);
	}
	
	public <T> void clearDocuments(Class<T> type) {
		mongoTemplate.dropCollection(type);
	}

	public void clearAllDocuments() {
		for (IMongoModel type : MongoModelList.getModelSet()) {
			mongoTemplate.dropCollection(type.collectionName());
		}
	}

	public <T> void mapReduce(Class<T> type, String mapper) {
		MyLogger.appendLog("Collection exists?", type.getSimpleName(), Boolean.toString(mongoTemplate.collectionExists(type)));		
		MapReduceResults<T> results = mongoTemplate.mapReduce(
				type.getSimpleName(), mapper,
				"classpath:js/reduce.js", type);
		mongoTemplate.dropCollection(type.getSimpleName());
		MyLogger.appendLog("Collection exists after drop too?", type.getSimpleName(), 
				Boolean.toString(mongoTemplate.collectionExists(type)));		
		for (T result : results) {
			IMongoModel aggregated = (IMongoModel) result;
			aggregated.moveIdToContent();
			MyLogger.appendLog("mongoTemplate.insert", aggregated.toString(), type.getSimpleName());
			mongoTemplate.insert(aggregated, aggregated.collectionName());
		}
	}

}

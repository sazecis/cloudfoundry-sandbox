package hu.evosoft.service;

import hu.evosoft.logger.CounterCategory;
import hu.evosoft.logger.CounterType;
import hu.evosoft.logger.MyLogger;
import hu.evosoft.model.IMongoModel;
import hu.evosoft.model.MongoModelList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class RedisMongoTransferrer {
   
	@Autowired
	@Qualifier("cloudMongoService")
	private CloudMongoService mongoService;
	@Autowired
	@Qualifier("cloudRedisService")
	private CloudRedisService redisService;
	@Autowired
	private PerformanceCounterService performanceCounterService;

    public void transferAll() {
    	performanceCounterService.addNewCounterEntry(
    			CounterCategory.MONGO_ADD, CounterType.START, 
    			this.getClass().getSimpleName(), 
    			System.currentTimeMillis());
    	for (IMongoModel model : redisService.popAllMongoCompatibleValues()) {
    		MyLogger.appendLog("transfer ", RedisMongoTransferrer.class.getSimpleName(), model.toString());
    		mongoService.addDocument(model);
    	}
    	performanceCounterService.addNewCounterEntry(
    			CounterCategory.MONGO_ADD, CounterType.END, 
    			this.getClass().getSimpleName(), 
    			System.currentTimeMillis());
    	MyLogger.appendLog("Size of MongoModelList", Integer.toString(MongoModelList.getModelSet().size()));
    	performanceCounterService.addNewCounterEntry(
    			CounterCategory.MONGO_MR, CounterType.START, 
    			this.getClass().getSimpleName(), 
				System.currentTimeMillis());
    	for (IMongoModel model : MongoModelList.getModelSet())
    	{
    		MyLogger.appendLog("transferAll ", model.getClass().getSimpleName());
    		mongoService.mapReduce(model.getClass(), model.mapper());
    	}
    	performanceCounterService.addNewCounterEntry(
    			CounterCategory.MONGO_MR, CounterType.END, 
    			this.getClass().getSimpleName(), 
				System.currentTimeMillis());
    }
    
}

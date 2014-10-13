package hu.evosoft.service;

import hu.evosoft.logger.CounterCategory;
import hu.evosoft.logger.CounterType;
import hu.evosoft.logger.MyLogger;
import hu.evosoft.model.DestinationHost;
import hu.evosoft.model.IMongoModel;
import hu.evosoft.model.LogEntryDate;
import hu.evosoft.model.MongoModelList;
import hu.evosoft.parser.NetStatsParser;

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
    		if (model instanceof DestinationHost && 
    				NetStatsParser.isValidDateFormat(((DestinationHost) model).getName())) {
    			continue;
    		}
    		if (model instanceof LogEntryDate && 
    				NetStatsParser.isOldDate(((LogEntryDate) model).getTimeStamp().longValue())) {
    			continue;
    		}
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
    		mongoService.mapReduce(model.getClass(), model.mapper());
    	}
    	performanceCounterService.addNewCounterEntry(
    			CounterCategory.MONGO_MR, CounterType.END, 
    			this.getClass().getSimpleName(), 
				System.currentTimeMillis());
    }
    
}

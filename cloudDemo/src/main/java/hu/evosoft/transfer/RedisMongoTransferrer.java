package hu.evosoft.transfer;

import hu.evosoft.logger.MyLogger;
import hu.evosoft.model.IMongoModel;
import hu.evosoft.model.MongoModelList;
import hu.evosoft.service.CloudMongoService;
import hu.evosoft.service.CloudRedisService;

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
	
    public void transferData() {
    	mongoService.addDocument(redisService.getData());
    }
    
    public void transferAll() {
    	for (IMongoModel model : redisService.popAllMongoCompatibleValues()) {
    		MyLogger.appendLog("transfer ", RedisMongoTransferrer.class.getSimpleName(), model.toString());
    		mongoService.addDocument(model);
    	}
    	MyLogger.appendLog("Size of MongoModelList", Integer.toString(MongoModelList.getModelSet().size()));
    	for (IMongoModel model : MongoModelList.getModelSet())
    	{
    		MyLogger.appendLog("transferAll ", model.getClass().getSimpleName());
    		mongoService.mapReduce(model.getClass(), model.mapper());
    	}
    }
    
}

package hu.evosoft.transfer;

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
	
    public void transferData()
    {
    	mongoService.addData(redisService.getData());
    }
}

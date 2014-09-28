package hu.evosoft.transfer;

import hu.evosoft.service.PersonMongoService;
import hu.evosoft.service.PersonRedisService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class RedisMongoTransferrer {
   
	@Autowired
	@Qualifier("personMongoService")
	private PersonMongoService mongoService;
	@Autowired
	@Qualifier("personRedisService")
	private PersonRedisService redisService;
	
    public void transferPerson()
    {
    	mongoService.addPerson(redisService.getPerson());
    }
}

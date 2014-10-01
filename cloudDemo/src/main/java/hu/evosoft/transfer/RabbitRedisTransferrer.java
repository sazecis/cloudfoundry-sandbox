package hu.evosoft.transfer;

import hu.evosoft.service.CloudRabbitService;
import hu.evosoft.service.CloudRedisService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class RabbitRedisTransferrer {

	@Autowired
	@Qualifier("cloudRabbitService")
	private CloudRabbitService rabbitService;
	@Autowired
	@Qualifier("cloudRedisService")
	private CloudRedisService redisService;
	
    public void transferData()
    {
    	redisService.addData(rabbitService.retrieveOneData().getData());
    }
	
}

package hu.evosoft.transfer;

import hu.evosoft.service.PersonRabbitService;
import hu.evosoft.service.PersonRedisService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class RabbitRedisTransferrer {

	@Autowired
	@Qualifier("personRabbitService")
	private PersonRabbitService rabbitService;
	@Autowired
	@Qualifier("personRedisService")
	private PersonRedisService redisService;
	
    public void transferPerson()
    {
    	redisService.addPerson(rabbitService.retrieveOnePerson().getName());
    }
	
}

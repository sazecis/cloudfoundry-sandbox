package hu.evosoft.service;

import hu.evosoft.model.Data;
import hu.evosoft.model.Signal;
import hu.evosoft.model.SignalType;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class CloudRabbitService {

	private static final String QUEUE_NAME = "cloudDemo"; 

	@Autowired 
    private RabbitTemplate rabbitTemplate;
	@Autowired
	@Qualifier("cloudRedisService")
	private CloudRedisService redisService;
	
    public void queueMessage(String name) {
        rabbitTemplate.convertAndSend(QUEUE_NAME, name);
    }
	
    public Data retrieveOneData() {
    	return new Data((String) rabbitTemplate.receiveAndConvert(QUEUE_NAME));    		
    }

	public void SendBeginSignal() {
        rabbitTemplate.convertAndSend(QUEUE_NAME, new Signal(SignalType.BEGIN));
	}

	public void SendEndSignal() {
        rabbitTemplate.convertAndSend(QUEUE_NAME, new Signal(SignalType.END));		
	}
       
}

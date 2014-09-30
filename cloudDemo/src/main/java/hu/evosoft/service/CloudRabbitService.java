package hu.evosoft.service;

import hu.evosoft.model.Data;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CloudRabbitService {

	private static final String QUEUE_NAME = "cloudDemo"; 
	
    @Autowired 
    private RabbitTemplate rabbitTemplate;
       
    public void queueMessage(String name) {
    	rabbitTemplate.setQueue(QUEUE_NAME);
        rabbitTemplate.convertAndSend(QUEUE_NAME, name);
    }
	
    public Data retrieveOneData() {
    	return new Data((String) rabbitTemplate.receiveAndConvert(QUEUE_NAME));    		
    }
    
}

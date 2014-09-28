package hu.evosoft.service;

import hu.evosoft.model.Person;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PersonRabbitService {

	private static final String QUEUE_NAME = "sazecis"; 
	
    @Autowired 
    private RabbitTemplate rabbitTemplate;
       
    public void queuePerson(String name) {
    	rabbitTemplate.setQueue(QUEUE_NAME);
        rabbitTemplate.convertAndSend(QUEUE_NAME, name);
    }
	
    public Person retrieveOnePerson() {
    	return new Person((String) rabbitTemplate.receiveAndConvert(QUEUE_NAME));    		
    }
    
}

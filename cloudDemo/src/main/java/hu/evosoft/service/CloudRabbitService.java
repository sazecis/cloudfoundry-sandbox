package hu.evosoft.service;

import hu.evosoft.rabbit.Signal;
import hu.evosoft.rabbit.SignalType;

import org.springframework.amqp.core.AmqpAdmin;
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
	@Qualifier("rabbitAdmin")
	private AmqpAdmin rabbitAdmin;
	@Autowired
	@Qualifier("cloudRedisService")
	private CloudRedisService redisService;
	
    public void queueMessage(String name) {
        rabbitTemplate.convertAndSend(QUEUE_NAME, name);
    }
	
	public void sendBeginSignal() {
        rabbitTemplate.convertAndSend(QUEUE_NAME, new Signal(SignalType.BEGIN));
	}

	public void sendEndSignal() {
        rabbitTemplate.convertAndSend(QUEUE_NAME, new Signal(SignalType.END));		
	}
	
	public void sendChunkEndSignal() {
        rabbitTemplate.convertAndSend(QUEUE_NAME, new Signal(SignalType.CHUNK));		
	}

	public void purgeQueue()
	{
		rabbitAdmin.purgeQueue(QUEUE_NAME, false);
	}
       
}

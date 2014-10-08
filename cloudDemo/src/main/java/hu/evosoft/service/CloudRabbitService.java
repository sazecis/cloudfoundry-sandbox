package hu.evosoft.service;

import hu.evosoft.logger.CounterEntity;
import hu.evosoft.rabbit.Signal;
import hu.evosoft.rabbit.SignalType;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class CloudRabbitService {

	private static final String DEFAULT_QUEUE_NAME = "cloudDemo"; 

	@Autowired 
    private RabbitTemplate rabbitTemplate;
	@Autowired
	@Qualifier("rabbitPerfTemplate")
    private RabbitTemplate rabbitPerfTemplate;
	@Autowired
	@Qualifier("rabbitAdmin")
	private AmqpAdmin rabbitAdmin;
	@Autowired
	@Qualifier("cloudRedisService")
	private CloudRedisService redisService;
	
    public void queueMessage(String message) {
    	queueMessage(DEFAULT_QUEUE_NAME, message);
    }

    public void queueMessage(String queueName, String message) {
        rabbitTemplate.convertAndSend(queueName, message);
    }

    public void queueMessage(String queueName, CounterEntity message) {
    	rabbitPerfTemplate.convertAndSend(queueName, message);
    }

	public void sendBeginSignal() {
        rabbitTemplate.convertAndSend(DEFAULT_QUEUE_NAME, new Signal(SignalType.BEGIN));
	}

	public void sendEndSignal() {
        rabbitTemplate.convertAndSend(DEFAULT_QUEUE_NAME, new Signal(SignalType.END));		
	}
	
	public void sendChunkEndSignal() {
        rabbitTemplate.convertAndSend(DEFAULT_QUEUE_NAME, new Signal(SignalType.CHUNK));		
	}

	public void purgeQueue()
	{
		rabbitAdmin.purgeQueue(DEFAULT_QUEUE_NAME, false);
	}
       
}

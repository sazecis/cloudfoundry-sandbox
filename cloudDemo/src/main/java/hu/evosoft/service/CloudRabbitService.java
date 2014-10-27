package hu.evosoft.service;

import hu.evosoft.logger.CounterEntity;
import hu.evosoft.rabbit.Signal;
import hu.evosoft.rabbit.SignalType;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * Handles the calls from the controller to the RabbitMQ.
 * 
 * @author Csaba.Szegedi
 *
 */
@Repository
public class CloudRabbitService {

	// used in RabbitMQ
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
	
	/**
	 * Queue the given message to the default queue.
	 * @param message to be queued
	 */
    public void queueMessage(String message) {
    	queueMessage(DEFAULT_QUEUE_NAME, message);
    }

	/**
	 * Queue the given message to the given queue.
	 * 
	 * @param queueName the name of the queue where the messages will be sent
	 * @param message to be queued
	 */
    public void queueMessage(String queueName, String message) {
        rabbitTemplate.convertAndSend(queueName, message);
    }

	/**
	 * Queue the given performance CounterEntity to the given queue.
	 * 
	 * @param queueName the name of the queue where the messages will be sent
	 * @param message to be queued
	 */
    public void queueMessage(String queueName, CounterEntity message) {
    	rabbitPerfTemplate.convertAndSend(queueName, message);
    }

	/**
	 * Send a Signal to the default queue pointing out that the we started to process a new file.  
	 */
	public void sendBeginSignal() {
        rabbitTemplate.convertAndSend(DEFAULT_QUEUE_NAME, new Signal(SignalType.BEGIN));
	}

	/**
	 * Send a Signal to the default queue pointing out that the we reached the end of the file which was processed.  
	 */
	public void sendEndSignal() {
		rabbitTemplate.convertAndSend(DEFAULT_QUEUE_NAME, new Signal(SignalType.END));
	}
	
	/**
	 * Send a Signal to the default queue pointing out that the data collected on receiver side should be transferred from
	 * Redis to MongoDB. 
	 */
	public void sendChunkEndSignal() {
        rabbitTemplate.convertAndSend(DEFAULT_QUEUE_NAME, new Signal(SignalType.CHUNK));		
	}

	/**
	 * Delete all messages from the default queue.
	 */
	public void purgeQueue()
	{
		rabbitAdmin.purgeQueue(DEFAULT_QUEUE_NAME, false);
	}
       
}

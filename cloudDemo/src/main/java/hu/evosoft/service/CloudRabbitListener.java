package hu.evosoft.service;

import hu.evosoft.logger.CounterCategory;
import hu.evosoft.logger.CounterEntity;
import hu.evosoft.logger.CounterType;
import hu.evosoft.logger.MyLogger;
import hu.evosoft.parser.InvalidNetStatLineException;
import hu.evosoft.parser.NetStatsParser;
import hu.evosoft.rabbit.Signal;
import hu.evosoft.rabbit.SignalType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Used as a listener for RabbitMQ queues.
 * 
 * @author Csaba.Szegedi
 *
 */
public class CloudRabbitListener {

	// if this value is true then the received messages won't be sent further to the Redis but just to DevNull. 
	private static boolean isDevNullSet = false;	
	
	@Autowired
	@Qualifier("cloudRedisService")
	private CloudRedisService redisService;
	@Autowired
	@Qualifier("cloudMongoService")
	private CloudMongoService mongoService;
	@Autowired
	private PerformanceCounterService performanceCounterService;

	@Autowired
	private RedisMongoTransferrer redisMongoTransferrer;

	/**
	 * Listens for the messages.
	 * 
	 * @param message the message received from the queue.
	 */
	public void listen(Object message) {
		// if it is a string then process the message as an IMongoModel
		if (message instanceof String) {
			receiveStrings((String) message);
		}
		// if the message type is a Signal then handle accordingly
		else if (message instanceof Signal) {
			receiveSignal((Signal) message);
		}
	}

	
	private void receiveStrings(String message) {
		if (isDevNullSet) {
			return;
		}
		try {
			// Split the message and add the needed information to the Redis
			String[] parts = NetStatsParser.splitLine(message);
			redisService.addNetStatInfo(NetStatsParser.getDestinationHost(parts), 
					Long.toString(NetStatsParser.getTimeStamp(parts)));
		}
		catch (InvalidNetStatLineException x) {
			// In case the received message has an incorrect format then log this
			MyLogger.appendLog(x.getMessage());
		}
	}
	
	private void receiveSignal(Signal signal) {
		MyLogger.appendLog("Signal listener: ", signal.toString());
		switch (signal.getType().name()) {
			// start to measure the queue receiver side
			case "BEGIN" : {
				performanceCounterService.addNewCounterEntry(
						CounterCategory.RABBIT_RECEIVE, CounterType.START, 
						this.getClass().getSimpleName(), 
						System.currentTimeMillis());
				break;
			}
			// stop measuring the queue receiver side
			case "END" : {
				performanceCounterService.addNewCounterEntry(
						CounterCategory.RABBIT_RECEIVE, CounterType.END, 
						this.getClass().getSimpleName(), 
						System.currentTimeMillis());
				break;
			}
		}
		if (signal != null && !isDevNullSet &&  
				(signal.getType().equals(SignalType.END) || signal.getType().equals(SignalType.CHUNK))) {
			try {
				redisMongoTransferrer.transferAll();
			} 
			catch (Exception ex) {
				MyLogger.appendLog("Transfer exception:", ex.toString());
				MyLogger.appendLog(" ", ex.getStackTrace());
			}
		}
	}

	/**
	 * Turn on and off the DevNull
	 * 
	 * @return it returns a string which can be used in JSP for checkbox states, e.g. "checked"
	 */
	public static String changeDevNullState() {
		isDevNullSet = !isDevNullSet;
		return isDevNullSet ? "checked" : "";
	}
	
	/**
	 * Listen for the performance counter messages.
	 * 
	 * @param entity the received measurement
	 */
	public void listenCounter(CounterEntity entity) {
		MyLogger.appendLog("listenCounter {0}", entity);
		mongoService.addCounter(entity);
	}	
	
}

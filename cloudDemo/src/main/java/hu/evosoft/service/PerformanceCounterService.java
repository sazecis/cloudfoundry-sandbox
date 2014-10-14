package hu.evosoft.service;

import hu.evosoft.logger.CounterCategory;
import hu.evosoft.logger.CounterEntity;
import hu.evosoft.logger.CounterType;
import hu.evosoft.logger.MyLogger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Handle the performance counter request. The counters will be sent to a centralized queue. 
 * The CloudRabbitListener will add the messages to the MongoDB.
 * 
 * @author Csaba.Szegedi
 *
 */
@Service
public class PerformanceCounterService {
	
	@Autowired
	@Qualifier("cloudMongoService")
	private CloudMongoService mongoService;
	@Autowired
	@Qualifier("cloudRabbitService")
	private CloudRabbitService rabbitService;

	// the queue where the performance counter are sent
	private static final String QUEUE_NAME = "perfCounter";

	/**
	 * Add a new performance entry to the queue.
	 * 
	 * @param category CounterCategory enum
	 * @param counterType CounterType enum
	 * @param name the name of the measurement
	 * @param value the millisecond when the measurement was taken
	 */
	public void addNewCounterEntry(CounterCategory category, CounterType counterType, String name, long value) {
		addNewCounterEntry(new CounterEntity(category, counterType, name, value));
	}
		
	/**
	 * Add a new performance entry to the queue.
	 * 
	 * @param entity contains the category, type, name and measurement value.
	 */
	public void addNewCounterEntry(CounterEntity entity) {
		MyLogger.appendLog("addNewCounterEntry: Queue[{0}], Counter[{1}]", QUEUE_NAME, entity.toString());
		rabbitService.queueMessage(QUEUE_NAME, entity);
	}
	
	/**
	 * Sums the measurements for a given counter category, e.g. RABBIT_SEND.
	 * 
	 * @param category
	 * @return the milliseconds spent on the given category
	 */
	public long getGlobalTimeSpentFor(CounterCategory category) {
		long timeStart = Long.MAX_VALUE; 
		long timeEnd = -1;
		// the counters are stored in MongoDB
		for (CounterEntity entity : mongoService.listPerformanceCounters()) {
			MyLogger.appendLog("getGlobalTimeSpentFor counter: {0}", entity);
			if (entity.getCategory().equals(category)) {
				switch (entity.getCounterType().name()) {
					case "START" : {
						timeStart = entity.getValue() < timeStart ? 
								entity.getValue() : timeStart; 
						break;
					}
					case "END" : {
						timeEnd = entity.getValue() > timeEnd ?
								entity.getValue() : timeEnd; 
						break;
					}
				}
			}
		}
		if (timeStart != Long.MAX_VALUE && timeEnd != -1) {
			return timeEnd - timeStart;
		}
		return -1;
	}
	
	/**
	 * Delete the measurements from MongoDB.
	 */
	public void clearCounters() {
		mongoService.clearDocuments(CounterEntity.class);
	}
	
	
}


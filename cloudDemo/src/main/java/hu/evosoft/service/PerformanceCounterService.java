package hu.evosoft.service;

import hu.evosoft.logger.CounterCategory;
import hu.evosoft.logger.CounterEntity;
import hu.evosoft.logger.CounterType;
import hu.evosoft.logger.MyLogger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class PerformanceCounterService {
	
	@Autowired
	@Qualifier("cloudMongoService")
	private CloudMongoService mongoService;
	@Autowired
	@Qualifier("cloudRabbitService")
	private CloudRabbitService rabbitService;

	private static final String QUEUE_NAME = "perfCounter";

	public void addNewCounterEntry(CounterCategory category, CounterType counterType, String name, long value) {
		addNewCounterEntry(new CounterEntity(category, counterType, name, value));
	}
		
	public void addNewCounterEntry(CounterEntity entity) {
		MyLogger.appendLog("addNewCounterEntry: Queue[{0}], Counter[{1}]", QUEUE_NAME, entity.toString());
		rabbitService.queueMessage(QUEUE_NAME, entity);
	}
	
	public long getGlobalTimeSpentFor(CounterCategory category) {
		long timeStart = -1; 
		long timeEnd = Long.MAX_VALUE;
		for (CounterEntity entity : mongoService.listPerformanceCounters()) {
			MyLogger.appendLog("getGlobalTimeSpentFor counter: {0}", entity);
			if (entity.getCategory().equals(category)) {
				switch (entity.getCounterType().name()) {
					case "START" : {
						timeStart = entity.getValue() > timeStart ? 
								entity.getValue() : timeStart; 
						break;
					}
					case "END" : {
						timeEnd = entity.getValue() < timeEnd ?
								entity.getValue() : timeEnd; 
						break;
					}
				}
			}
		}
		if (timeStart != -1 && timeEnd != Long.MAX_VALUE) {
			return timeEnd - timeStart;
		}
		return -1;
	}
	
	public void clearCounters() {
		mongoService.clearDocuments(CounterEntity.class);
	}
	
	
}


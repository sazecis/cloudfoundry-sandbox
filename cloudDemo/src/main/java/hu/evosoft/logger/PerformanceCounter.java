package hu.evosoft.logger;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PerformanceCounter {

	@Autowired
	@Qualifier("rabbitPerfCountTemplate")
	private static RabbitTemplate rabbitPerfCountTemplate;
	@Autowired
	private static MongoTemplate mongoTemplate;
	
	private static Map<CounterId, Long> counters = new HashMap<>();

	private static final String QUEUE_NAME = "perfCounter";
	
	public static void addNewCounterEntry(CounterId id, long timeStamp) {
		rabbitPerfCountTemplate.convertAndSend(QUEUE_NAME, new CounterEntity(id, timeStamp));
		counters.put(id, timeStamp);
	}
	
	public static long getGlobalTimeSpentFor(CounterCategory category) {
		long timeStart = -1; 
		long timeEnd = -1;
		
		for (CounterId id : counters.keySet()) {
			if (id.getCategory().equals(category)) {
				switch (id.getType().name()) {
					case "START" : timeStart = counters.get(id); break;
					case "END" : timeEnd = counters.get(id); break;
				}
			}
		}
		if (timeStart != -1 && timeStart != -1) {
			return timeEnd - timeStart;
		}
		return -1;
	} 

	public static long getLocalTimeSpentFor(CounterCategory category) {
		long timeStart = Long.MAX_VALUE; 
		long timeEnd = -1;
		for (CounterEntity entity : mongoTemplate.findAll(CounterEntity.class)) {
			CounterId id = entity.getId();
			if (id.getCategory().equals(category)) {
				switch (id.getType().name()) {
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
		if (timeStart != -1 && timeStart != -1) {
			return timeEnd - timeStart;
		}
		return -1;
	}
	
	public void listen(CounterEntity entity) {
		if (!mongoTemplate.collectionExists(entity.getClass())) {
			mongoTemplate.createCollection(entity.getClass());
		}
		mongoTemplate.insert(entity);	
	}	
	
}


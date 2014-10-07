package hu.evosoft.service;

import hu.evosoft.logger.CounterCategory;
import hu.evosoft.logger.CounterId;
import hu.evosoft.logger.CounterType;
import hu.evosoft.logger.MyLogger;
import hu.evosoft.logger.PerformanceCounter;
import hu.evosoft.parser.InvalidNetStatLineException;
import hu.evosoft.parser.NetStatsParser;
import hu.evosoft.rabbit.Signal;
import hu.evosoft.rabbit.SignalType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class CloudRabbitListener {

	private static boolean isDevNullSet = false;	
	
	@Autowired
	@Qualifier("cloudRedisService")
	private CloudRedisService redisService;

	@Autowired
	private RedisMongoTransferrer redisMongoTransferrer;

	public void listen(Object message) {
		if (message instanceof String) {
			receiveStrings((String) message);
		} else if (message instanceof Signal) {
			receiveSignal((Signal) message);
		} 
	}

	public void receiveStrings(String message) {
		if (isDevNullSet) {
			return;
		}
		try {
			String[] parts = NetStatsParser.splitLine(message);
			redisService.addNetStatInfo(NetStatsParser.getDestinationHost(parts), 
					Long.toString(NetStatsParser.getTimeStamp(parts)));
		}
		catch (InvalidNetStatLineException x) {
			MyLogger.appendLog(x.getMessage(), x.getStackTrace());
		}
	}
	
	public void receiveSignal(Signal signal) {
		MyLogger.appendLog("Signal listener: ", signal.toString());
		switch (signal.getType().name()) {
			case "START" : {
				PerformanceCounter.addNewCounterEntry(
						new CounterId(CounterCategory.RABBIT_RECEIVE, CounterType.START, this.getClass().getSimpleName()), 
						System.currentTimeMillis());
			}
			case "END" : {
				PerformanceCounter.addNewCounterEntry(
						new CounterId(CounterCategory.RABBIT_RECEIVE, CounterType.END, this.getClass().getSimpleName()), 
						System.currentTimeMillis());				
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

	public static String changeDevNullState() {
		isDevNullSet = !isDevNullSet;
		return isDevNullSet ? "checked" : "";
	}
	
}

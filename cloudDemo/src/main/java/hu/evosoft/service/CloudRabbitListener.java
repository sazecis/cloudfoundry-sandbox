package hu.evosoft.service;

import hu.evosoft.model.Signal;
import hu.evosoft.model.SignalType;
import hu.evosoft.parser.NetStatsParser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class CloudRabbitListener {

	@Autowired
	@Qualifier("cloudRedisService")
	private CloudRedisService redisService;
	
	public void listen(String message) {
		redisService.addDestinationHost(NetStatsParser.parseAsDestinationHost(message));
	}

	public void listen(Signal signal) {
		if (signal.getType().equals(SignalType.END)) {
			
		}
	}

}

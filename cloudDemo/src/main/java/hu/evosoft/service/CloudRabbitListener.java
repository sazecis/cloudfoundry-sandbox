package hu.evosoft.service;

import hu.evosoft.logger.MyLogger;
import hu.evosoft.model.Signal;
import hu.evosoft.model.SignalType;
import hu.evosoft.parser.NetStatsParser;
import hu.evosoft.transfer.RedisMongoTransferrer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class CloudRabbitListener {

	@Autowired
	@Qualifier("cloudRedisService")
	private CloudRedisService redisService;

	@Autowired
	private RedisMongoTransferrer redisMongoTransferrer;

	public void listen(String message) {
		if (NetStatsParser.isNetStatLog(message)) {
			redisService.addDestinationHost(NetStatsParser.getDestinationHost(message));
		}
		else {
			redisService.addData(message);
		}
	}

	public void listen(Signal signal) {
		MyLogger.appendLog("Signal listener: ", signal.toString());
		if (signal != null && signal.getType().equals(SignalType.END)) {
			redisMongoTransferrer.transferAllDestinationHosts();
		}
	}

}

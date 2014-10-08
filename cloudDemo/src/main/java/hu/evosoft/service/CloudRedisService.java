package hu.evosoft.service;

import hu.evosoft.logger.MyLogger;
import hu.evosoft.model.DestinationHost;
import hu.evosoft.model.IMongoModel;
import hu.evosoft.model.LogEntryDate;
import hu.evosoft.parser.NetStatsParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
public class CloudRedisService {

    @Autowired 
    private RedisTemplate<String, String> redisTemplate;
    
    private static final String MY_KEYS = "MY_KEYS";
    
    private static final String INC_STEP = "1";  
    
    // inject the template as ListOperations
    // can also inject as Value, Set, ZSet, and HashOperations
    @Resource(name="redisTemplate")
    private ListOperations<String, String> listOps;
    @Resource(name="redisTemplate")
    private SetOperations<String, String> setOps;
    @Resource(name="redisTemplate")
    private ValueOperations<String, String> valOps;
    
    public void addNetStatInfo(String... keys) {    	
		setOps.add(MY_KEYS, keys);
    	for (String key : keys) {
    		listOps.rightPush(key, INC_STEP);
    	}
    }

    public List<String> listNetStatInfo() {
    	MyLogger.appendLog("{0}: {1}", MY_KEYS, setOps.members(MY_KEYS));
    	List<String> list = new ArrayList<String>();
    	for (String key : getKeys()) {
    		list.add(String.format("%s %s", key, listOps.range(key, 0, listOps.size(key))));
    	}
    	return list;
    }
    
    public List<IMongoModel> popAllMongoCompatibleValues() {
    	List<IMongoModel> list = new ArrayList<IMongoModel>();
    	for (String key : getKeys()) {
        	MyLogger.appendLog("popAllDestinationHosts {0} is a date in milis = {1}", 
        			key, NetStatsParser.isDateInMilisecond(key));
    		if (NetStatsParser.isDateInMilisecond(key)) {
        		list.add(new LogEntryDate(Long.parseLong(key), listOps.size(key).intValue()));    			    			
    		} 
    		else {
        		list.add(new DestinationHost(key, listOps.size(key).intValue()));    			
    		}
    		flushKey(key);
    	}
    	return list;		
    }

    private Set<String> getKeys() {
    	return setOps.members(MY_KEYS);
    }
    
    private void flushKey(String key) {
    	redisTemplate.delete(key);
    	setOps.remove(MY_KEYS, key);
    }
    
    public void clearData() {
    	for (String key : getKeys()) {
    		redisTemplate.delete(key);
    	}
		redisTemplate.delete(MY_KEYS);
    }
}

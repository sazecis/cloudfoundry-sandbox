package hu.evosoft.service;

import hu.evosoft.logger.MyLogger;
import hu.evosoft.model.Data;
import hu.evosoft.model.DestinationHost;
import hu.evosoft.model.IMongoModel;
import hu.evosoft.model.LogEntryDate;
import hu.evosoft.parser.NetStatsParser;

import java.sql.Timestamp;
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
    
    private static final String SIMPLE_KEY = "myData";
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
    
    public void addData(String name) {
    	listOps.rightPush(SIMPLE_KEY, name);
    }

    public List<String> listData() {
    	return listOps.range(SIMPLE_KEY, 0, listOps.size(SIMPLE_KEY));
    }

    public Data getData() {
    	Data data = new Data();
    	data.setData(listOps.leftPop(SIMPLE_KEY));
    	return data;		
    }

    public void addNetStatInfo(Long key) {    	
    	if (key != null) {
    		addNetStatInfo(Long.toString(key));
    	}
    }

    public void addNetStatInfo(String key) {
    	if (!redisTemplate.hasKey(key)) {
    		setOps.add(MY_KEYS, key);
    	} 
		listOps.rightPush(key, INC_STEP);
    }

    public List<String> listNetStatInfo() {
    	MyLogger.appendLog(MY_KEYS + ": " + setOps.members(MY_KEYS).toString());
    	List<String> list = new ArrayList<String>();
    	for (String key : getKeys()) {
    		list.add(String.format("%s %s", key, listOps.range(key, 0, listOps.size(key))));
    	}
    	return list;
    }
    
    public List<IMongoModel> popAllMongoCompatibleValues() {
    	List<IMongoModel> list = new ArrayList<IMongoModel>();
    	for (String key : getKeys()) {
        	MyLogger.appendLog(String.format("popAllDestinationHosts %s is a date in milis = %s", 
        			key, NetStatsParser.isDateInMilisecond(key)));
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
    	redisTemplate.delete(SIMPLE_KEY);
    }
}

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

/**
 * Handles the calls from the controller to the Redis.
 * 
 * @author Csaba.Szegedi
 *
 */
@Repository
public class CloudRedisService {

    @Autowired 
    private RedisTemplate<String, String> redisTemplate;
    
    // this is a special key which will be used as a pointer to the real keys 
    private static final String MY_KEYS = "MY_KEYS";
    
    private static final String INC_STEP = "1";  
    
    // inject the template as ListOperations
    // can also inject as Value, Set
    @Resource(name="redisTemplate")
    private ListOperations<String, String> listOps;
    @Resource(name="redisTemplate")
    private SetOperations<String, String> setOps;
    @Resource(name="redisTemplate")
    private ValueOperations<String, String> valOps;
    
    /**
     * Default ctr.
     */
    public CloudRedisService() {
    	
    }
    
    /**
     * Constructor initialized with a redis template and with needed operations. Helps unit testing.
     * 
     * @param redisTemplate
     * @param listOps
     * @param setOps
     * @param valOps
     */
    public CloudRedisService(RedisTemplate<String, String> redisTemplate, 
    		ListOperations<String, String> listOps, 
    		SetOperations<String, String> setOps,
    		ValueOperations<String, String> valOps) {
    	this.redisTemplate = redisTemplate;
    	this.listOps = listOps;
    	this.setOps = setOps;
    	this.valOps = valOps;
    }
    
    /**
     * Add the given keys to the Redis. If already exists then will just register that the
     * given key was received one more time. This is the Mapping phase.
     * 
     * @param keys the keys to be added to the Redis
     */
    public void addNetStatInfo(String... keys) {    
    	// add the key to MY_KEYS set
		setOps.add(MY_KEYS, keys);
    	for (String key : keys) {
    		// <key, 1, 1, 1, ... ,1>
    		listOps.rightPush(key, INC_STEP);
    	}
    }

    /**
     * Get the list of the stored key value pairs.
     * 
     * @return String list "key 1 1 1 1 ..."
     */
    public List<String> listNetStatInfo() {
    	MyLogger.appendLog("{0}: {1}", MY_KEYS, setOps.members(MY_KEYS));
    	List<String> list = new ArrayList<String>();
    	for (String key : getKeys()) {
    		list.add(String.format("%s %s", key, listOps.range(key, 0, listOps.size(key))));
    	}
    	return list;
    }
    
    /**
     * Pop all values from Redis which will be transfered to MongoDB.
     * Combiner phase.
     * 
     * @return the combined list of IMongoModels
     */
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
    
    /**
     * Clear the Redis.
     */
    public void clearData() {
    	for (String key : getKeys()) {
    		redisTemplate.delete(key);
    	}
		redisTemplate.delete(MY_KEYS);
    }
}

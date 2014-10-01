package hu.evosoft.service;

import hu.evosoft.model.Data;
import hu.evosoft.model.DestinationHost;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CloudRedisService {

    @Autowired 
    private RedisTemplate<String, String> redisTemplate;
    
    private static final String SIMPLE_KEY = "myData";
    private static final String DEST_HOST_KEY = "DestHost";
    
    // inject the template as ListOperations
    // can also inject as Value, Set, ZSet, and HashOperations
    @Resource(name="redisTemplate")
    private ListOperations<String, String> listOps;
    @Resource(name="redisTemplate")
    private ListOperations<String, DestinationHost> listOpsDestHost;

    public void addData(String name) {
        listOps.rightPush(SIMPLE_KEY, name);
    }

    public void addDestinationHost(DestinationHost destHost) {
    	listOpsDestHost.rightPush(DEST_HOST_KEY, destHost);
    }

    public List<String> listData() {
    	return listOps.range(SIMPLE_KEY, 0, listOps.size(SIMPLE_KEY));
    }

    public List<DestinationHost> listDestionationHosts() {
    	return listOpsDestHost.range(DEST_HOST_KEY, 0, listOps.size(DEST_HOST_KEY));
    }
    
    public Data getData() {
    	Data data = new Data();
    	data.setData(listOps.leftPop(SIMPLE_KEY));
    	return data;		
    }

    public Data popData(Data data) {
    	data.setData(listOps.leftPop(SIMPLE_KEY));
    	return data;		
    }

    public void clearData() {
    	redisTemplate.delete(SIMPLE_KEY);
    	redisTemplate.delete(DEST_HOST_KEY);
    }
}

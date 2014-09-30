package hu.evosoft.service;

import hu.evosoft.model.Data;

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
    
    private static final String key = "myData";
    
    // inject the template as ListOperations
    // can also inject as Value, Set, ZSet, and HashOperations
    @Resource(name="redisTemplate")
    private ListOperations<String, String> listOps;

    public void addData(String name) {
        listOps.rightPush(key, name);
    }
	
    public List<String> listData() {
    	return listOps.range(key, 0, listOps.size(key));
    }
    
    public Data getData() {
    	Data data = new Data();
    	data.setName(listOps.leftPop(key));
    	return data;
    		
    }
    
}

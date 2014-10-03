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
    /*@Autowired @Qualifier("redisTemplate")
    private RedisTemplate<DestinationHost, Integer> redisTemplateDestHost;*/
    
    private static final String SIMPLE_KEY = "myData";
    private static final String DEST_HOST_KEY = "DestHost";
    
    // inject the template as ListOperations
    // can also inject as Value, Set, ZSet, and HashOperations
    @Resource(name="redisTemplate")
    private ListOperations<String, String> listOps;
    @Resource(name="redisTemplate")
    private ListOperations<String, DestinationHost> listOpsDestHost;
    /*@Resource(name="redisTemplate")
    private ListOperations<DestinationHost, Integer> listOpsDestHostTemp;*/

    public void addData(String name) {
		/*MyLogger.appendLog("Redis keys");
		listOpsDestHostTemp.rightPush(new DestinationHost("Test", 1), 1);
    	try {
			for (DestinationHost key : redisTemplateDestHost.keys(DestinationHost.class.newInstance())) {
				MyLogger.appendLog("Redis key:", key.toString());
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
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

    public List<DestinationHost> popAllDestionationHosts() {
    	List<DestinationHost> list = listDestionationHosts();
    	redisTemplate.delete(DEST_HOST_KEY);
    	return list;		
    }

    public void clearData() {
    	redisTemplate.delete(SIMPLE_KEY);
    	redisTemplate.delete(DEST_HOST_KEY);
    }
}

package hu.evosoft.service;

import hu.evosoft.model.Person;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PersonRedisService {

    @Autowired 
    private RedisTemplate<String, String> redisTemplate;
    
    private static final String key = "myPerson";
    
    // inject the template as ListOperations
    // can also inject as Value, Set, ZSet, and HashOperations
    @Resource(name="redisTemplate")
    private ListOperations<String, String> listOps;

    public void addPerson(String name) {
        listOps.rightPush(key, name);
    }
	
    public List<String> listPerson() {
    	return listOps.range(key, 0, listOps.size(key));
    }
    
    public Person getPerson() {
    	Person person = new Person();
    	person.setName(listOps.leftPop(key));
    	return person;
    		
    }
    
}

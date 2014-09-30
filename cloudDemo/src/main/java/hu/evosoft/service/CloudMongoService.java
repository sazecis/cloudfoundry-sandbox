package hu.evosoft.service;

import hu.evosoft.model.Data;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
 
@Repository
public class CloudMongoService {
     
    @Autowired 
    private MongoTemplate mongoTemplate;
     
    public static final String COLLECTION_NAME = "data";
     
    public void addData(Data data) {
        if (!mongoTemplate.collectionExists(Data.class)) {
            mongoTemplate.createCollection(Data.class);
        }       
        data.setId(UUID.randomUUID().toString());
        mongoTemplate.insert(data, COLLECTION_NAME);
    }
     
    public List<Data> listData() {
        return mongoTemplate.findAll(Data.class, COLLECTION_NAME);
    }
     
    public void deleteData(Data data) {
        mongoTemplate.remove(data, COLLECTION_NAME);
    }
     
    public void updateData(Data data) {
        mongoTemplate.insert(data, COLLECTION_NAME);      
    }
}

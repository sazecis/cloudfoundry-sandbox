package hu.evosoft.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
 
@Document
public class Data {
 
    @Id
    private String id;
    private String name;

    public Data() {
    }

    public Data(String name) {
    	this.name = name;
    }
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
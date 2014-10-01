package hu.evosoft.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
 
@Document
public class Data {
 
    @Id
    private String id;
    private String data;

    public Data() {
    }

    public Data(String data) {
    	this.data = data;
    }
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }
}
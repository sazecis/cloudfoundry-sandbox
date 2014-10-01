package hu.evosoft.model;
 
public class Data extends AbstractModelWithId {
 
    /**
	 * Generated UID
	 */
	private static final long serialVersionUID = -4509338178469037612L;
	
	private String data;

    public Data() {
    }

    public Data(String data) {
    	this.data = data;
    }
    
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }
}
package hu.evosoft.model;

@Deprecated
public class Data extends AbstractMongoModel {
 
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

	@Override
	public void exchangeInnerItems() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String mapper() {
		// TODO Auto-generated method stub
		return null;
	}

}
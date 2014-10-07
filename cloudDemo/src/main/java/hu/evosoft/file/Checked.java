package hu.evosoft.file;

public enum Checked {

	YES("checked"), NO("");
	
	private final String value;
	
	Checked(String value) {
		this.value = value;
	}
	
	public String getValue() {
        return value;
    }
}

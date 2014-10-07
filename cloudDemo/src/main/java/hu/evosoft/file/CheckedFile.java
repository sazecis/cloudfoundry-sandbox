package hu.evosoft.file;

import java.text.MessageFormat;

public class CheckedFile {

	private Checked checked;
	private String name;

	public CheckedFile(String name, Checked checked) {
		this.name = name;
		this.checked = checked;
	}

	public Checked getChecked() {
		return checked;
	}

	public void setChecked(Checked checked) {
		this.checked = checked;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object file) {
		return this.name.equals(((CheckedFile)file).name);
	}
	
	@Override
	public int hashCode() {
		if (name != null) {
			return name.hashCode();		
		}
		return this.getClass().hashCode();
	}

	@Override
	public String toString() {
		return MessageFormat.format("{0} : {1}", name, checked);
	}

}

package hu.evosoft.file;

import java.text.MessageFormat;

public class UploadedFile {

	private String size = "0";
	private String name = "";

	public UploadedFile() {
		
	}
	
	public UploadedFile(String name, String size) {
		this.name = name;
		this.size = size;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object file) {
		return this.name.equals(((UploadedFile)file).name);
	}
	
	@Override
	public int hashCode() {
		return name.hashCode() + new Long(size).hashCode();
	}

	@Override
	public String toString() {
		return MessageFormat.format("{0} : {1}", name, size);
	}

}

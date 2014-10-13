package hu.evosoft.file;

import java.text.MessageFormat;

/**
 * 
 * Data transfer object for files which are uploaded to the ReadNetStatController.
 * 
 * @author Csaba.Szegedi
 *
 */
public class UploadedFile {

	private String size = "0";
	private String name = "";

	/**
	 * Default ctr. 
	 */
	public UploadedFile() {
		
	}

	/**
	 * Constructor signed with file name and size of the file.  
	 * 
	 * @param fileName
	 * @param size
	 */
	public UploadedFile(String fileName, String size) {
		this.name = fileName;
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

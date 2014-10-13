package hu.evosoft.parser;

/**
 * Used in case some exception occurs during the parsing of the raw statistics file.
 * 
 * @author Csaba.Szegedi
 *
 */
public class InvalidNetStatLineException extends Exception {

	/**
	 * generated
	 */
	private static final long serialVersionUID = -4626443724694611022L;

	public InvalidNetStatLineException() {
		super();
	}

	public InvalidNetStatLineException(String message) {
		super(message);
	}

	public InvalidNetStatLineException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidNetStatLineException(Throwable cause) {
		super(cause);
	}

}

package org.nicksun.shrek.validator;

/**
 * @author nicksun
 *
 */
public class ValidException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1879039041664008239L;

	public ValidException() {
		super();
	}

	public ValidException(String message) {
		super(message);
	}

	public ValidException(String message, Throwable cause) {
		super(message, cause);
	}

	public ValidException(Throwable cause) {
		super(cause);
	}

	public ValidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}

/**
 * 
 */
package com.beans.exceptions;

/**
 * @author Admin
 *
 */
public class BSLException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8741390597638777104L;

	public BSLException() {
		super();
	}

	public BSLException(String message, Throwable cause) {
		super(message, cause);
	}

	public BSLException(String message) {
		super(message);
	}

	public BSLException(Throwable cause) {
		super(cause);
	}
}

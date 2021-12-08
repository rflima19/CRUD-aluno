package main.java.com.exceptions;

public class SistemaEscolarRuntimeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3601990653954299329L;

	public SistemaEscolarRuntimeException() {
		super();
	}

	public SistemaEscolarRuntimeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public SistemaEscolarRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public SistemaEscolarRuntimeException(String message) {
		super(message);
	}

	public SistemaEscolarRuntimeException(Throwable cause) {
		super(cause);
	}
}

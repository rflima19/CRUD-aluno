package main.java.com.exceptions;

public class SistemaEscolarException extends Exception {

	public SistemaEscolarException() {
		super();
	}

	public SistemaEscolarException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public SistemaEscolarException(String message, Throwable cause) {
		super(message, cause);
	}

	public SistemaEscolarException(String message) {
		super(message);
	}

	public SistemaEscolarException(Throwable cause) {
		super(cause);
	}
}

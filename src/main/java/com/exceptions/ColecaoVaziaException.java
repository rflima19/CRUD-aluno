package main.java.com.exceptions;

public class ColecaoVaziaException extends Exception {

	private static final long serialVersionUID = -1687091871448253907L;

	public ColecaoVaziaException() {
		super();
	}

	public ColecaoVaziaException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ColecaoVaziaException(String message, Throwable cause) {
		super(message, cause);
	}

	public ColecaoVaziaException(String message) {
		super(message);
	}

	public ColecaoVaziaException(Throwable cause) {
		super(cause);
	}
}

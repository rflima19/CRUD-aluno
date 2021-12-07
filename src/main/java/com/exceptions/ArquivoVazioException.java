package main.java.com.exceptions;

public class ArquivoVazioException extends Exception {

	private static final long serialVersionUID = -1687091871448253907L;

	public ArquivoVazioException() {
		super();
	}

	public ArquivoVazioException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ArquivoVazioException(String message, Throwable cause) {
		super(message, cause);
	}

	public ArquivoVazioException(String message) {
		super(message);
	}

	public ArquivoVazioException(Throwable cause) {
		super(cause);
	}
}

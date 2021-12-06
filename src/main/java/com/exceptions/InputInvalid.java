package main.java.com.exceptions;

public class InputInvalid extends Exception {

	private static final long serialVersionUID = -4816082193702272012L;
	private Object input;

	public InputInvalid(Object input) {
		super();
		this.input = input;
	}

	public InputInvalid(Object input, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.input = input;
	}

	public InputInvalid(Object input, String message, Throwable cause) {
		super(message, cause);
		this.input = input;
	}

	public InputInvalid(Object input, String message) {
		super(message);
		this.input = input;
	}

	public InputInvalid(Object input, Throwable cause) {
		super(cause);
		this.input = input;
	}

	public Object getInput() {
		return input;
	}
}

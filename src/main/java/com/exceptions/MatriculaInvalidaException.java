package main.java.com.exceptions;

public class MatriculaInvalidaException extends Exception {

	private static final long serialVersionUID = -1831731483143515173L;
	private int matricula;

	public MatriculaInvalidaException(int matricula) {
		super();
		this.matricula = matricula;
	}

	public MatriculaInvalidaException(int matricula, String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.matricula = matricula;
	}

	public MatriculaInvalidaException(int matricula, String message, Throwable cause) {
		super(message, cause);
		this.matricula = matricula;
	}

	public MatriculaInvalidaException(int matricula, String message) {
		super(message);
		this.matricula = matricula;
	}

	public MatriculaInvalidaException(int matricula, Throwable cause) {
		super(cause);
		this.matricula = matricula;
	}
	
	public int getMatricula() {
		return matricula;
	}
}

package main.java.com.exceptions;

public class MatriculaDuplicadaException extends Exception {

	private static final long serialVersionUID = 2715151802706072742L;
	private int matricula;

	public MatriculaDuplicadaException(int matricula) {
		super();
		this.matricula = matricula;
	}

	public MatriculaDuplicadaException(int matricula, String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.matricula = matricula;
	}

	public MatriculaDuplicadaException(int matricula, String message, Throwable cause) {
		super(message, cause);
		this.matricula = matricula;
	}

	public MatriculaDuplicadaException(int matricula, String message) {
		super(message);
		this.matricula = matricula;
	}

	public MatriculaDuplicadaException(int matricula, Throwable cause) {
		super(cause);
		this.matricula = matricula;
	}
	
	public int getMatricula() {
		return matricula;
	}
}

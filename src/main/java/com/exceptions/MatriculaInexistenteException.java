package main.java.com.exceptions;

public class MatriculaInexistenteException extends Exception {

	private static final long serialVersionUID = -4587380235739182763L;
	private int matricula;

	public MatriculaInexistenteException(int matricula) {
		super();
		this.matricula = matricula;
	}

	public MatriculaInexistenteException(int matricula, String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.matricula = matricula;
	}

	public MatriculaInexistenteException(int matricula, String message, Throwable cause) {
		super(message, cause);
		this.matricula = matricula;
	}

	public MatriculaInexistenteException(int matricula, String message) {
		super(message);
		this.matricula = matricula;
	}

	public MatriculaInexistenteException(int matricula, Throwable cause) {
		super(cause);
		this.matricula = matricula;
	}
	
	public int getMatricula() {
		return matricula;
	}
}

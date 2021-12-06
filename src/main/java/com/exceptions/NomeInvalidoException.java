package main.java.com.exceptions;

public class NomeInvalidoException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7524833529950561133L;
	private String nome;

	public NomeInvalidoException(String nome) {
		super();
		this.nome = nome;
	}

	public NomeInvalidoException(String nome, String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.nome = nome;
	}

	public NomeInvalidoException(String nome, String message, Throwable cause) {
		super(message, cause);
		this.nome = nome;
	}

	public NomeInvalidoException(String nome, String message) {
		super(message);
		this.nome = nome;
	}

	public NomeInvalidoException(String nome, Throwable cause) {
		super(cause);
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}
}

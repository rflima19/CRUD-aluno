package main.java.com.view;

public abstract class InputUsuario {

	protected String titulo;
	protected String mensagemErro;

	public InputUsuario(String titulo) {
		super();
		if (titulo == null) {
			throw new IllegalArgumentException("argumento string nulo");
		}
		this.titulo = titulo;
	}

	public String getTitulo() {
		return titulo;
	}
	
	public String getMensagemErro() {
		return mensagemErro;
	}
	
	public abstract Object lerDado();
}

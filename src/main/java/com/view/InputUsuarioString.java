package main.java.com.view;

import main.java.com.util.Console;

public class InputUsuarioString extends InputUsuario {

	private String texto;
	private ValidadorString validador;
		
	public InputUsuarioString(String titulo, ValidadorString validador) {
		super(titulo);
		if (validador == null) {
			throw new IllegalArgumentException("Argumento validador nulo");
		}
		this.validador = validador;
	}
	
	public String getTexto() {
		return texto;
	}
	
	public ValidadorString getValidador() {
		return validador;
	}

	@Override
	public Object lerDado() {
		System.out.println(this.titulo);
		System.out.print(">>");
		this.texto = Console.readString();
		if (this.validador.test(this.texto) == false) {
			this.mensagemErro = "Input '" + this.texto + "' não é um válido";
			return null;
		}
		return this.texto;
	}

}

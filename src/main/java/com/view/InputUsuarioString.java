package main.java.com.view;

import main.java.com.util.Console;
import main.java.com.util.Validador;

public class InputUsuarioString extends InputUsuario<String> {

	private String texto;
	private Validador<String> validador;
		
	public InputUsuarioString(String titulo, Validador<String> validador) {
		super(titulo);
		if (validador == null) {
			throw new IllegalArgumentException("Argumento validador nulo");
		}
		this.validador = validador;
	}
	
	public String getTexto() {
		return texto;
	}
	
	public Validador<String> getValidador() {
		return validador;
	}

	@Override
	public String lerDado() {
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

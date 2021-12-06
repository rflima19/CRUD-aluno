package main.java.com.view;

import main.java.com.util.Console;

public class InputUsuarioNumeroInterio extends InputUsuario {

	private int valor;
	private ValidadorInteiro validador;

	public InputUsuarioNumeroInterio(String titulo, ValidadorInteiro validador) {
		super(titulo);
		if (validador == null) {
			throw new IllegalArgumentException("Argumento validador nulo");
		}
		this.validador = validador;
	}
	
	public int getValor() {
		return valor;
	}
	
	public ValidadorInteiro getValidador() {
		return validador;
	}

	@Override
	public Object lerDado() {
		System.out.println(this.titulo);
		System.out.print(">>");
		this.valor = Console.readInteger();
		if (this.validador.test(this.valor) == false) {
			this.mensagemErro = "Input '" + this.valor + "' não é um Integer válido";
			return null;
		}
		return this.valor;
	}

}

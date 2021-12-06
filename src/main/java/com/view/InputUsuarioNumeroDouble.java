package main.java.com.view;

import main.java.com.util.Console;

public class InputUsuarioNumeroDouble extends InputUsuario {
	
	private double valor;
	private ValidadorDouble validador;

	public InputUsuarioNumeroDouble(String titulo, ValidadorDouble validador) {
		super(titulo);
		if (validador == null) {
			throw new IllegalArgumentException("Argumento validador nulo");
		}
		this.validador = validador;
	}
	
	public double getValor() {
		return valor;
	}
	
	public ValidadorDouble getValidador() {
		return validador;
	}

	@Override
	public Object lerDado() {
		System.out.println(this.titulo);
		System.out.print(">>");
		this.valor = Console.readDouble();
		if (this.validador.test(this.valor) == false) {
			this.mensagemErro = "Input '" + this.valor + "' não é um Double válido";
			return null;
		}
		return this.valor;
	}
}

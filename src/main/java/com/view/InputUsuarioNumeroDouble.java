package main.java.com.view;

import main.java.com.util.Console;
import main.java.com.util.Validador;

public class InputUsuarioNumeroDouble extends InputUsuario<Double> {
	
	private double valor;
	private Validador<Double> validador;

	public InputUsuarioNumeroDouble(String titulo, Validador<Double> validador) {
		super(titulo);
		if (validador == null) {
			throw new IllegalArgumentException("Argumento validador nulo");
		}
		this.validador = validador;
	}
	
	public double getValor() {
		return valor;
	}
	
	public Validador<Double> getValidador() {
		return validador;
	}

	@Override
	public Double lerDado() {
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

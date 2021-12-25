package main.java.com.view;

import main.java.com.util.Console;
import main.java.com.util.Validador;

public class InputUsuarioNumeroInterio extends InputUsuario<Integer> {

	private int valor;
	private Validador<Integer> validador;

	public InputUsuarioNumeroInterio(String titulo, Validador<Integer> validador) {
		super(titulo);
		if (validador == null) {
			throw new IllegalArgumentException("Argumento validador nulo");
		}
		this.validador = validador;
	}
	
	public int getValor() {
		return valor;
	}
	
	public Validador<Integer> getValidador() {
		return validador;
	}

	@Override
	public Integer lerDado() {
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

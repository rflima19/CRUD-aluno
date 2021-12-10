package main.java.com.view;

import java.util.List;

import main.java.com.util.Console;

public class TerminalUsuario {

	public int menuOpcoes(String titulo, String[] titulos) {
		if (titulos == null) {
			throw new IllegalArgumentException("Argumento nulo");
		}
		if (titulos.length == 0) {
			throw new IllegalArgumentException("Array com tamanho 0");
		}
		//System.out.printf("%n");
		this.exibirCabecario(titulo);

		int opcao = 0;

		while (true) {
			try {
				for (int i = 0; i < titulos.length; i++) {
					System.out.println((i + 1) + " - " + titulos[i]);
				}
				System.out.print(">>");

				opcao = Console.readInteger();

				if (opcao < 1 || opcao > titulos.length) {
					this.exibirMensagemErro("Opcão " + opcao + " não existe no menu!");
				} else {
					break;
				}
			} catch (NumberFormatException e) {
				this.exibirMensagemErro("Digite um valor inteiro.");
				continue;
			}
		}
		return opcao;
	}
	
	public Object[] formulario(String titulo, List<InputUsuario> inputs) {
		if (titulo == null) {
			throw new IllegalArgumentException("argumento string nulo");
		}
		if (inputs == null) {
			throw new IllegalArgumentException("argumento map nulo");
		}
		if (inputs.size() == 0) {
			throw new IllegalArgumentException("coleção map vazia");
		}
		System.out.printf("%n");
		this.exibirCabecario(titulo);

		Object[] arrayInputs = new Object[inputs.size()];
		Object obj = null;
		int cont = 0;
		for (InputUsuario input : inputs) {
			while (true) {
				try {
					obj = input.lerDado();
					if (obj == null) {
						this.exibirMensagemErro(input.getMensagemErro());
						continue;
					}
					break;
				} catch (NumberFormatException e) {
					this.exibirMensagemErro(
							"Ops!!! Você digitou uma entrada textual em vez de uma entrada numerica. Tente novamente.");
					continue;
				}
			}
			arrayInputs[cont] = obj;
			++cont;

		}
		return arrayInputs;
	}

	public void imprimirTabela(String[] titulos, List<String[]> lista) {
		this.imprimirLinha();
		for (int i = 0; i < titulos.length; i++) {
			System.out.printf("%-30s\t", titulos[i]);
		}
		System.out.println();
		this.imprimirLinha();
		this.imprimirLinha();
		for (String[] strings : lista) {
			for (int i = 0; i < strings.length; i++) {
				System.out.printf("%-30s\t", strings[i]);
			}
			System.out.println();
			this.imprimirLinha();
		}
		this.imprimirLinha();
		System.out.println();
	}

	public void exibirMensagemErro(Exception exc) {
		// this.exibirMensagem(String.format("%s%n%s", "Mensagem de erro:",
		// exc.getMessage()));
		this.exibirMensagemErro(exc.getMessage());
	}

	public void exibirMensagemErro(String message) {
		this.exibirMensagem(String.format("%s%n%s", "Mensagem de erro:", message));
	}

	public void exibirMensagem(String message) {
		System.out.printf("%n");
		this.imprimirLinha();
		System.out.println(message);
		this.imprimirLinha();
		System.out.printf("%n");
	}

	private void exibirCabecario(String titulo) {
		this.imprimirLinha();
		this.imprimirLinhaComTitulo(titulo);
		this.imprimirLinha();
	}

	private void imprimirLinha() {
		for (int i = 0; i < 100; i++) {
			System.out.print("-");
		}
		System.out.println();
	}

	private void imprimirLinhaComTitulo(String titulo) {
		int tamanho = titulo.length();
		if (tamanho < 100) {
			tamanho = 100 - tamanho;
			tamanho = tamanho / 2;

			for (int i = 0; i < tamanho; i++) {
				System.out.print("-");
			}
			System.out.print(titulo);
			for (int i = 0; i < tamanho; i++) {
				System.out.print("-");
			}
			if (tamanho % 2 != 0) {
				System.out.println("-");
			} else {
				System.out.println();
			}
		}
	}
}

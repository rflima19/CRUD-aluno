package main.java.com.controller;

import main.java.com.view.TerminalCadastroAlunoView;

public class AppController {

	public void switchOpcao(int opcao) {
		switch (opcao) {
		case 1 -> new TerminalCadastroAlunoView().salvarAluno();
		case 7 -> System.exit(0);
		//default ->
		//throw new IllegalArgumentException("Unexpected value: " + opcao);
		}
	}
}

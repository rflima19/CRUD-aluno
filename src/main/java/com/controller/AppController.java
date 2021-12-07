package main.java.com.controller;

import main.java.com.view.TerminalCadastroAlunoView;
import main.java.com.view.TerminalConsultaAlunoView;
import main.java.com.view.TerminalRelatorioAlunoView;

public class AppController {

	public void switchOpcao(int opcao) {
		switch (opcao) {
		case 1 -> new TerminalCadastroAlunoView().salvarAluno();
		case 4 -> new TerminalConsultaAlunoView().consultarAluno();
		case 6 -> new TerminalRelatorioAlunoView().listarAlunos();
		case 7 -> System.exit(0);
		//default ->
		//throw new IllegalArgumentException("Unexpected value: " + opcao);
		}
	}
}

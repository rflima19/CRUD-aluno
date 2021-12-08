package main.java.com.controller;

import main.java.com.view.TerminalAlterarAlunoView;
import main.java.com.view.TerminalCadastroAlunoView;
import main.java.com.view.TerminalConsultaAlunoView;
import main.java.com.view.TerminalExcluirAlunoView;
import main.java.com.view.TerminalOrdenarAlunoView;
import main.java.com.view.TerminalRelatorioAlunoView;

public class AppController {

	public void switchOpcao(int opcao) {
		switch (opcao) {
		case 1 -> new TerminalCadastroAlunoView().salvarAluno();
		case 2 -> new TerminalExcluirAlunoView().excluirAluno();
		case 3 -> new TerminalAlterarAlunoView().alterarAluno();
		case 4 -> new TerminalConsultaAlunoView().consultarAluno();
		case 5 -> new TerminalOrdenarAlunoView().ordenarAluno();
		case 6 -> new TerminalRelatorioAlunoView().listarAlunos();
		case 7 -> System.exit(0);
		}
	}
}

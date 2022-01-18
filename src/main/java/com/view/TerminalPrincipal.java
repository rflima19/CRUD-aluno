package main.java.com.view;

import main.java.com.facade.FacadeConcrete;
import main.java.com.facade.FacadeOfSystem;

public class TerminalPrincipal {

	private FacadeOfSystem fachada;

	public TerminalPrincipal() {
		super();
		this.fachada = FacadeConcrete.getFacade();
	}

	public void exibir() {
		int opcao = 0;
		TerminalUsuario tu = new TerminalUsuario();
		String[] arrayStr = new String[] { "Incluir Aluno", "Excluir Aluno", "Alterar Aluno", "Consultar Aluno", "Ordenar Alunos", "Relatório",
				"Sair" };
		while (true) {
			opcao = tu.menuOpcoes("MENU PRINCIPAL", arrayStr);
			this.fachada.switchOpcao(opcao);
		}
	}
}

package main.java.com.view;

import main.java.com.controller.AppController;
import main.java.com.facade.FacadeConcrete;
import main.java.com.facade.FacadeOfSystem;

public class TerminalPrincipal {

	// private AppController appController;
	private FacadeOfSystem fachada;

	public TerminalPrincipal() {
		super();
		// this.appController = new AppController();
		this.fachada = FacadeConcrete.getFacade();
	}

	public void exibir() {
		int opcao = 0;
		TerminalUsuario tu = new TerminalUsuario();
		String[] arrayStr = new String[] { "Incluir", "Excluir", "Alterar", "Consultar", "Ordenar", "Relatório",
				"Sair" };
		while (true) {
			opcao = tu.menuOpcoes("MENU PRINCIPAL", arrayStr);
			// this.appController.switchOpcao(opcao);
			this.fachada.switchOpcao(opcao);
		}
	}
}

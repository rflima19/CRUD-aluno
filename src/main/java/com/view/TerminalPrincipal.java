package main.java.com.view;

import main.java.com.controller.AppController;

public class TerminalPrincipal {

	private AppController appController;

	public TerminalPrincipal() {
		super();
		this.appController = new AppController();
	}

	public void exibir() {
		int opcao = 0;
		TerminalUsuario tu = new TerminalUsuario();
		String[] arrayStr = new String[] { "Incluir", "Excluir", "Alterar", "Consultar", "Ordenar", "Relatório",
				"Sair" };
		while (true) {
			opcao = tu.menuOpcoes("MENU PRINCIPAL", arrayStr);
			this.appController.switchOpcao(opcao);
		}
	}
}

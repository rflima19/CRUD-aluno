package main.java.com.view;

import java.util.List;

import main.java.com.controller.AlunoController;
import main.java.com.exceptions.SistemaEscolarException;

public class TerminalOrdenarAlunoView {

	private AlunoController alunoController;

	public TerminalOrdenarAlunoView() {
		super();
		this.alunoController = new AlunoController();
	}

	public void ordenarAluno() {
		TerminalUsuario tu = new TerminalUsuario();
		
		String[] tituloOpcoes = { "MATRICULA", "NOME", "DATA NASCIMENTO" };
		int opcao = tu.menuOpcoes("OPÇÕES DE ORDENACÃO", tituloOpcoes);

		try {
			List<String[]> listaOrdenada = this.alunoController.ordenarAluno(opcao);
			String[] titulos = new String[]{"MATRICULA", "NOME", "DATA NASCIMENTO"};
			tu.imprimirTabela(titulos, listaOrdenada);
		} catch (SistemaEscolarException e) {
			tu.exibirMensagemErro(e);
		}
	}
}

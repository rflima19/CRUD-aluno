package main.java.com.view;

import java.util.List;

import main.java.com.controller.AlunoController;
import main.java.com.exceptions.SistemaEscolarException;

public class TerminalRelatorioAlunoView {

	private AlunoController alunoController;

	public TerminalRelatorioAlunoView() {
		super();
		this.alunoController = new AlunoController();
	}

	public void listarAlunos() {
		TerminalUsuario tu = new TerminalUsuario();
		try {
			List<String[]> lista = this.alunoController.obterAlunos();
			String[] titulos = new String[] { "MATRICULA", "NOME", "DATA NASCIMENTO" };
			tu.imprimirTabela(titulos, lista);
		} catch (SistemaEscolarException e) {
			tu.exibirMensagemErro(e);
		}
	}
}

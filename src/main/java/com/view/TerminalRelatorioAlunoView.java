package main.java.com.view;

import java.util.List;

import main.java.com.exceptions.SistemaEscolarException;
import main.java.com.facade.FacadeConcrete;
import main.java.com.facade.FacadeOfSystem;

public class TerminalRelatorioAlunoView {

	private FacadeOfSystem fachada;

	public TerminalRelatorioAlunoView() {
		super();
		this.fachada = FacadeConcrete.getFacade();
	}

	public void listarAlunos() {
		TerminalUsuario tu = new TerminalUsuario();
		try {
			List<String[]> lista = this.fachada.listarAlunos();
			String[] titulos = new String[] { "MATRICULA", "NOME", "DATA NASCIMENTO" };
			tu.imprimirTabela(titulos, lista);
		} catch (SistemaEscolarException e) {
			tu.exibirMensagemErro(e);
		}
	}
}

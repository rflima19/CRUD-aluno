package main.java.com.view;

import java.util.List;

import main.java.com.exceptions.SistemaEscolarException;
import main.java.com.facade.FacadeConcrete;
import main.java.com.facade.FacadeOfSystem;

public class TerminalOrdenarAlunoView {

	private FacadeOfSystem fachada;

	public TerminalOrdenarAlunoView() {
		super();
		this.fachada = FacadeConcrete.getFacade();
	}

	public void ordenarAluno() {
		TerminalUsuario tu = new TerminalUsuario();
		
		String[] tituloOpcoes = { "MATRICULA", "NOME", "DATA NASCIMENTO" };
		int opcao = tu.menuOpcoes("OPÇÕES DE ORDENAÇÃO", tituloOpcoes);

		try {
			List<String[]> listaOrdenada = this.fachada.ordenarAlunos(opcao);
			String[] titulos = new String[]{"MATRICULA", "NOME", "DATA NASCIMENTO"};
			tu.imprimirTabela(titulos, listaOrdenada);
		} catch (SistemaEscolarException e) {
			tu.exibirMensagemErro(e);
		}
	}
}

package main.java.com.view;

import java.util.ArrayList;
import java.util.List;

import main.java.com.controller.AlunoController;
import main.java.com.exceptions.SistemaEscolarException;
import main.java.com.util.ValidadorMatricula;

public class TerminalExcluirAlunoView {

	private AlunoController alunoController;

	public TerminalExcluirAlunoView() {
		super();
		this.alunoController = new AlunoController();
	}

	public void excluirAluno() {
		TerminalUsuario tu = new TerminalUsuario();
		try {
			List<String[]> lista = this.alunoController.obterAlunos();
			String[] titulos = new String[] { "MATRICULA", "NOME", "DATA NASCIMENTO" };
			tu.imprimirTabela(titulos, lista);
			List<InputUsuario> listInputs = new ArrayList<>();
			listInputs.add(new InputUsuarioNumeroInterio("Digite a matricula do aluno: ", new ValidadorMatricula()));
			Object[] array = tu.formulario("EXCLUIR ALUNO", listInputs);
			int matricula = (int) array[0];
			boolean result = this.alunoController.excluirAluno(matricula);
			if (result == false) {
				tu.exibirMensagem("Aluno não encontrado!");
			} else {
				tu.exibirMensagem("Aluno excluido com sucesso!");
			}
		} catch (SistemaEscolarException e) {
			tu.exibirMensagemErro(e);
		}

	}
}

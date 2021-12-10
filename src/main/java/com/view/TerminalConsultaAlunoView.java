package main.java.com.view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import main.java.com.controller.AlunoController;
import main.java.com.exceptions.SistemaEscolarException;
import main.java.com.facade.FacadeConcrete;
import main.java.com.facade.FacadeOfSystem;
import main.java.com.util.ValidadorDataNascimento;
import main.java.com.util.ValidadorMatricula;
import main.java.com.util.ValidadorNome;

public class TerminalConsultaAlunoView {

	// private AlunoController alunoController;
	private FacadeOfSystem fachada;

	public TerminalConsultaAlunoView() {
		super();
		// this.alunoController = new AlunoController();
		this.fachada = FacadeConcrete.getFacade();
	}

	public void consultarAluno() {
		TerminalUsuario tu = new TerminalUsuario();
		List<String[]> dadosAluno = null;

		String[] tituloOpcoes = { "MATRICULA", "NOME", "DATA NASCIMENTO" };
		int opcao = tu.menuOpcoes("OPÇÕES DE CONSULTA", tituloOpcoes);

		try {
			switch (opcao) {
			case 1 -> {
				dadosAluno = this.consultarPorMatricula(tu);
			}
			case 2 -> {
				dadosAluno = this.consultarPorNome(tu);
			}
			case 3 -> {
				dadosAluno = this.consultarPorDataNascimento(tu);
			}
			}
			if (dadosAluno == null) {
				tu.exibirMensagem("Aluno não encontrado!");
			} else {
				String[] titulos = new String[]{"MATRICULA", "NOME", "DATA NASCIMENTO"};
				tu.imprimirTabela(titulos, dadosAluno);
			}
		} catch (SistemaEscolarException e) {
			tu.exibirMensagemErro(e);
		}
	}

	public List<String[]> consultarPorMatricula(TerminalUsuario tu) throws SistemaEscolarException {
		List<InputUsuario> list = new ArrayList<>();
		list.add(new InputUsuarioNumeroInterio("Digite a matricula do aluno: ", new ValidadorMatricula()));
		Object[] array = tu.formulario("CONSULTAR ALUNO", list);
		int matricula = (int) array[0];
		// return this.alunoController.consultarAluno(matricula);
		return this.fachada.pesquisarAluno(matricula);
	}

	public List<String[]> consultarPorNome(TerminalUsuario tu) throws SistemaEscolarException {
		List<InputUsuario> list = new ArrayList<>();
		list.add(new InputUsuarioString("Digite o nome do aluno: ", new ValidadorNome()));
		Object[] array = tu.formulario("CONSULTAR ALUNO", list);
		String nome = (String) array[0];
		// return this.alunoController.consultarAluno(nome);
		return this.fachada.pesquisarAluno(nome);
	}

	public List<String[]> consultarPorDataNascimento(TerminalUsuario tu) throws SistemaEscolarException {
		List<InputUsuario> list = new ArrayList<>();
		list.add(new InputUsuarioString("Digite a data nascimento(dd/mm/yyyy): ", new ValidadorDataNascimento()));
		Object[] array = tu.formulario("CONSULTAR ALUNO", list);
		String dataNascimentoStr = (String) array[0];
		LocalDate dataNascimento = LocalDate.parse(dataNascimentoStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		// return this.alunoController.consultarAluno(dataNascimento);
		return this.fachada.pesquisarAluno(dataNascimento);
	}
}

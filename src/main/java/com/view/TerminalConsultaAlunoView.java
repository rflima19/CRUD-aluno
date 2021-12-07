package main.java.com.view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.java.com.controller.AlunoController;
import main.java.com.exceptions.SistemaEscolarException;
import main.java.com.util.ValidadorDataNascimento;
import main.java.com.util.ValidadorMatricula;
import main.java.com.util.ValidadorNome;

public class TerminalConsultaAlunoView {

	private AlunoController alunoController;

	public TerminalConsultaAlunoView() {
		super();
		this.alunoController = new AlunoController();
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
//				for (String[] dados : dadosAluno) {
//					StringBuilder strb = new StringBuilder();
//					strb.append("Aluno encontrado:%n");
//					strb.append("Matricula: " + dados[0] + "%n");
//					strb.append("Nome: " + dados[1] + "%n");
//					strb.append("Data Nascimento: " + dados[2]);
//					tu.exibirMensagem(strb.toString());
//				}
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
		return this.alunoController.consultarAluno(matricula);
	}

	public List<String[]> consultarPorNome(TerminalUsuario tu) throws SistemaEscolarException {
		List<InputUsuario> list = new ArrayList<>();
		list.add(new InputUsuarioString("Digite o nome do aluno: ", new ValidadorNome()));
		Object[] array = tu.formulario("CONSULTAR ALUNO", list);
		String nome = (String) array[0];
		return this.alunoController.consultarAluno(nome);
	}

	public List<String[]> consultarPorDataNascimento(TerminalUsuario tu) throws SistemaEscolarException {
		List<InputUsuario> list = new ArrayList<>();
		list.add(new InputUsuarioString("Digite a data nascimento(dd/mm/yyyy): ", new ValidadorDataNascimento()));
		Object[] array = tu.formulario("CONSULTAR ALUNO", list);
		String dataNascimentoStr = (String) array[0];
		LocalDate dataNascimento = LocalDate.parse(dataNascimentoStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		return this.alunoController.consultarAluno(dataNascimento);
	}
}

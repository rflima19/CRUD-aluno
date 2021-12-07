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

public class TerminalCadastroAlunoView {
	
	private AlunoController alunoController;
	
	public TerminalCadastroAlunoView() {
		super();
		this.alunoController = new AlunoController();
	}

	public void salvarAluno() {
		TerminalUsuario tu = new TerminalUsuario();
		List<InputUsuario> list = new ArrayList<>();
		list.add(new InputUsuarioNumeroInterio("Digite a matricula do aluno: ", new ValidadorMatricula()));
		list.add(new InputUsuarioString("Digite o nome do aluno: ", new ValidadorNome()));
		list.add(new InputUsuarioString("Digite a data nascimento(dd/mm/yyyy): ", new ValidadorDataNascimento()));
		Object[] array = tu.formulario("SALVAR ALUNO", list);
		try {
			int matricula = (int) array[0];
			String nome = (String) array[1];
			String dataNascimento = (String) array[2];
			boolean result = this.alunoController.salvarAluno(matricula, nome, dataNascimento);
			if (result == true) {
				tu.exibirMensagem("Aluno " + nome + " salvo com sucesso!");
			} else {
				tu.exibirMensagem("Erro ao salvar aluno " + nome + "!");
			}
		} catch (SistemaEscolarException e) {
			tu.exibirMensagemErro(e);
		}
	}

}

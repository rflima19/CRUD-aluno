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

public class TerminalCadastroAlunoView {
	
	private AlunoController alunoController;
	
	public TerminalCadastroAlunoView() {
		super();
		this.alunoController = new AlunoController();
	}

	public void salvarAluno() {
		TerminalUsuario tu = new TerminalUsuario();
		List<InputUsuario> list = new ArrayList<>();
		list.add(new InputUsuarioNumeroInterio("Digite a matricula do aluno: ", new ValidadorInteiro() {
			@Override
			public boolean test(Integer num) {
				if (((num < 1) == true) || ((num > Integer.MAX_VALUE) == true)) {
					return false;
				}
				return true;
			}
		}));
		list.add(new InputUsuarioString("Digite o nome do aluno: ", new ValidadorString() {	
			@Override
			public boolean test(String str) {
				if ((str.isBlank() == true) || (str.isEmpty() == true)) {
					return false;
				}
				return true;
			}
		}));
		list.add(new InputUsuarioString("Digite a data nascimento(dd/mm/yyyy): ", new ValidadorString() {	
			@Override
			public boolean test(String str) {
				Pattern p = Pattern.compile("\\d{2}/\\d{2}/\\d{4}");
				Matcher m = p.matcher(str);
				if (m.matches() == false) {
					return false;
				}
				try {
					LocalDate date = LocalDate.parse(str, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
				} catch (DateTimeParseException e) {
					return false;
				}
				return true;
			}
		}));
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

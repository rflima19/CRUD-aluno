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

package main.java.com.view;

import java.util.ArrayList;
import java.util.List;

import main.java.com.exceptions.SistemaEscolarException;
import main.java.com.facade.FacadeConcrete;
import main.java.com.facade.FacadeOfSystem;
import main.java.com.util.ValidadorDataNascimento;
import main.java.com.util.ValidadorMatricula;
import main.java.com.util.ValidadorNome;

public class TerminalAlterarAlunoView {

	private FacadeOfSystem fachada;

	public TerminalAlterarAlunoView() {
		super();
		this.fachada = FacadeConcrete.getFacade();
	}

	public void alterarAluno() {
		TerminalUsuario tu = new TerminalUsuario();
		try {
			List<String[]> lista = this.fachada.listarAlunos();
			String[] titulos = new String[] { "MATRICULA", "NOME", "DATA NASCIMENTO" };
			tu.imprimirTabela(titulos, lista);
			List<InputUsuario<Integer>> listInputs = new ArrayList<>();
			listInputs.add(new InputUsuarioNumeroInterio("Digite a matricula do aluno: ", new ValidadorMatricula()));
			Object[] array = tu.formulario("ALTERAR ALUNO", listInputs);
			int matricula = (int) array[0];
			List<String[]> aluno = this.fachada.pesquisarAluno(matricula);
			if (aluno != null) {
				String[] dadosAluno = aluno.get(0);
				String mensagem = String.format("%s%nMatricula: %s%nNome: %s%nData Nascimento: %s%n", 
						"Aluno selecionado:", dadosAluno[0], dadosAluno[1], dadosAluno[2]);
				tu.exibirMensagem(mensagem);
				List<InputUsuario<String>> list = new ArrayList<>();
				list.add(new InputUsuarioString("Digite o nome do aluno: ", new ValidadorNome()));
				list.add(new InputUsuarioString("Digite a data nascimento(dd/mm/yyyy): ", new ValidadorDataNascimento()));
				Object[] novosDadosAluno = tu.formulario("ALTERAR ALUNO", list);
				boolean result = this.fachada.atualizarAluno(matricula, novosDadosAluno);
				if (result == true) {
					tu.exibirMensagem("Aluno alterado com sucesso!");
				} else {
					tu.exibirMensagem("Erro ao alterar aluno!");
				}
			} else {
				tu.exibirMensagem("Aluno não encontrado!");
			}
		} catch (SistemaEscolarException e) {
			tu.exibirMensagemErro(e);
		}

	}
}

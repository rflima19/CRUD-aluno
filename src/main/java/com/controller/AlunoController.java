package main.java.com.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import main.java.com.exceptions.MatriculaInvalidaException;
import main.java.com.exceptions.NomeInvalidoException;
import main.java.com.exceptions.SistemaEscolarException;
import main.java.com.model.Aluno;
import main.java.com.model.OpcaoOrdenacao;

public class AlunoController {

	public boolean salvarAluno(int matricula, String nome, String dataNascimento) throws SistemaEscolarException {
		Aluno aluno = new Aluno(matricula, nome, dataNascimento);
		return aluno.persistir();
	}

	public boolean salvarAluno(int matricula, String nome, LocalDate dataNascimento) throws SistemaEscolarException {
		Aluno aluno = new Aluno(matricula, nome, dataNascimento);
		return aluno.persistir();
	}

	public List<String[]> consultarAluno(int matricula) throws SistemaEscolarException {
		Aluno aluno = Aluno.consultar(matricula);
		if (aluno == null) {
			return null;
		}
		List<Aluno> alunos = new ArrayList<>();
		alunos.add(aluno);
		return this.converterLista(alunos);
	}

	public List<String[]> consultarAluno(String nome) throws SistemaEscolarException {
		List<Aluno> alunos = Aluno.consultar(nome);
		if (alunos == null) {
			return null;
		}
		return this.converterLista(alunos);
	}

	public List<String[]> consultarAluno(LocalDate dataNascimento) throws SistemaEscolarException {
		List<Aluno> alunos = Aluno.consultar(dataNascimento);
		if (alunos == null) {
			return null;
		}
		return this.converterLista(alunos);
	}

	public List<String[]> obterAlunos() throws SistemaEscolarException {
		List<Aluno> arrayAluno = Aluno.obterAlunos();
		return this.converterLista(arrayAluno);
	}

	private List<String[]> converterLista(List<Aluno> alunos) {
		List<String[]> resultAlunos = new ArrayList<>();
		String[] array = null;
		for (Aluno a : alunos) {
			array = new String[3];
			array[0] = Integer.toString(a.getMatricula());
			array[1] = a.getNome();
			array[2] = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(a.getNascimento());
			resultAlunos.add(array);
		}
		return resultAlunos;
	}

	public boolean excluirAluno(int matricula) throws SistemaEscolarException {
		Aluno aluno = Aluno.consultar(matricula);
		if (aluno != null) {
			return aluno.excluir();
		}
		return false;
	}

	public boolean alterarAluno(int matricula, Object[] novosDadosAluno) throws SistemaEscolarException {
		Aluno aluno = Aluno.consultar(matricula);
		if (aluno != null) {
			String nome = (String) novosDadosAluno[0];
			String dataNascimento = (String) novosDadosAluno[1];
			return aluno.alterar(nome, dataNascimento);
		}
		return false;
	}

	public List<String[]> ordenarAluno(int opcao) throws SistemaEscolarException {
		List<Aluno> alunos = null;
		if (opcao == 1) { 
			alunos = Aluno.ordenar(OpcaoOrdenacao.MATRICULA);
		} else if (opcao == 2) {
			alunos = Aluno.ordenar(OpcaoOrdenacao.NOME);
		} else if (opcao == 3) {
			alunos = Aluno.ordenar(OpcaoOrdenacao.DATA_NASCIMENTO);
		} 
		return this.converterLista(alunos);
	}
}

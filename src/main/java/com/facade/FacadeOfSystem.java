package main.java.com.facade;

import java.time.LocalDate;
import java.util.List;

import main.java.com.exceptions.SistemaEscolarException;

/**
 * Fachada do sistema
 * */
public interface FacadeOfSystem {

	public abstract boolean salvarAluno(int matricula, String nome, String dataNascimento) throws SistemaEscolarException;
	public abstract boolean deletarAluno(int matricula) throws SistemaEscolarException;
	public abstract boolean atualizarAluno(int matricula, Object[] dados) throws SistemaEscolarException;
	public abstract List<String[]> listarAlunos() throws SistemaEscolarException;
	public abstract List<String[]> pesquisarAluno(int matricula) throws SistemaEscolarException;
	public abstract List<String[]> pesquisarAluno(String nome) throws SistemaEscolarException;
	public abstract List<String[]> pesquisarAluno(LocalDate dataNascimento) throws SistemaEscolarException;
	public abstract List<String[]> ordenarAlunos(int opcaoOrdenacao) throws SistemaEscolarException;
	
	public abstract void salvarProfessor();
	public abstract void deletarProfessor();
	public abstract void atualizarProfessor();
	public abstract void listarProfessores();
	public abstract void pesquisarProfessor();
	
	public void switchOpcao(int opcao);
}

package main.java.com.facade;

import main.java.com.exceptions.SistemaEscolarException;

/**
 * Fachada do sistema
 * */
public interface FacadeOfSystem {

	public abstract boolean salvarAluno(int matricula, String nome, String dataNascimento) throws SistemaEscolarException;
	public abstract void deletarAluno();
	public abstract void atualizarAluno();
	public abstract void listarAluno();
	public abstract void pesquisarAluno();
	
	public abstract void salvarProfessor();
	public abstract void deletarProfessor();
	public abstract void atualizarProfessor();
	public abstract void listarProfessor();
	public abstract void pesquisarProfessor();
}

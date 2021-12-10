package main.java.com.facade;

import main.java.com.controller.AlunoController;
import main.java.com.controller.ProfessorController;
import main.java.com.exceptions.SistemaEscolarException;

/**
 * Segue o padrão singleton
 * */
public class FacadeConcrete implements FacadeOfSystem {

	private static FacadeConcrete facade = null;
	
	private AlunoController alunocontroller;
	private ProfessorController professorController;
	
	private FacadeConcrete() {
		this.alunocontroller = new AlunoController();
		this.professorController = new ProfessorController();
	}
	
	public static FacadeConcrete getFacade() {
		if (FacadeConcrete.facade == null) {
			FacadeConcrete.facade = new FacadeConcrete();
		}
		return FacadeConcrete.facade;
	}
	
	@Override
	public boolean salvarAluno(int matricula, String nome, String dataNascimento) throws SistemaEscolarException {
		return this.alunocontroller.salvarAluno(matricula, nome, dataNascimento);
	}

	@Override
	public void deletarAluno() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void atualizarAluno() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void listarAluno() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pesquisarAluno() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void salvarProfessor() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deletarProfessor() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void atualizarProfessor() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void listarProfessor() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pesquisarProfessor() {
		// TODO Auto-generated method stub
		
	}

}

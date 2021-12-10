package main.java.com.facade;

import java.time.LocalDate;
import java.util.List;

import main.java.com.controller.AlunoController;
import main.java.com.controller.AppController;
import main.java.com.controller.ProfessorController;
import main.java.com.exceptions.SistemaEscolarException;

/**
 * Segue o padrão singleton
 * */
public class FacadeConcrete implements FacadeOfSystem {

	private static FacadeConcrete facade = null;
	
	private AppController appController;
	private AlunoController alunoController;
	private ProfessorController professorController;
	
	private FacadeConcrete() {
		this.appController = new AppController();
		this.alunoController = new AlunoController();
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
		return this.alunoController.salvarAluno(matricula, nome, dataNascimento);
	}

	@Override
	public boolean deletarAluno(int matricula) throws SistemaEscolarException {
		return this.alunoController.excluirAluno(matricula);
	}

	@Override
	public boolean atualizarAluno(int matricula, Object[] dados) throws SistemaEscolarException {
		return this.alunoController.alterarAluno(matricula, dados);
	}

	@Override
	public List<String[]> listarAlunos() throws SistemaEscolarException {
		return this.alunoController.listarAlunos();
	}

	@Override
	public List<String[]> pesquisarAluno(int matricula) throws SistemaEscolarException {
		return this.alunoController.consultarAluno(matricula);
	}

	@Override
	public List<String[]> pesquisarAluno(String nome) throws SistemaEscolarException {
		return this.alunoController.consultarAluno(nome);
	}

	@Override
	public List<String[]> pesquisarAluno(LocalDate dataNascimento) throws SistemaEscolarException {
		return this.alunoController.consultarAluno(dataNascimento);
	}

	@Override
	public List<String[]> ordenarAlunos(int opcaoOrdenacao) throws SistemaEscolarException {
		return this.alunoController.ordenarAluno(opcaoOrdenacao);
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
	public void listarProfessores() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pesquisarProfessor() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void switchOpcao(int opcao) {
		this.appController.switchOpcao(opcao);
	}
}

package main.java.com.DAO;

import java.time.LocalDate;
import java.util.List;

import main.java.com.exceptions.SistemaEscolarException;
import main.java.com.model.Aluno;

public interface AlunoDAO {

		public abstract boolean salvar(Aluno aluno) throws SistemaEscolarException;
		
		public abstract boolean excluir(Aluno aluno) throws SistemaEscolarException;
		
		public abstract Aluno consultar(int matricula) throws SistemaEscolarException;
		
		public abstract List<Aluno> consultar(String nome) throws SistemaEscolarException;
		
		public abstract List<Aluno> consultar(LocalDate dataNascimento) throws SistemaEscolarException;
		
		public abstract List<Aluno> listar() throws SistemaEscolarException;
		
		public abstract List<Aluno> ordenarPorMatricula() throws SistemaEscolarException;
		
		public abstract List<Aluno> ordenarPorNome() throws SistemaEscolarException;

		public abstract List<Aluno> ordenarPorDataNascimento() throws SistemaEscolarException;
		
		public abstract boolean atualizar(Aluno aluno, String novoNome, LocalDate novaDataNascimento) throws SistemaEscolarException;
	
}

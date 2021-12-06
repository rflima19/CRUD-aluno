package main.java.com.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import main.java.com.exceptions.MatriculaInvalidaException;
import main.java.com.exceptions.NomeInvalidoException;
import main.java.com.exceptions.SistemaEscolarException;
import main.java.com.model.Aluno;

public class AlunoController {

	public boolean salvarAluno(int matricula, String nome, String dataNascimento) 
			throws SistemaEscolarException {
		Aluno aluno = new Aluno(matricula, nome, dataNascimento);
		return aluno.persistir();
	}
	
	public boolean salvarAluno(int matricula, String nome, LocalDate dataNascimento) 
			throws SistemaEscolarException {
		Aluno aluno = new Aluno(matricula, nome, dataNascimento);
		return aluno.persistir();
	}
	
	public ArrayList<String[]> obterAlunos() {
		ArrayList<String[]> arrayStr = new ArrayList<>();
		ArrayList<Aluno> arrayAluno = Aluno.obterAlunos();
		String[] aux = null;
		for (Aluno aluno : arrayAluno) {
			aux = new String[3];
			aux[0] = Integer.toString(aluno.getMatricula());
			aux[1] = aluno.getNome();
			aux[2] = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(aluno.getNascimento());
			arrayStr.add(aux);
		}
		return arrayStr;
	}
}

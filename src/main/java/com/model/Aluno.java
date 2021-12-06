package main.java.com.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

import main.java.com.DAO.AlunoFileDAO;
import main.java.com.exceptions.MatriculaInvalidaException;
import main.java.com.exceptions.NomeInvalidoException;
import main.java.com.exceptions.SistemaEscolarException;

public class Aluno implements Comparable<Aluno> {
	
	private AlunoFileDAO alunoDAO;

	private int matricula;
	private String nome;
	private LocalDate nascimento;
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	public Aluno(int matricula, String nome, LocalDate nascimento) 
			throws SistemaEscolarException {
		super();
		this.setMatricula(matricula);
		this.setNome(nome);
		this.setNascimento(nascimento);
		this.alunoDAO = new AlunoFileDAO();
	}
	
	public Aluno(int matricula, String nome, String nascimento) 
			throws SistemaEscolarException {
		this(matricula, nome, LocalDate.parse(nascimento, Aluno.FORMATTER));
		this.alunoDAO = new AlunoFileDAO();
	}

	public int getMatricula() {
		return matricula;
	}

	public void setMatricula(int matricula) throws SistemaEscolarException {
		if (matricula < 0) {
			throw new SistemaEscolarException("Número de Matricula Invalida: " + matricula, 
					new MatriculaInvalidaException(matricula, "Número de Matricula Invalida: " + matricula));
		}
		this.matricula = matricula;
	}
	
	public void setMatricula(String matricula) throws SistemaEscolarException {
		int mat = Integer.valueOf(matricula);
		this.setMatricula(mat);
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) throws SistemaEscolarException {
		if ((nome.isBlank() == true) || (nome.isEmpty() == true) ||
				(nome.matches("[^a-zA-Z]+") == true)) {
			throw new SistemaEscolarException("Nome Invalido: "  + nome, 
					new NomeInvalidoException(nome, "Nome invalido: " + nome));
		}
		this.nome = nome;
	}

	public LocalDate getNascimento() {
		return nascimento;
	}

	public void setNascimento(LocalDate nascimento) {
		if (nascimento == null) {
			throw new IllegalArgumentException("Argumento data de nascimento nulo");
		}
		this.nascimento = nascimento;
	}
	
	public void setNascimento(String nascimento) {
		LocalDate data = LocalDate.parse(nascimento, Aluno.FORMATTER);
		this.setNascimento(data);
	}

	@Override
	public String toString() {
		return String.format("[matricula=%d, nome=%s, nascimento=%s]", this.matricula, this.nome, 
				DateTimeFormatter.ofPattern("dd/MM/yyyy").format(this.nascimento));
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(matricula);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Aluno other = (Aluno) obj;
		return matricula == other.matricula;
	}
	
	@Override
	public int compareTo(Aluno o) {
		return Integer.compare(this.matricula, o.getMatricula());
	}
	
	public boolean persistir() throws SistemaEscolarException {
		return this.alunoDAO.salvarAlunoFileBinario(this);
	}
	
	public static ArrayList<Aluno> obterAlunos() {
		ArrayList<Aluno> alunos = new ArrayList<>();
		return alunos;
	}

}

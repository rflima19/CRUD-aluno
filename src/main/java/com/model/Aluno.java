package main.java.com.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import main.java.com.DAO.AlunoFileDAO;
import main.java.com.exceptions.ArquivoVazioException;
import main.java.com.exceptions.MatriculaDuplicadaException;
import main.java.com.exceptions.MatriculaInvalidaException;
import main.java.com.exceptions.NomeInvalidoException;
import main.java.com.exceptions.SistemaEscolarException;

public class Aluno implements Comparable<Aluno> {

	private static AlunoFileDAO alunoDAO = new AlunoFileDAO();

	private int matricula;
	private String nome;
	private LocalDate nascimento;
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	public Aluno(int matricula, String nome, LocalDate nascimento) throws SistemaEscolarException {
		super();
		this.setMatricula(matricula);
		this.setNome(nome);
		this.setNascimento(nascimento);
	}

	public Aluno(int matricula, String nome, String nascimento) throws SistemaEscolarException {
		this(matricula, nome, LocalDate.parse(nascimento, Aluno.FORMATTER));
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
		if ((nome.isBlank() == true) || (nome.isEmpty() == true) || (nome.matches("[^a-zA-Z]+") == true)) {
			throw new SistemaEscolarException("Nome Invalido: " + nome,
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
		Aluno aluno = this.alunoDAO.consultaFileBinario(this.matricula);
		// Aluno aluno = this.alunoDAO.consultaFileTexto(this.matricula);
		if (aluno != null) {
			throw new SistemaEscolarException("Matricula " + this.matricula + " já cadastrada no sistema",
					new MatriculaDuplicadaException(this.matricula,
							"Matricula " + this.matricula + " já cadastrado no sistema"));
		}
		return this.alunoDAO.salvarAlunoFileBinario(this);
		// return Aluno.alunoDAO.salvarAlunoFileTexto(this);
	}

	public static Aluno consultar(int matricula) throws SistemaEscolarException {
//		Aluno aluno = Aluno.alunoDAO.consultaFileBinario(matricula);
//		// List<Aluno> alunos = Aluno.alunoDAO.consultaFileTexto(matricula);
//		if (aluno == null) {
//			throw new SistemaEscolarException("Base de dados vazia!",
//					new ArquivoVazioException("Base de dados vazia!"));
//		}
//		return aluno;
		
		return Aluno.alunoDAO.consultaFileBinario(matricula);
		// return Aluno.alunoDAO.consultaFileTexto(matricula);
	}

	public static List<Aluno> consultar(String nome) throws SistemaEscolarException {
//		List<Aluno> alunos = Aluno.alunoDAO.consultaFileBinario(nome);
//		// List<Aluno> alunos = Aluno.alunoDAO.consultaFileTexto(nome);
//		if (alunos == null) {
//			throw new SistemaEscolarException("Base de dados vazia!",
//					new ArquivoVazioException("Base de dados vazia!"));
//		}
//		return alunos;
		
		return Aluno.alunoDAO.consultaFileBinario(nome);
		// return Aluno.alunoDAO.consultaFileTexto(nome);
	}

	public static List<Aluno> consultar(LocalDate dataNascimento) throws SistemaEscolarException {
//		List<Aluno> alunos = Aluno.alunoDAO.consultaFileBinario(dataNascimento);
//		// List<Aluno> alunos = Aluno.alunoDAO.consultaFileTexto(dataNascimento);
//		if (alunos == null) {
//			throw new SistemaEscolarException("Base de dados vazia!",
//					new ArquivoVazioException("Base de dados vazia!"));
//		}
//		return alunos;
//		
		return Aluno.alunoDAO.consultaFileBinario(dataNascimento);
		// return Aluno.alunoDAO.consultaFileTexto(dataNascimento);
	}

	public static List<Aluno> obterAlunos() throws SistemaEscolarException {
		List<Aluno> alunos = Aluno.alunoDAO.obterAlunosFileBinario();
		// List<Aluno> alunos = Aluno.alunoDAO.obterAlunosFileTexto();
		if (alunos == null) {
			throw new SistemaEscolarException("Base de dados vazia!",
					new ArquivoVazioException("Base de dados vazia!"));
		}
		return alunos;
	}

	public boolean excluir() throws SistemaEscolarException {
		return alunoDAO.excluirFileBinairo(this);
	}
	
	public boolean alterar(String nome, String dataNascimento) throws SistemaEscolarException {
		return alunoDAO.alterarFileBinario(this.matricula, nome, LocalDate.parse(dataNascimento, Aluno.FORMATTER));
	}
	
	public static List<Aluno> ordenar(OpcaoOrdenacao opcao) throws SistemaEscolarException {
		List<Aluno> list = null;
		if (OpcaoOrdenacao.MATRICULA.equals(opcao)) {
			list = alunoDAO.ordenarPorMatriculaFileBinario();
		} else if (OpcaoOrdenacao.NOME.equals(opcao)) {
			list = alunoDAO.ordenarPorNomeFileBinario();
		} else if (OpcaoOrdenacao.DATA_NASCIMENTO.equals(opcao)) {
			list = alunoDAO.ordenarPorDataNascimentoFileBinario();
		}
		if (list == null) {
			throw new SistemaEscolarException("Base de dados vazia!",
					new ArquivoVazioException("Base de dados vazia!"));
		}
		return list;
	}

}

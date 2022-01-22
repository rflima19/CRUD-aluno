package main.java.com.DAO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import main.java.com.exceptions.SistemaEscolarException;
import main.java.com.model.Aluno;
import main.java.com.util.AlunoDataNascimentoComparator;
import main.java.com.util.AlunoMatriculaComparator;
import main.java.com.util.AlunoNomeComparator;

public class AlunoDataBaseDAO implements AlunoDAO {

	private Properties props;
	private String driverClass;
	private String url;
	private String user;
	private String password;

	public AlunoDataBaseDAO() {
		super();
		Path arquivoProps = Paths.get("database.properties");
		try {
			this.props = new Properties();
			this.props.load(Files.newInputStream(arquivoProps, StandardOpenOption.READ));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		this.driverClass = this.props.getProperty("jdbc.driver");
		this.url = this.props.getProperty("jdbc.url");
		this.user = this.props.getProperty("jdbc.user");
		this.password = this.props.getProperty("jdbc.pass");
		try {
			Class.forName(this.driverClass);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	@Override
	public boolean salvar(Aluno aluno) throws SistemaEscolarException {
		String sql = "INSERT INTO aluno (matricula, nome, dataNascimento) VALUES (?, ?, ?);";
		try (Connection conn = DriverManager.getConnection(this.url, this.user, this.password)) {
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setInt(1, aluno.getMatricula());
				stmt.setString(2, aluno.getNome());
				stmt.setDate(3, Date.valueOf(aluno.getNascimento()));
				stmt.executeUpdate();
			}
		} catch (SQLException e) {
			throw new SistemaEscolarException("Falha ao salvar aluno", e);
		}
		return true;
	}

	@Override
	public boolean excluir(Aluno aluno) throws SistemaEscolarException {
		String sql = "DELETE FROM aluno WHERE aluno.matricula = ?;";
		try (Connection conn = DriverManager.getConnection(this.url, this.user, this.password)) {
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setInt(1, aluno.getMatricula());
				stmt.executeUpdate();
			}
		} catch (SQLException e) {
			throw new SistemaEscolarException("Falha ao excluir aluno", e);
		}
		return true;
	}

	@Override
	public Aluno consultar(int matricula) throws SistemaEscolarException {
		Aluno aluno = null;
		String sql = "SELECT matricula, nome, dataNascimento FROM aluno WHERE aluno.matricula = ?;";
		try (Connection conn = DriverManager.getConnection(this.url, this.user, this.password)) {
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setInt(1, matricula);
				try (ResultSet rs = stmt.executeQuery()) {
					if (rs.next() == true) {
						aluno = new Aluno(rs.getInt(1), rs.getString(2), rs.getDate(3).toLocalDate());
					}
				}
			}
		} catch (SQLException e) {
			throw new SistemaEscolarException("Falha ao consultar aluno", e);
		}
		return aluno;
	}

	@Override
	public List<Aluno> consultar(String nome) throws SistemaEscolarException {
		List<Aluno> alunos = new ArrayList<>();
		String sql = "SELECT matricula, nome, dataNascimento FROM aluno WHERE aluno.nome LIKE ?;";
		try (Connection conn = DriverManager.getConnection(this.url, this.user, this.password)) {
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setString(1, "%" + nome + "%");
				try (ResultSet rs = stmt.executeQuery()) {
					while (rs.next() == true) {
						Aluno aluno = new Aluno(rs.getInt(1), rs.getString(2), rs.getDate(3).toLocalDate());
						alunos.add(aluno);
					}
				}
			}
		} catch (SQLException e) {
			throw new SistemaEscolarException("Falha ao consultar aluno", e);
		}
		return alunos;
	}

	@Override
	public List<Aluno> consultar(LocalDate dataNascimento) throws SistemaEscolarException {
		List<Aluno> alunos = new ArrayList<>();
		String sql = "SELECT matricula, nome, dataNascimento FROM aluno WHERE aluno.dataNascimento = ?;";
		try (Connection conn = DriverManager.getConnection(this.url, this.user, this.password)) {
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setDate(1, Date.valueOf(dataNascimento));
				try (ResultSet rs = stmt.executeQuery()) {
					while (rs.next() == true) {
						Aluno aluno = new Aluno(rs.getInt(1), rs.getString(2), rs.getDate(3).toLocalDate());
						alunos.add(aluno);
					}
				}
			}
		} catch (SQLException e) {
			throw new SistemaEscolarException("Falha ao consultar aluno", e);
		}
		return alunos;
	}

	@Override
	public List<Aluno> listar() throws SistemaEscolarException {
		List<Aluno> alunos = new ArrayList<>();
		String sql = "SELECT matricula, nome, dataNascimento FROM aluno;";
		try (Connection conn = DriverManager.getConnection(this.url, this.user, this.password)) {
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				try (ResultSet rs = stmt.executeQuery()) {
					while (rs.next() == true) {
						Aluno aluno = new Aluno(rs.getInt(1), rs.getString(2), rs.getDate(3).toLocalDate());
						alunos.add(aluno);
					}
				}
			}
		} catch (SQLException e) {
			throw new SistemaEscolarException("Falha ao listar aluno", e);
		}
		return alunos;
	}

	public synchronized List<Aluno> ordenarPorMatricula() throws SistemaEscolarException {
		List<Aluno> alunos = this.listar();
		Collections.sort(alunos, new AlunoMatriculaComparator());
		return alunos;
	}

	public synchronized List<Aluno> ordenarPorNome() throws SistemaEscolarException {
		List<Aluno> alunos = this.listar();
		Collections.sort(alunos, new AlunoNomeComparator());
		return alunos;
	}

	public synchronized List<Aluno> ordenarPorDataNascimento() throws SistemaEscolarException {
		List<Aluno> alunos = this.listar();
		Collections.sort(alunos, new AlunoDataNascimentoComparator());
		return alunos;
	}

	@Override
	public boolean atualizar(Aluno aluno, String novoNome, LocalDate novaDataNascimento)
			throws SistemaEscolarException {
		String sql = "UPDATE aluno SET aluno.nome = ?, aluno.dataNascimento = ? " +
						"WHERE aluno.matricula = ?;";
		try (Connection conn = DriverManager.getConnection(this.url, this.user, this.password)) {
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setInt(3, aluno.getMatricula());
				stmt.setString(1, novoNome);
				stmt.setDate(2, Date.valueOf(novaDataNascimento));
				stmt.executeUpdate();
			}
		} catch (SQLException e) {
			throw new SistemaEscolarException("Falha ao salvar aluno", e);
		}
		return true;
	}

}

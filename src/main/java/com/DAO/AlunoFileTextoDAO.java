package main.java.com.DAO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import main.java.com.exceptions.SistemaEscolarException;
import main.java.com.model.Aluno;
import main.java.com.util.AlunoDataNascimentoComparator;
import main.java.com.util.AlunoMatriculaComparator;
import main.java.com.util.AlunoNomeComparator;

public class AlunoFileTextoDAO implements AlunoDAO {

	public static final String SEPARATOR = FileSystems.getDefault().getSeparator();
	public static final Path DIRECTORY = Paths.get("files" + AlunoFileBinarioDAO.SEPARATOR);
	public static final Path FILE = AlunoFileBinarioDAO.DIRECTORY.resolve("alunos.txt");
	public static final String SEPARADOR = ";";

	public AlunoFileTextoDAO() {
		super();
	}

	public synchronized boolean salvar(Aluno aluno) throws SistemaEscolarException {
		if (Files.exists(AlunoFileTextoDAO.DIRECTORY) == false) {
			try {
				Files.createDirectory(AlunoFileTextoDAO.DIRECTORY);
			} catch (IOException e) {
				throw new SistemaEscolarException(
						"Falha ao criar diretório", e);
			}
		}
		try (Writer writer = Files.newBufferedWriter(AlunoFileTextoDAO.FILE, Charset.defaultCharset(), StandardOpenOption.CREATE,
				StandardOpenOption.WRITE, StandardOpenOption.APPEND); PrintWriter pw = new PrintWriter(writer)) {
			String line = this.converterAluno(aluno);
			pw.println(line);
		} catch (IOException e) {
			throw new SistemaEscolarException(
					"Falha ao abrir o arquivo " + AlunoFileTextoDAO.FILE.toAbsolutePath() + " para escrita", e);
		}
		return true;
	}

	public synchronized boolean excluir(Aluno aluno) throws SistemaEscolarException {
		if (Files.exists(AlunoFileTextoDAO.FILE) == false) {
			throw new SistemaEscolarException("Base de dados inexistente");
		}
		Path arquivoTemporario = null;
		try {
			arquivoTemporario = Files.createTempFile("alunos", "temp");
		} catch (IOException e) {
			throw new SistemaEscolarException(
					"Não foi possivel criar o arquivo temporário para realizar operação de exclusão", e);
		}
		try (Reader reader = Files.newBufferedReader(AlunoFileTextoDAO.FILE, Charset.defaultCharset());
				BufferedReader buffer = new BufferedReader(reader)) {
			try (Writer writer = Files.newBufferedWriter(arquivoTemporario, StandardOpenOption.WRITE);
					PrintWriter pw = new PrintWriter(writer)) {
				String line = null;
				while ((line = buffer.readLine()) != null) {
					Aluno a = this.desconverterAluno(line);
					if (a.equals(aluno) == false) {
						String s = this.converterAluno(a);
						pw.println(s);
					}
				}
			} catch (IOException e) {
				throw new SistemaEscolarException("Falha ao escrever no arquivo " + arquivoTemporario.toAbsolutePath(),
						e);
			}
		} catch (IOException e) {
			throw new SistemaEscolarException(
					"Falha ao abrir o arquivo " + AlunoFileTextoDAO.FILE.toAbsolutePath() + " para leitura", e);
		}

		try {
			Files.copy(arquivoTemporario, AlunoFileTextoDAO.FILE, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new SistemaEscolarException("Falha ao copiar do arquivo temporário", e);
		}

		return true;
	}

	public synchronized Aluno consultar(int matricula) throws SistemaEscolarException {
		if (Files.exists(AlunoFileTextoDAO.FILE) == false) {
			throw new SistemaEscolarException("Base de dados inexistente");
		}
		Aluno aluno = null;
		try (Reader reader = Files.newBufferedReader(AlunoFileTextoDAO.FILE, Charset.defaultCharset());
				BufferedReader buffer = new BufferedReader(reader)) {
			String line = null;
			while ((line = buffer.readLine()) != null) {
				Aluno a = this.desconverterAluno(line);
				if (a.getMatricula() == matricula) {
					aluno = a;
					break;
				}
			}
		} catch (IOException e) {
			throw new SistemaEscolarException(
					"Falha ao abrir o arquivo " + AlunoFileTextoDAO.FILE.toAbsolutePath() + " para leitura", e);
		}
		return aluno;
	}

	public synchronized List<Aluno> consultar(String nome) throws SistemaEscolarException {
		if (Files.exists(AlunoFileTextoDAO.FILE) == false) {
			throw new SistemaEscolarException("Base de dados inexistente");
		}
		List<Aluno> alunos = new ArrayList<>();
		try (Reader reader = Files.newBufferedReader(AlunoFileTextoDAO.FILE, Charset.defaultCharset());
				BufferedReader buffer = new BufferedReader(reader)) {
			String line = null;
			while ((line = buffer.readLine()) != null) {
				Aluno aluno = this.desconverterAluno(line);
				if (Pattern.matches(".*" + nome + ".*", aluno.getNome()) == true) {
					alunos.add(aluno);
				}
			}
		} catch (IOException e) {
			throw new SistemaEscolarException(
					"Falha ao abrir o arquivo " + AlunoFileTextoDAO.FILE.toAbsolutePath() + " para leitura", e);
		}
		return alunos;
	}

	public synchronized List<Aluno> consultar(LocalDate dataNascimento) throws SistemaEscolarException {
		if (Files.exists(AlunoFileTextoDAO.FILE) == false) {
			throw new SistemaEscolarException("Base de dados inexistente");
		}
		List<Aluno> alunos = new ArrayList<>();
		try (Reader reader = Files.newBufferedReader(AlunoFileTextoDAO.FILE, Charset.defaultCharset());
				BufferedReader buffer = new BufferedReader(reader)) {
			String line = null;
			while ((line = buffer.readLine()) != null) {
				Aluno aluno = this.desconverterAluno(line);
				if (aluno.getNascimento().equals(dataNascimento) == true) {
					alunos.add(aluno);
				}
			}
		} catch (IOException e) {
			throw new SistemaEscolarException(
					"Falha ao abrir o arquivo " + AlunoFileTextoDAO.FILE.toAbsolutePath() + " para leitura", e);
		}
		return alunos;
	}

	public synchronized List<Aluno> listar() throws SistemaEscolarException {
		if (Files.exists(AlunoFileTextoDAO.FILE) == false) {
			throw new SistemaEscolarException("Base de dados inexistente");
		}
		List<Aluno> alunos = new ArrayList<>();
		try (Reader reader = Files.newBufferedReader(AlunoFileTextoDAO.FILE, Charset.defaultCharset());
				BufferedReader buffer = new BufferedReader(reader)) {
			String line = null;
			while ((line = buffer.readLine()) != null) {
				Aluno aluno = this.desconverterAluno(line);
				alunos.add(aluno);
			}
		} catch (IOException e) {
			throw new SistemaEscolarException(
					"Falha ao abrir o arquivo " + AlunoFileTextoDAO.FILE.toAbsolutePath() + " para leitura", e);
		}
		return alunos;
	}

	public synchronized List<Aluno> ordenarPorMatricula() throws SistemaEscolarException {
		List<Aluno> alunos = this.listar();
//		if (alunos.size() == 0) {
//			return null;
//		}
		Collections.sort(alunos, new AlunoMatriculaComparator());
		return alunos;
	}

	public synchronized List<Aluno> ordenarPorNome() throws SistemaEscolarException {
		List<Aluno> alunos = this.listar();
//		if (alunos.size() == 0) {
//			return null;
//		}
		Collections.sort(alunos, new AlunoNomeComparator());
		return alunos;
	}

	public synchronized List<Aluno> ordenarPorDataNascimento() throws SistemaEscolarException {
		List<Aluno> alunos = this.listar();
//		if (alunos.size() == 0) {
//			return null;
//		}
		Collections.sort(alunos, new AlunoDataNascimentoComparator());
		return alunos;
	}

	public synchronized boolean atualizar(Aluno aluno, String novoNome, LocalDate novaDataNascimento)
			throws SistemaEscolarException {
		if (Files.exists(AlunoFileTextoDAO.FILE) == false) {
			throw new SistemaEscolarException("Base de dados inexistente");
		}
		Path arquivoTemporario = null;
		try {
			arquivoTemporario = Files.createTempFile("alunos", "temp");
		} catch (IOException e) {
			throw new SistemaEscolarException(
					"Não foi possivel criar o arquivo temporário para realizar operação de exclusão", e);
		}
		try (Reader reader = Files.newBufferedReader(AlunoFileTextoDAO.FILE, Charset.defaultCharset());
				BufferedReader buffer = new BufferedReader(reader)) {
			try (Writer writer = Files.newBufferedWriter(arquivoTemporario, StandardOpenOption.WRITE);
					PrintWriter pw = new PrintWriter(writer)) {
				aluno.setNome(novoNome);
				aluno.setNascimento(novaDataNascimento);
				String line = null;
				String s = null;
				while ((line = buffer.readLine()) != null) {
					Aluno a = this.desconverterAluno(line);
					if (a.equals(aluno) == true) {
						s = this.converterAluno(aluno);
					} else {
						s = this.converterAluno(a);
					}
					pw.println(s);
				}
			} catch (IOException e) {
				throw new SistemaEscolarException("Falha ao escrever no arquivo " + arquivoTemporario.toAbsolutePath(),
						e);
			}
		} catch (IOException e) {
			throw new SistemaEscolarException(
					"Falha ao abrir o arquivo " + AlunoFileTextoDAO.FILE.toAbsolutePath() + " para leitura", e);
		}

		try {
			Files.copy(arquivoTemporario, AlunoFileTextoDAO.FILE, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new SistemaEscolarException("Falha ao copiar do arquivo temporário", e);
		}
		return true;
	}

	private String converterAluno(Aluno aluno) {
		StringBuilder strb = new StringBuilder();
		strb.append(aluno.getMatricula());
		strb.append(AlunoFileTextoDAO.SEPARADOR);
		strb.append(aluno.getNome());
		strb.append(AlunoFileTextoDAO.SEPARADOR);
		strb.append(aluno.getNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		return strb.toString();
	}

	private Aluno desconverterAluno(String srt) throws SistemaEscolarException {
		String[] tokens = srt.split(AlunoFileTextoDAO.SEPARADOR);
		int matricula = Integer.parseInt(tokens[0]);
		String nome = tokens[1];
		String dataNascimento = tokens[2];
		Aluno a = new Aluno(matricula, nome, dataNascimento);
		return a;
	}
}

package main.java.com.DAO;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

public class AlunoFileBinarioDAO implements AlunoDAO {

	public static final String SEPARATOR = FileSystems.getDefault().getSeparator();
	public static final Path DIRECTORY = Paths.get("files" + AlunoFileBinarioDAO.SEPARATOR);
	public static final Path FILE = AlunoFileBinarioDAO.DIRECTORY.resolve("alunos.bin");
	public static final int BYTES_REGISTRO = 124;

	public AlunoFileBinarioDAO() {
		super();
	}

	/*
	 * Cada registro salvo de aluno consome 124 bytes do arquivo
	 */
	public synchronized boolean salvar(Aluno aluno) throws SistemaEscolarException {
		try (OutputStream outBytes = Files.newOutputStream(AlunoFileBinarioDAO.FILE, StandardOpenOption.CREATE,
				StandardOpenOption.WRITE, StandardOpenOption.APPEND);
				DataOutputStream outData = new DataOutputStream(outBytes)) {
			outData.writeInt(aluno.getMatricula());
			StringBuilder strb = new StringBuilder(aluno.getNome());
			strb.setLength(50);
			outData.writeChars(strb.toString());
			String dataNascimento = aluno.getNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			strb = new StringBuilder(dataNascimento);
			strb.setLength(10);
			outData.writeChars(strb.toString());
		} catch (IOException e) {
			throw new SistemaEscolarException(
					"Falha ao abrir o arquivo " + AlunoFileBinarioDAO.FILE.toAbsolutePath() + " para escrita", e);
		}
		return true;
	}

	public synchronized boolean excluir(Aluno aluno) throws SistemaEscolarException {
		if (Files.exists(AlunoFileBinarioDAO.FILE) == false) {
			throw new SistemaEscolarException("Base de dados inexistente");
		}
		Path arquivoTemporario = null;
		try {
			arquivoTemporario = Files.createTempFile("alunos", "temp");
		} catch (IOException e) {
			throw new SistemaEscolarException(
					"Não foi possivel criar o arquivo temporário para realizar operação de exclusão", e);
		}

		try (InputStream inBytes = Files.newInputStream(AlunoFileBinarioDAO.FILE, StandardOpenOption.READ);
				DataInputStream inData = new DataInputStream(inBytes)) {
			long tamanhoArquivo = Files.size(AlunoFileBinarioDAO.FILE);
			long numRegistros = tamanhoArquivo / AlunoFileBinarioDAO.BYTES_REGISTRO;
			try (OutputStream outBytes = Files.newOutputStream(arquivoTemporario, StandardOpenOption.WRITE);
					DataOutputStream outData = new DataOutputStream(outBytes)) {
				for (int i = 0; i < numRegistros; i++) {
					int matricula = inData.readInt();
					char[] caracteres = new char[50];
					for (int j = 0; j < caracteres.length; j++) {
						caracteres[j] = inData.readChar();
					}
					String nome = new String(caracteres);
					caracteres = new char[10];
					for (int j = 0; j < caracteres.length; j++) {
						caracteres[j] = inData.readChar();
					}
					String dataNascimento = new String(caracteres);
					Aluno a = new Aluno(matricula, nome.trim(), dataNascimento);
					if (a.equals(aluno) == false) {
						outData.writeInt(a.getMatricula());
						StringBuilder strb = new StringBuilder(a.getNome());
						strb.setLength(50);
						outData.writeChars(strb.toString());
						String dn = a.getNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
						strb = new StringBuilder(dn);
						strb.setLength(10);
						outData.writeChars(strb.toString());
					}
				}
			} catch (IOException e) {
				throw new SistemaEscolarException("Falha ao escrever no arquivo " + arquivoTemporario.toAbsolutePath(),
						e);
			}
		} catch (IOException e) {
			throw new SistemaEscolarException(
					"Falha ao abrir o arquivo " + AlunoFileBinarioDAO.FILE.toAbsolutePath() + " para leitura", e);
		}

		try {
			Files.copy(arquivoTemporario, AlunoFileBinarioDAO.FILE, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
			throw new SistemaEscolarException("Falha ao copiar do arquivo temporário", e);
		}

		return true;
	}

	public synchronized Aluno consultar(int matricula) throws SistemaEscolarException {
		if (Files.exists(AlunoFileBinarioDAO.FILE) == false) {
			throw new SistemaEscolarException("Base de dados inexistente");
		}
		Aluno aluno = null;
		try (InputStream inBytes = Files.newInputStream(AlunoFileBinarioDAO.FILE, StandardOpenOption.READ);
				DataInputStream inData = new DataInputStream(inBytes)) {
			long tamanhoArquivo = Files.size(AlunoFileBinarioDAO.FILE);
			long numRegistros = tamanhoArquivo / AlunoFileBinarioDAO.BYTES_REGISTRO;
			for (int i = 0; i < numRegistros; i++) {
				int m = inData.readInt();
				char[] caracteres = new char[50];
				for (int j = 0; j < caracteres.length; j++) {
					caracteres[j] = inData.readChar();
				}
				String nome = new String(caracteres);
				caracteres = new char[10];
				for (int j = 0; j < caracteres.length; j++) {
					caracteres[j] = inData.readChar();
				}
				String dataNascimento = new String(caracteres);
				Aluno a = new Aluno(m, nome.trim(), dataNascimento);
				if (a.getMatricula() == matricula) {
					aluno = a;
					break;
				}
			}
		} catch (IOException e) {
			throw new SistemaEscolarException(
					"Falha ao abrir o arquivo " + AlunoFileBinarioDAO.FILE.toAbsolutePath() + " para leitura", e);
		}
		return aluno;
	}

	public synchronized List<Aluno> consultar(String nome) throws SistemaEscolarException {
		if (Files.exists(AlunoFileBinarioDAO.FILE) == false) {
			throw new SistemaEscolarException("Base de dados inexistente");
		}
		List<Aluno> alunos = new ArrayList<>();
		try (InputStream inBytes = Files.newInputStream(AlunoFileBinarioDAO.FILE, StandardOpenOption.READ);
				DataInputStream inData = new DataInputStream(inBytes)) {
			long tamanhoArquivo = Files.size(AlunoFileBinarioDAO.FILE);
			long numRegistros = tamanhoArquivo / AlunoFileBinarioDAO.BYTES_REGISTRO;
			for (int i = 0; i < numRegistros; i++) {
				int matricula = inData.readInt();
				char[] caracteres = new char[50];
				for (int j = 0; j < caracteres.length; j++) {
					caracteres[j] = inData.readChar();
				}
				String n = new String(caracteres);
				caracteres = new char[10];
				for (int j = 0; j < caracteres.length; j++) {
					caracteres[j] = inData.readChar();
				}
				String dataNascimento = new String(caracteres);
				Aluno aluno = new Aluno(matricula, n.trim(), dataNascimento);
				if (Pattern.matches(".*" + nome + ".*", aluno.getNome()) == true) {
					alunos.add(aluno);
				}
			}
		} catch (IOException e) {
			throw new SistemaEscolarException(
					"Falha ao abrir o arquivo " + AlunoFileBinarioDAO.FILE.toAbsolutePath() + " para leitura", e);
		}
		return alunos;
	}

	public synchronized List<Aluno> consultar(LocalDate dataNascimento) throws SistemaEscolarException {
		if (Files.exists(AlunoFileBinarioDAO.FILE) == false) {
			throw new SistemaEscolarException("Base de dados inexistente");
		}
		List<Aluno> alunos = new ArrayList<>();
		try (InputStream inBytes = Files.newInputStream(AlunoFileBinarioDAO.FILE, StandardOpenOption.READ);
				DataInputStream inData = new DataInputStream(inBytes)) {
			long tamanhoArquivo = Files.size(AlunoFileBinarioDAO.FILE);
			long numRegistros = tamanhoArquivo / AlunoFileBinarioDAO.BYTES_REGISTRO;
			for (int i = 0; i < numRegistros; i++) {
				int matricula = inData.readInt();
				char[] caracteres = new char[50];
				for (int j = 0; j < caracteres.length; j++) {
					caracteres[j] = inData.readChar();
				}
				String nome = new String(caracteres);
				caracteres = new char[10];
				for (int j = 0; j < caracteres.length; j++) {
					caracteres[j] = inData.readChar();
				}
				String dn = new String(caracteres);
				Aluno aluno = new Aluno(matricula, nome.trim(), dn);
				if (aluno.getNascimento().equals(dataNascimento) == true) {
					alunos.add(aluno);
				}
			}
		} catch (IOException e) {
			throw new SistemaEscolarException(
					"Falha ao abrir o arquivo " + AlunoFileBinarioDAO.FILE.toAbsolutePath() + " para leitura", e);
		}
		return alunos;
	}

	public synchronized List<Aluno> listar() throws SistemaEscolarException {
		if (Files.exists(AlunoFileBinarioDAO.FILE) == false) {
			throw new SistemaEscolarException("Base de dados inexistente");
		}
		List<Aluno> alunos = new ArrayList<>();
		try (InputStream inBytes = Files.newInputStream(AlunoFileBinarioDAO.FILE, StandardOpenOption.READ);
				DataInputStream inData = new DataInputStream(inBytes)) {
			long tamanhoArquivo = Files.size(AlunoFileBinarioDAO.FILE);
			long numRegistros = tamanhoArquivo / AlunoFileBinarioDAO.BYTES_REGISTRO;
			for (int i = 0; i < numRegistros; i++) {
				int matricula = inData.readInt();
				char[] caracteres = new char[50];
				for (int j = 0; j < caracteres.length; j++) {
					caracteres[j] = inData.readChar();
				}
				String nome = new String(caracteres);
				caracteres = new char[10];
				for (int j = 0; j < caracteres.length; j++) {
					caracteres[j] = inData.readChar();
				}
				String dataNascimento = new String(caracteres);
				Aluno aluno = new Aluno(matricula, nome.trim(), dataNascimento);
				alunos.add(aluno);
			}
		} catch (IOException e) {
			throw new SistemaEscolarException(
					"Falha ao abrir o arquivo " + AlunoFileBinarioDAO.FILE.toAbsolutePath() + " para leitura", e);
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

	public synchronized boolean atualizar(Aluno aluno, String novoNome, LocalDate novaDataNascimento)
			throws SistemaEscolarException {
		if (Files.exists(AlunoFileBinarioDAO.FILE) == false) {
			throw new SistemaEscolarException("Base de dados inexistente");
		}
		Path arquivoTemporario = null;
		try {
			arquivoTemporario = Files.createTempFile("alunos", "temp");
		} catch (IOException e) {
			throw new SistemaEscolarException(
					"Não foi possivel criar o arquivo temporário para realizar operação de exclusão", e);
		}
		try (InputStream inBytes = Files.newInputStream(AlunoFileBinarioDAO.FILE, StandardOpenOption.READ);
				DataInputStream inData = new DataInputStream(inBytes)) {
			long tamanhoArquivo = Files.size(AlunoFileBinarioDAO.FILE);
			long numRegistros = tamanhoArquivo / AlunoFileBinarioDAO.BYTES_REGISTRO;
			// try (OutputStream outBytes = new FileOutputStream(arquivoTemporario);
			try (OutputStream outBytes = Files.newOutputStream(arquivoTemporario, StandardOpenOption.WRITE);
					DataOutputStream outData = new DataOutputStream(outBytes)) {
				aluno.setNome(novoNome);
				aluno.setNascimento(novaDataNascimento);
				for (int i = 0; i < numRegistros; i++) {
					int matricula = inData.readInt();
					char[] caracteres = new char[50];
					for (int j = 0; j < caracteres.length; j++) {
						caracteres[j] = inData.readChar();
					}
					String nome = new String(caracteres);
					caracteres = new char[10];
					for (int j = 0; j < caracteres.length; j++) {
						caracteres[j] = inData.readChar();
					}
					String dataNascimento = new String(caracteres);
					Aluno a = new Aluno(matricula, nome.trim(), dataNascimento);
					if (a.equals(aluno) == true) {
						outData.writeInt(aluno.getMatricula());
						StringBuilder strb = new StringBuilder(aluno.getNome());
						strb.setLength(50);
						outData.writeChars(strb.toString());
						String dn = aluno.getNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
						strb = new StringBuilder(dn);
						strb.setLength(10);
						outData.writeChars(strb.toString());
					} else {
						outData.writeInt(a.getMatricula());
						StringBuilder strb = new StringBuilder(a.getNome());
						strb.setLength(50);
						outData.writeChars(strb.toString());
						String dn = a.getNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
						strb = new StringBuilder(dn);
						strb.setLength(10);
						outData.writeChars(strb.toString());
					}
				}
			} catch (IOException e) {
				throw new SistemaEscolarException("Falha ao escrever no arquivo " + arquivoTemporario.toAbsolutePath(),
						e);
			}
		} catch (IOException e) {
			throw new SistemaEscolarException(
					"Falha ao abrir o arquivo " + AlunoFileBinarioDAO.FILE.toAbsolutePath() + " para leitura", e);
		}

		try {
			Files.copy(arquivoTemporario, AlunoFileBinarioDAO.FILE, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new SistemaEscolarException("Falha ao copiar do arquivo temporário", e);
		}

		return true;
	}
}

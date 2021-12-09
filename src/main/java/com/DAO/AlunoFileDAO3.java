package main.java.com.DAO;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

public class AlunoFileDAO3 implements AlunoDAO {

	public static final File DIRECTORY = new File("files" + File.separator);
	public static final File FILE = new File(AlunoFileDAO3.DIRECTORY, "alunos.bin");
	public static final int BYTES_REGISTRO = 124;

	public AlunoFileDAO3() {
		super();
	}

	/*
	 * Cada registro salvo de aluno consome 124 bytes do arquivo
	 */
	public synchronized boolean salvar(Aluno aluno) throws SistemaEscolarException {
		try {
			this.criarDiretorio(AlunoFileDAO3.DIRECTORY);
			this.criarArquivo(AlunoFileDAO3.FILE);
		} catch (IOException e) {
			throw new SistemaEscolarException("Não foi possivel criar o arquivo", e);
		}
		try (OutputStream outBytes = new FileOutputStream(AlunoFileDAO3.FILE, true);
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
					"Falha ao abrir o arquivo " + AlunoFileDAO3.FILE.getAbsolutePath() + " para escrita", e);
		}
		return true;
	}

	public synchronized boolean excluir(Aluno aluno) throws SistemaEscolarException {
		if (AlunoFileDAO3.FILE.exists() == false) {
			throw new SistemaEscolarException("Base de dados inexistente");
		}
		File arquivoTemporario = new File(AlunoFileDAO3.DIRECTORY, "alunos_temp.bin");
		try {
			this.criarArquivo(arquivoTemporario);
		} catch (IOException e) {
			throw new SistemaEscolarException(
					"Não foi possivel criar o arquivo temporário para realizar operação de exclusão", e);
		}
		long tamanhoArquivo = AlunoFileDAO3.FILE.length();
		long numRegistros = tamanhoArquivo / AlunoFileDAO3.BYTES_REGISTRO;
		try {
			try (InputStream inBytes = new FileInputStream(AlunoFileDAO3.FILE);
					DataInputStream inData = new DataInputStream(inBytes)) {
				try (OutputStream outBytes = new FileOutputStream(arquivoTemporario);
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
					throw new SistemaEscolarException(
							"Falha ao escrever no arquivo " + arquivoTemporario.getAbsolutePath(), e);
				}
			} catch (IOException e) {
				throw new SistemaEscolarException(
						"Falha ao abrir o arquivo " + AlunoFileDAO3.FILE.getAbsolutePath() + " para leitura", e);
			}

			try {
				this.copiarArquivo(arquivoTemporario, AlunoFileDAO3.FILE);
			} catch (IOException e) {
				e.printStackTrace();
				throw new SistemaEscolarException("Falha ao copiar do arquivo temporário", e);
			}
		} finally {
			if (arquivoTemporario.exists() == true) {
				arquivoTemporario.delete();
			}
		}
		return true;
	}

	public synchronized Aluno consultar(int matricula) throws SistemaEscolarException {
		if (AlunoFileDAO3.FILE.exists() == false) {
			throw new SistemaEscolarException("Base de dados inexistente");
		}
		Aluno aluno = null;
		long tamanhoArquivo = AlunoFileDAO3.FILE.length();
		long numRegistros = tamanhoArquivo / AlunoFileDAO3.BYTES_REGISTRO;
		try (InputStream inBytes = new FileInputStream(AlunoFileDAO3.FILE);
				DataInputStream inData = new DataInputStream(inBytes)) {
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
					"Falha ao abrir o arquivo " + AlunoFileDAO3.FILE.getAbsolutePath() + " para leitura", e);
		}
		return aluno;
	}

	public synchronized List<Aluno> consultar(String nome) throws SistemaEscolarException {
		if (AlunoFileDAO3.FILE.exists() == false) {
			throw new SistemaEscolarException("Base de dados inexistente");
		}
		List<Aluno> alunos = new ArrayList<>();
		long tamanhoArquivo = AlunoFileDAO3.FILE.length();
		long numRegistros = tamanhoArquivo / AlunoFileDAO3.BYTES_REGISTRO;
		try (InputStream inBytes = new FileInputStream(AlunoFileDAO3.FILE);
				DataInputStream inData = new DataInputStream(inBytes)) {
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
					"Falha ao abrir o arquivo " + AlunoFileDAO3.FILE.getAbsolutePath() + " para leitura", e);
		}
		return alunos;
	}

	public synchronized List<Aluno> consultar(LocalDate dataNascimento) throws SistemaEscolarException {
		if (AlunoFileDAO3.FILE.exists() == false) {
			throw new SistemaEscolarException("Base de dados inexistente");
		}
		List<Aluno> alunos = new ArrayList<>();
		long tamanhoArquivo = AlunoFileDAO3.FILE.length();
		long numRegistros = tamanhoArquivo / AlunoFileDAO3.BYTES_REGISTRO;
		try (InputStream inBytes = new FileInputStream(AlunoFileDAO3.FILE);
				DataInputStream inData = new DataInputStream(inBytes)) {
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
					"Falha ao abrir o arquivo " + AlunoFileDAO3.FILE.getAbsolutePath() + " para leitura", e);
		}
		return alunos;
	}

	public synchronized List<Aluno> listar() throws SistemaEscolarException {
		if (AlunoFileDAO3.FILE.exists() == false) {
			throw new SistemaEscolarException("Base de dados inexistente");
		}
		long tamanhoArquivo = AlunoFileDAO3.FILE.length();
		long numRegistros = tamanhoArquivo / AlunoFileDAO3.BYTES_REGISTRO;
		List<Aluno> alunos = new ArrayList<>();
		try (InputStream inBytes = new FileInputStream(AlunoFileDAO3.FILE);
				DataInputStream inData = new DataInputStream(inBytes)) {
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
					"Falha ao abrir o arquivo " + AlunoFileDAO3.FILE.getAbsolutePath() + " para leitura", e);
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
		if (AlunoFileDAO3.FILE.exists() == false) {
			throw new SistemaEscolarException("Base de dados inexistente");
		}
		File arquivoTemporario = new File(AlunoFileDAO3.DIRECTORY, "alunos_temp.txt");
		try {
			this.criarArquivo(arquivoTemporario);
		} catch (IOException e) {
			throw new SistemaEscolarException(
					"Não foi possivel criar o arquivo temporário para realizar operação de alteração", e);
		}
		
		long tamanhoArquivo = AlunoFileDAO3.FILE.length();
		long numRegistros = tamanhoArquivo / AlunoFileDAO3.BYTES_REGISTRO;
		try {
			try (InputStream inBytes = new FileInputStream(AlunoFileDAO3.FILE);
					DataInputStream inData = new DataInputStream(inBytes)) {
				try (OutputStream outBytes = new FileOutputStream(arquivoTemporario);
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
					throw new SistemaEscolarException(
							"Falha ao escrever no arquivo " + arquivoTemporario.getAbsolutePath(), e);
				}
			} catch (IOException e) {
				throw new SistemaEscolarException(
						"Falha ao abrir o arquivo " + AlunoFileDAO3.FILE.getAbsolutePath() + " para leitura", e);
			}

			try {
				this.copiarArquivo(arquivoTemporario, AlunoFileDAO3.FILE);
			} catch (IOException e) {
				throw new SistemaEscolarException("Falha ao copiar do arquivo temporário", e);
			}
		} finally {
			if (arquivoTemporario.exists() == true) {
				arquivoTemporario.delete();
			}
		}
		return true;
	}

	private void criarDiretorio(File diretorio) throws IOException {
		if ((diretorio.exists() == false) && (diretorio.isDirectory() == true)) {
			AlunoFileDAO.DIRECTORY.mkdir();
		}
	}

	private void criarArquivo(File arquivo) throws IOException {
		if ((arquivo.exists() == false) && (arquivo.isFile() == true)) {
			AlunoFileDAO.FILE_TXT.createNewFile();
		}
	}

	private void copiarArquivo(File arquivoOrigem, File arquivoDestino) throws IOException {
		if (arquivoOrigem.exists() == false) {
			throw new IllegalArgumentException(
					"Falha ao copiar arquivo: Objeto File de origem se refere a um caminho inexistente");
		}
		if (arquivoDestino.exists() == false) {
			throw new IllegalArgumentException(
					"Falha ao copiar arquivo: Objeto File de destino se refere a um caminho inexistente");
		}
		if (arquivoOrigem.isDirectory() == true) {
			throw new IllegalArgumentException(
					"Falha ao copiar arquivo: Objeto File de origem se refere a um diretório");
		}
		if (arquivoDestino.isDirectory() == true) {
			throw new IllegalArgumentException(
					"Falha ao copiar arquivo: Objeto File de destino se refere a um diretório");
		}
		// long tamanhoArquivo = AlunoFileDAO3.FILE.length();
		long tamanhoArquivo = arquivoOrigem.length();
		long numRegistros = tamanhoArquivo / AlunoFileDAO3.BYTES_REGISTRO;
		try (InputStream inBytes = new FileInputStream(arquivoOrigem);
				DataInputStream inData = new DataInputStream(inBytes)) {
			try (OutputStream outBytes = new FileOutputStream(arquivoDestino);
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

					outData.writeInt(matricula);
					outData.writeChars(nome);
					outData.writeChars(dataNascimento);
				}
			}
		}
	}
}

package main.java.com.DAO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
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

	public static final File DIRECTORY = new File("files" + File.separator);
	public static final File FILE = new File(AlunoFileTextoDAO.DIRECTORY, "alunos.txt");
	public static final String SEPARADOR = ";";
	
	public AlunoFileTextoDAO() {
		super();
	}

	public synchronized boolean salvar(Aluno aluno) throws SistemaEscolarException {
		try {
			this.criarDiretorio(AlunoFileTextoDAO.DIRECTORY);
			this.criarArquivo(AlunoFileTextoDAO.FILE);
		} catch (IOException e) {
			throw new SistemaEscolarException("Não foi possivel criar o arquivo", e);
		}
		try (Writer writer = new FileWriter(AlunoFileTextoDAO.FILE, true);
				PrintWriter pw = new PrintWriter(writer)) {
			String line = this.converterAluno(aluno);
			pw.println(line);
		} catch (IOException e) {
			throw new SistemaEscolarException(
				"Falha ao abrir o arquivo " + AlunoFileTextoDAO.FILE.getAbsolutePath() + 
					" para escrita", e);
		}
		return true;
	}
	
	public synchronized boolean excluir(Aluno aluno) throws SistemaEscolarException {
		if (AlunoFileTextoDAO.FILE.exists() == false) {
			throw new SistemaEscolarException("Base de dados inexistente");
		}
		File arquivoTemporario = new File(AlunoFileTextoDAO.DIRECTORY, "alunos_temp.txt");
		try {
			this.criarArquivo(arquivoTemporario);
		} catch (IOException e) {
			throw new SistemaEscolarException("Não foi possivel criar o arquivo temporário para realizar operação de exclusão", e);
		}
		try {
			try (Reader reader = new FileReader(AlunoFileTextoDAO.FILE);
					BufferedReader buffer = new BufferedReader(reader)) {
				try (Writer writer = new FileWriter(arquivoTemporario);
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
					throw new SistemaEscolarException(
							"Falha ao escrever no arquivo " + arquivoTemporario.getAbsolutePath(), e);
				}
			} catch (IOException e) {
				throw new SistemaEscolarException(
					"Falha ao abrir o arquivo " + AlunoFileTextoDAO.FILE.getAbsolutePath() + 
						" para leitura", e);
			}
			
			try {
				this.copiarArquivo(arquivoTemporario, AlunoFileTextoDAO.FILE);
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
	
	public synchronized Aluno consultar(int matricula) throws SistemaEscolarException {
		if (AlunoFileTextoDAO.FILE.exists() == false) {
			throw new SistemaEscolarException("Base de dados inexistente");
		}
		Aluno aluno = null;
		try (Reader reader = new FileReader(AlunoFileTextoDAO.FILE);
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
				"Falha ao abrir o arquivo " + AlunoFileTextoDAO.FILE.getAbsolutePath() + 
					" para leitura", e);
		}
		return aluno;
	}
	
	public synchronized List<Aluno> consultar(String nome) throws SistemaEscolarException {
		if (AlunoFileTextoDAO.FILE.exists() == false) {
			throw new SistemaEscolarException("Base de dados inexistente");
		}
		List<Aluno> alunos = new ArrayList<>();
		try (Reader reader = new FileReader(AlunoFileTextoDAO.FILE);
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
				"Falha ao abrir o arquivo " + AlunoFileTextoDAO.FILE.getAbsolutePath() + 
					" para leitura", e);
		}
		return alunos;
	}
	
	public synchronized List<Aluno> consultar(LocalDate dataNascimento) throws SistemaEscolarException {
		if (AlunoFileTextoDAO.FILE.exists() == false) {
			throw new SistemaEscolarException("Base de dados inexistente");
		}
		List<Aluno> alunos = new ArrayList<>();
		try (Reader reader = new FileReader(AlunoFileTextoDAO.FILE);
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
				"Falha ao abrir o arquivo " + AlunoFileTextoDAO.FILE.getAbsolutePath() + 
					" para leitura", e);
		}
		return alunos;
	}
	
	public synchronized List<Aluno> listar() throws SistemaEscolarException {
		if (AlunoFileTextoDAO.FILE.exists() == false) {
			throw new SistemaEscolarException("Base de dados inexistente");
		}
		List<Aluno> alunos = new ArrayList<>();
		try (Reader reader = new FileReader(AlunoFileTextoDAO.FILE);
			BufferedReader buffer = new BufferedReader(reader)) {
			String line = null;
			while ((line = buffer.readLine()) != null) {
				Aluno aluno = this.desconverterAluno(line);
				alunos.add(aluno);
			}
		} catch (IOException e) {
			throw new SistemaEscolarException(
				"Falha ao abrir o arquivo " + AlunoFileTextoDAO.FILE.getAbsolutePath() + 
					" para leitura", e);
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

	public synchronized boolean atualizar(Aluno aluno, 
			String novoNome, LocalDate novaDataNascimento) throws SistemaEscolarException {
		if (AlunoFileTextoDAO.FILE.exists() == false) {
			throw new SistemaEscolarException("Base de dados inexistente");
		}
		File arquivoTemporario = new File(AlunoFileTextoDAO.DIRECTORY, "alunos_temp.txt");
		try {
			this.criarArquivo(arquivoTemporario);
		} catch (IOException e) {
			throw new SistemaEscolarException("Não foi possivel criar o arquivo temporário para realizar operação de alteração", e);
		}
		try {
			try (Reader reader = new FileReader(AlunoFileTextoDAO.FILE);
					BufferedReader buffer = new BufferedReader(reader)) {
				try (Writer writer = new FileWriter(arquivoTemporario);
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
					throw new SistemaEscolarException(
							"Falha ao escrever no arquivo " + arquivoTemporario.getAbsolutePath(), e);
				}
			} catch (IOException e) {
				throw new SistemaEscolarException(
					"Falha ao abrir o arquivo " + AlunoFileTextoDAO.FILE.getAbsolutePath() + 
						" para leitura", e);
			}
			
			try {
				this.copiarArquivo(arquivoTemporario, AlunoFileTextoDAO.FILE);
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
			AlunoFileTextoDAO.DIRECTORY.mkdir();
		}
	}
	
	private void criarArquivo(File arquivo) throws IOException {
		if ((arquivo.exists() == false) && (arquivo.isFile() == true)) {
			AlunoFileTextoDAO.FILE.createNewFile();
		}
	}
	
	private void copiarArquivo(File arquivoOrigem, File arquivoDestino) throws IOException {
		if (arquivoOrigem.exists() == false) {
			throw new IllegalArgumentException("Falha ao copiar arquivo: Objeto File de origem se refere a um caminho inexistente");
		}
		if (arquivoDestino.exists() == false) {
			throw new IllegalArgumentException("Falha ao copiar arquivo: Objeto File de destino se refere a um caminho inexistente");
		}
		if (arquivoOrigem.isDirectory() == true) {
			throw new IllegalArgumentException("Falha ao copiar arquivo: Objeto File de origem se refere a um diretório");
		}
		if (arquivoDestino.isDirectory() == true) {
			throw new IllegalArgumentException("Falha ao copiar arquivo: Objeto File de destino se refere a um diretório");
		}
		try (Reader reader = new FileReader(arquivoOrigem);
				BufferedReader buffer = new BufferedReader(reader);
				Writer writer = new FileWriter(arquivoDestino);
				PrintWriter pw = new PrintWriter(writer)) {
			String line = null;
			while ((line = buffer.readLine()) != null) {
				pw.println(line);
			}		
		}
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

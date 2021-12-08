package main.java.com.DAO;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Writer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import main.java.com.exceptions.MatriculaDuplicadaException;
import main.java.com.exceptions.SistemaEscolarException;
import main.java.com.model.Aluno;
import main.java.com.util.AlunoMatriculaComparator;
import main.java.com.util.AlunoDataNascimentoComparator;
import main.java.com.util.AlunoNomeComparator;

public class AlunoFileDAO {

	public static final File DIRECTORY = new File("files" + File.separator);
	public static final File FILE_BINARY = new File(AlunoFileDAO.DIRECTORY, "alunos.bin");
	public static final File FILE_TXT = new File(AlunoFileDAO.DIRECTORY, "alunos.txt");
	public static final String SEPARADOR = ";";
	public static final int BYTES_REGISTRO = 124;
	
	public AlunoFileDAO() {
		super();
	}

	/*
	 * Cada registro salvo de aluno consome 124 bytes do arquivo
	 * */
	public synchronized boolean salvarAlunoFileBinario(Aluno aluno) throws SistemaEscolarException {
		if (AlunoFileDAO.FILE_BINARY.exists() == false) {
			try {
				if (AlunoFileDAO.DIRECTORY.exists() == false) {
					AlunoFileDAO.DIRECTORY.mkdir();
				}
				AlunoFileDAO.FILE_BINARY.createNewFile();
			} catch (IOException e) {
				throw new SistemaEscolarException("Não foi possivel criar o arquivo binário", e);
			}
		}

		try (OutputStream outBytes = new FileOutputStream(AlunoFileDAO.FILE_BINARY, true);
				DataOutputStream outBytesData = new DataOutputStream(outBytes)) {
			outBytesData.writeInt(aluno.getMatricula());
			StringBuilder strb = new StringBuilder(aluno.getNome());
			strb.setLength(50);
			outBytesData.writeChars(strb.toString());
			String dataNascimento = aluno.getNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			strb = new StringBuilder(dataNascimento);
			strb.setLength(10);
			outBytesData.writeChars(strb.toString());
		} catch (IOException e) {
			throw new SistemaEscolarException(
				"Falha ao abrir o arquivo " + AlunoFileDAO.FILE_BINARY.getAbsolutePath() + 
					" para escrita", e);
		}
		return true;
	}
	
	public synchronized boolean salvarAlunoFileTexto(Aluno aluno) throws SistemaEscolarException {
		if (AlunoFileDAO.FILE_TXT.exists() == false) {
			try {
				if (AlunoFileDAO.DIRECTORY.exists() == false) {
					AlunoFileDAO.DIRECTORY.mkdir();
				}
				AlunoFileDAO.FILE_TXT.createNewFile();
			} catch (IOException e) {
				throw new SistemaEscolarException("Não foi possivel criar o arquivo binário", e);
			}
		}

		try (Writer writer = new FileWriter(AlunoFileDAO.FILE_TXT, true);
				PrintWriter pw = new PrintWriter(writer)) {
			//String matricula = Integer.toString(aluno.getMatricula());
			pw.print(aluno.getMatricula());
			pw.print(AlunoFileDAO.SEPARADOR);
			pw.print(aluno.getNome());
			pw.print(AlunoFileDAO.SEPARADOR);
			String dataNascimento = aluno.getNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			pw.print(dataNascimento);
			pw.println();
		} catch (IOException e) {
			throw new SistemaEscolarException(
				"Falha ao abrir o arquivo " + AlunoFileDAO.FILE_BINARY.getAbsolutePath() + 
					" para escrita", e);
		}
		return true;
	}
	
	public synchronized Aluno consultaFileBinario(int matricula) throws SistemaEscolarException {
		List<Aluno> alunos = this.recuperarRegistrosFileBinario();
		for (Aluno aluno : alunos) {
			if (aluno.getMatricula() == matricula) {
				return aluno;
			}
		}
		return null;
	}
	
	public synchronized List<Aluno> consultaFileBinario(String nome) throws SistemaEscolarException {
		List<Aluno> alunos = this.recuperarRegistrosFileBinario();
		List<Aluno> resultAlunos = new ArrayList<>();
		for (Aluno aluno : alunos) {
			if (Pattern.matches(".*" + nome + ".*", aluno.getNome()) == true) {
				resultAlunos.add(aluno);
			}
		}
		if (resultAlunos.size() > 0) {
			return resultAlunos;
		}
		return null;
	}
	
	public synchronized List<Aluno> consultaFileBinario(LocalDate dataNascimento) throws SistemaEscolarException {
		List<Aluno> alunos = this.recuperarRegistrosFileBinario();
		List<Aluno> resultAlunos = new ArrayList<>();
		for (Aluno aluno : alunos) {
			if (aluno.getNascimento().isEqual(dataNascimento) == true) {
				resultAlunos.add(aluno);
			}
		}
		if (resultAlunos.size() > 0) {
			return resultAlunos;
		}
		return null;
	}
	
	public synchronized Aluno consultaFileTexto(int matricula) throws SistemaEscolarException {
		List<Aluno> alunos = this.recuperarRegistrosFileTexto();
		for (Aluno aluno : alunos) {
			if (aluno.getMatricula() == matricula) {
				return aluno;
			}
		}
		return null;
	}
	
	public synchronized List<Aluno> consultaFileTexto(String nome) throws SistemaEscolarException {
		List<Aluno> alunos = this.recuperarRegistrosFileTexto();
		List<Aluno> resultAlunos = new ArrayList<>();
		for (Aluno aluno : alunos) {
			if (Pattern.matches(".*" + nome + ".*", aluno.getNome()) == true) {
				resultAlunos.add(aluno);
			}
		}
		if (resultAlunos.size() > 0) {
			return resultAlunos;
		}
		return null;
	}
	
	public synchronized List<Aluno> consultaFileTexto(LocalDate dataNascimento) throws SistemaEscolarException {
		List<Aluno> alunos = this.recuperarRegistrosFileTexto();
		List<Aluno> resultAlunos = new ArrayList<>();
		for (Aluno aluno : alunos) {
			if (aluno.getNascimento().isEqual(dataNascimento) == true) {
				resultAlunos.add(aluno);
			}
		}
		if (resultAlunos.size() > 0) {
			return resultAlunos;
		}
		return null;
	}
	
	public synchronized List<Aluno> obterAlunosFileBinario() throws SistemaEscolarException {
		List<Aluno> alunos = this.recuperarRegistrosFileBinario();
		if (alunos.size() > 0) {
			return alunos;
		}
		return null;
	}
	
	public synchronized List<Aluno> obterAlunosFileTexto() throws SistemaEscolarException {
		List<Aluno> alunos = this.recuperarRegistrosFileTexto();
		if (alunos.size() > 0) {
			return alunos;
		}
		return null;
	}
	
	public boolean excluirFileBinairo(Aluno aluno) throws SistemaEscolarException {
		List<Aluno> alunos = this.recuperarRegistrosFileBinario();
		Iterator<Aluno> iterator = alunos.iterator();
		while (iterator.hasNext() == true) {
			Aluno a = iterator.next();
			if (a.equals(aluno) == true) {
				iterator.remove();
				break;
			}
		}
		return this.persistirRegistrosFileBinario(alunos);
	}
	
	public boolean alterarFileBinario(int matricula, String nome, LocalDate dataNascimento) throws SistemaEscolarException {
		List<Aluno> alunos = this.recuperarRegistrosFileBinario();
		for (Aluno aluno : alunos) {
			if (aluno.getMatricula() == matricula) {
				aluno.setNome(nome);
				aluno.setNascimento(dataNascimento);
				break;
			}
		}
		return this.persistirRegistrosFileBinario(alunos);
	}
	
	public List<Aluno> ordenarPorMatriculaFileBinario() throws SistemaEscolarException {
		List<Aluno> alunos = this.recuperarRegistrosFileBinario();
		if (alunos.size() == 0) {
			return null;
		}
		Collections.sort(alunos, new AlunoMatriculaComparator());
		return alunos;
	}
	
	public List<Aluno> ordenarPorNomeFileBinario() throws SistemaEscolarException {
		List<Aluno> alunos = this.recuperarRegistrosFileBinario();
		if (alunos.size() == 0) {
			return null;
		}
		Collections.sort(alunos, new AlunoNomeComparator());
		return alunos;
	}

	public List<Aluno> ordenarPorDataNascimentoFileBinario() throws SistemaEscolarException {
		List<Aluno> alunos = this.recuperarRegistrosFileBinario();
		if (alunos.size() == 0) {
			return null;
		}
		Collections.sort(alunos, new AlunoDataNascimentoComparator());
		return alunos;
	}

	private List<Aluno> recuperarRegistrosFileBinario() throws SistemaEscolarException {
		List<Aluno> list = new ArrayList<>();
		if (AlunoFileDAO.FILE_BINARY.exists() == false) {
			return list;
		}
		long tamanhoArquivo = AlunoFileDAO.FILE_BINARY.length();
		long numRegistros = tamanhoArquivo / AlunoFileDAO.BYTES_REGISTRO;
		try (InputStream inBytes = new FileInputStream(AlunoFileDAO.FILE_BINARY);
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
				Aluno a = new Aluno(matricula, nome.trim(), dataNascimento);
				list.add(a);
			}
		} catch (IOException e) {
			throw new SistemaEscolarException(
				"Falha ao abrir o arquivo " + AlunoFileDAO.FILE_BINARY.getAbsolutePath() + 
					" para leitura", e);
		}
		return list;
	}
	
	private List<Aluno> recuperarRegistrosFileTexto() throws SistemaEscolarException {
		List<Aluno> list = new ArrayList<>();
		try (Reader reader = new FileReader(AlunoFileDAO.FILE_TXT);
				BufferedReader buffer = new BufferedReader(reader)) {
			String line = null;
			while ((line = buffer.readLine()) != null) {
				String[] tokens = line.split(AlunoFileDAO.SEPARADOR);
				int matricula = Integer.parseInt(tokens[0]);
				String nome = tokens[1];
				String dataNascimento = tokens[2];
				Aluno a = new Aluno(matricula, nome, dataNascimento);
				list.add(a);
			}
		} catch (IOException e) {
			throw new SistemaEscolarException(
				"Falha ao abrir o arquivo " + AlunoFileDAO.FILE_BINARY.getAbsolutePath() + 
					" para leitura", e);
		}
		return list;
	}

	private boolean persistirRegistrosFileBinario(List<Aluno> alunos) throws SistemaEscolarException {
		if ((alunos == null) || (AlunoFileDAO.FILE_BINARY.length() == 0)) {
			return false;
		}
		if (AlunoFileDAO.FILE_BINARY.exists() == true) {
			AlunoFileDAO.FILE_BINARY.delete();
		}
		for (Aluno aluno : alunos) {
			this.salvarAlunoFileBinario(aluno);
		}
		return true;
	}
}

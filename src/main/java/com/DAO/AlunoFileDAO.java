package main.java.com.DAO;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import main.java.com.exceptions.SistemaEscolarException;
import main.java.com.model.Aluno;

public class AlunoFileDAO {

	public static final File DIRECTORY = new File("files" + File.separator);
	public static final File FILE = new File(AlunoFileDAO.DIRECTORY, "alunos.bin");
	public static final String SEPARADOR = ";";

	public boolean salvarAluno(Aluno aluno) throws SistemaEscolarException {
		if (AlunoFileDAO.FILE.exists() == false) {
			try {
				AlunoFileDAO.DIRECTORY.mkdir();
				AlunoFileDAO.FILE.createNewFile();
			} catch (IOException e) {
				throw new SistemaEscolarException("Não foi possivel criar o arquivo binário", e);
			}
		}

		try (OutputStream outBytes = new FileOutputStream(AlunoFileDAO.FILE, true);
				//FileWriter f = new FileWriter(FILE, true);
				DataOutputStream outBytesData = new DataOutputStream(outBytes);
				// RandomAccessFile outBytesData = new RandomAccessFile(AlunoFileDAO.FILE, "rw");
				) {
			//outBytesData.seek(AlunoFileDAO.FILE.length());
			
			outBytesData.writeInt(aluno.getMatricula());
			outBytesData.writeUTF(aluno.getNome());
			outBytesData.writeUTF(aluno.getNascimento().toString());
			outBytesData.writeUTF(AlunoFileDAO.SEPARADOR);
		} catch (IOException e) {
			throw new SistemaEscolarException(
				"Falha ao abrir o arquivo " + AlunoFileDAO.FILE.getAbsolutePath() + 
					" para leitura", e);
		}
		
		return true;
	}
	
	public boolean salvarAlunoFileBinario(Aluno aluno) throws SistemaEscolarException {
		if (AlunoFileDAO.FILE.exists() == false) {
			try {
				AlunoFileDAO.DIRECTORY.mkdir();
				AlunoFileDAO.FILE.createNewFile();
			} catch (IOException e) {
				throw new SistemaEscolarException("Não foi possivel criar o arquivo binário", e);
			}
		}

//		try (OutputStream outBytes = new FileOutputStream(AlunoFileDAO.FILE, true)) {
//			outBytes.write(aluno.getMatricula());
//			byte[] byteNome = Arrays.copyOf(aluno.getNome().getBytes(), 50);
//			//System.out.println(Arrays.toString(byteNome));
//			outBytes.write(byteNome);
//			String dataNascimeto = aluno.getNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
//			outBytes.write(dataNascimeto.getBytes());
		try (OutputStream outBytes = new FileOutputStream(AlunoFileDAO.FILE, true);
				DataOutputStream outBytesData = new DataOutputStream(outBytes)) {
			outBytesData.writeInt(aluno.getMatricula());
			//byte[] byteNome = Arrays.copyOf(aluno.getNome().getBytes(), 50);
			//System.out.println(Arrays.toString(byteNome));
			//outBytesData.write(byteNome);
			StringBuilder strb = new StringBuilder(aluno.getNome());
			strb.setLength(50);
			outBytesData.writeChars(strb.toString());
			//String dataNascimeto = aluno.getNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			//outBytesData.write(dataNascimeto.getBytes());
			strb = new StringBuilder(aluno.getNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
			strb.setLength(10);
			outBytesData.writeChars(strb.toString());
		} catch (IOException e) {
			throw new SistemaEscolarException(
				"Falha ao abrir o arquivo " + AlunoFileDAO.FILE.getAbsolutePath() + 
					" para leitura", e);
		}
		System.out.println(AlunoFileDAO.FILE.length());
		return true;
	}
	
	public static void main(String[] args) {
		String s = "23-43-2323";
		byte[] b = s.getBytes(StandardCharsets.UTF_8);
		System.out.println(b.length);
	}
}

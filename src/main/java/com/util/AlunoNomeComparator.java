package main.java.com.util;

import java.util.Comparator;
import main.java.com.model.Aluno;

public class AlunoNomeComparator implements Comparator<Aluno> {

	@Override
	public int compare(Aluno o1, Aluno o2) {
		return o1.getNome().compareTo(o2.getNome());
	}
}

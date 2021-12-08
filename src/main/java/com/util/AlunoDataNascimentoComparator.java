package main.java.com.util;

import java.util.Comparator;
import main.java.com.model.Aluno;

public class AlunoDataNascimentoComparator implements Comparator<Aluno> {

	@Override
	public int compare(Aluno o1, Aluno o2) {
		return o1.getNascimento().compareTo(o2.getNascimento());
	}
}

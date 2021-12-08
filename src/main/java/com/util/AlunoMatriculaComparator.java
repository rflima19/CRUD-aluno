package main.java.com.util;

import java.util.Comparator;
import main.java.com.model.Aluno;

public class AlunoMatriculaComparator implements Comparator<Aluno> {

	@Override
	public int compare(Aluno o1, Aluno o2) {
		Integer m1 = o1.getMatricula();
		Integer m2 = o2.getMatricula();
		return m1.compareTo(m2);
	}
}

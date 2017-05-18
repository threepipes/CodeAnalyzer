package analyzer.dist;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import analyzer.Lexer;

public abstract class DistanceCalculator {
	public static final String NAME = "default";
	
	public List<String> tokenize(String filename) {
		Lexer lex = new Lexer(new File(filename));
		return lex.getTokenList();
	}
	
	public double[][] getDistanceTable(List<String> filelist) {
		final int size = filelist.size();
		double[][] dist = new double[size][size];
		List<Vector> vectors = new ArrayList<>();
		
		for(String filename: filelist) {
			List<String> tokens = tokenize(filename);
			vectors.add(vectorize(tokens));
		}
		
		for(int i = 0; i < size; i++) {
			Vector v = vectors.get(i);
			for(int j = i + 1; j < size; j++) {
				final double d = v.dist(vectors.get(j));
				dist[i][j] = dist[j][i] = d;
			}
		}
		
		return dist;
	}
	
	protected abstract Vector vectorize(List<String> tokens);
}

interface Vector {
	public double dist(Vector v);
	public Vector merge(Vector v);
}

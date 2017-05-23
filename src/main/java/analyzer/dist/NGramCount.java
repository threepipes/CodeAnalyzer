package analyzer.dist;

import java.util.List;

public class NGramCount extends NGram {
	private final int MAX_COUNT;
	public NGramCount(String whitelistFile, int n) {
		super(whitelistFile, n);
		MAX_COUNT = 10;
	}
	
	@Override
	protected Vector vectorize(List<String> tokens) {
		NGramVector vec =  (NGramVector) super.vectorize(tokens);
		int[] count = new int[MAX_COUNT + 1];
		for(int freq: vec.values()) {
			if(MAX_COUNT < freq) freq = MAX_COUNT;
			count[freq]++;
		}
		return new ListVector(count);
	}
}

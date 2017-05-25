package analyzer.dist;

import java.io.File;
import java.util.HashSet;
import java.util.List;

import analyzer.Lexer;

public class GeneralVectarize {
	public int[] build(String filename, String whitelistFile, int n) {
		Lexer lexer = new Lexer(new File(filename));
		ListVector vec = vectorize(lexer.getTokenList(), whitelistFile, n);
		return vec.toIntArray();
	}
	
	private ListVector vectorize(List<String> tokens, String whitelistFile, int n) {
		NGramCount ngcount = new NGramCount(whitelistFile, n);
		ListVector ngVec = (ListVector) ngcount.vectorize(tokens);
		HashSet<String> whitelist = ngcount.getWhitelist();
		ListVector sVec = generateListVector(tokens, whitelist);
		ngVec.merge(sVec);
		return ngVec;
	}
	
	private ListVector generateListVector(List<String> tokens, HashSet<String> whitelist) {
		List<AutoMaton> analyzers = AutoMaton.getter("if,indent");
		for(String t: tokens) {
			if(!whitelist.contains(t))
				t = "$";
			for(AutoMaton am: analyzers) {
				am.readToken(t);
			}
		}
		ListVector lv = (ListVector) analyzers.stream()
				.map(AutoMaton::getValue)
				.reduce(new ListVector(), (e1, e2) -> e1.merge(e2));
		return lv;
	}
}

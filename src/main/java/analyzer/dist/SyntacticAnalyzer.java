package analyzer.dist;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import utils.Reader;

public class SyntacticAnalyzer extends DistanceCalculator {
	public static final String NAME = "syntax";
	public static final String replace = "$";
	List<AutoMaton> analyzers;
	HashSet<String> whitelist;
	public SyntacticAnalyzer(HashMap<String, String> option) {
		String whitelistFile = option.getOrDefault("whitelist", "whitelist.txt");
		String automaton = option.get("automatons");
		analyzers = AutoMaton.getter(automaton);

		try(Reader in = new Reader(whitelistFile)) {
			whitelist = new HashSet<>(in.readAllLines());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected Vector vectorize(List<String> tokens) {
		for(AutoMaton am: analyzers) am.reset();
		for(String t: tokens) {
			if(!whitelist.contains(t))
				t = replace;
			for(AutoMaton am: analyzers) {
				am.readToken(t);
			}
		}
		Vector v = analyzers.stream()
				.map(AutoMaton::getValue)
				.reduce(new ListVector(), (e1, e2) -> e1.merge(e2));
		return v;
	}
}

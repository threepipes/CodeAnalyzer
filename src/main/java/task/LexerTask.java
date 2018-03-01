package task;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javassist.compiler.Lex;
import utils.Lexer;
import utils.MultiSet;

public class LexerTask extends Task {
	public static final String NAME = "lexer"; 
	
	@Override
	public void doTask(HashMap<String, String> option) {
		String opt = option.getOrDefault("subtask", "");
		if(opt.equals("parsertest")) {
			testParser();
			return;
		} else if(opt.equals("count")) {
			List<HashMap<String, Integer>> tokenCounts = countToken();
			out.printlnJson(tokenCounts);
			return;
		}
		List<List<String>> tokenList = new ArrayList<>();
		for(String filename: in.readAllLines()) {
			tokenList.add(Lexer.getTokenList(filename));
		}
		out.printlnJson(tokenList);
	}
	
	private void testParser() {

	}

	private List<HashMap<String, Integer>> countToken() {
		List<HashMap<String, Integer>> tokenCounts = new ArrayList<>();
		for(String filename: in.readAllLines()) {
			MultiSet<String> count = new MultiSet<>();
			for(String token: Lexer.getTokenList(filename)) {
				count.add(token);
			}
			tokenCounts.add(count);
		}
		return tokenCounts;
	}
}

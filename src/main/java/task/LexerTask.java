package task;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import analyzer.Lexer;

public class LexerTask extends Task {
	public static final String NAME = "lexer"; 
	
	@Override
	public void doTask(HashMap<String, String> option) {
		if(option.getOrDefault("test", "").equals("parser")) {
			testParser();
			return;
		}
		List<List<String>> tokenList = new ArrayList<>();
		for(String filename: in.readAllLines()) {
			tokenList.add(new Lexer(new File(filename)).getTokenList());
		}
		out.printlnJson(tokenList);
	}
	
	private void testParser() {
		
	}
}

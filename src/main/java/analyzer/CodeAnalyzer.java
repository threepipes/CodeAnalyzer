package analyzer;

import java.util.HashMap;

public class CodeAnalyzer {
	public static void main(String[] args) {
		CodeAnalyzer tool = new CodeAnalyzer(args);
		tool.execute();
	}
	
	private HashMap<String, String> option;
	/**
	 * コード解析ツール
	 * @param args ツールの実行方法を表す引数
	 * option=内容 の形式で記述
	 */
	public CodeAnalyzer(String[] args) {
		try {
			option = argParser(args);
		} catch (IllegalArgumentException e) {
			System.err.println(e);
			usage();
		}
	}
	
	public void usage() {
		String usage = "<java command> argkey0=argvalue0 argkey1=argvalue1 ...";
		System.out.println(usage);
	}
	
	public HashMap<String, String> argParser(String[] args) throws IllegalArgumentException {
		HashMap<String, String> option = new HashMap<>();
		String errorMsg = "引数の形式が違います";
		for(String arg: args) {
			if(!arg.contains("=")) throw new IllegalArgumentException(errorMsg);
			String[] value = arg.split("=");
			if(value.length != 2 || value[0].length() == 0 || value[1].length() == 0)
				throw new IllegalArgumentException(errorMsg);
			if(option.containsKey(value[0]))
				throw new IllegalArgumentException("同じkeyを複数使用しています: " + value[0]);
			option.put(value[0], value[1]);
		}
		return option;
	}
	
	public void execute() {
		
	}
}

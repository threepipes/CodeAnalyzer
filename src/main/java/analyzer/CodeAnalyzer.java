package analyzer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

import task.Task;

public class CodeAnalyzer {
	public static void main(String[] args) {
		if(args.length == 0) {
			usage();
			return;
		}
		try {
			CodeAnalyzer tool = new CodeAnalyzer(args);
			tool.execute();
		} catch (Exception e) {
			Logger.getGlobal().log(Level.SEVERE, e.toString());
			usage();
		}
	}

	private HashMap<String, String> option;
	/**
	 * コード解析ツール
	 * @param args ツールの実行方法を表す引数
	 * option=内容 の形式で記述
	 */
	public CodeAnalyzer(String[] args) throws Exception {
		option = argParser(args);
		setLogger();
	}
	
	public static void usage() {
		String usage = "usage:\n<java command> argkey0=argvalue0 argkey1=argvalue1 ...";
		/*
		 * 現在の設定([]内はargkey, "-"はargvalue)
		 * [inpath]: 入力ファイル(なしなら標準入力)
		 * [outpath]: 出力ファイル(なしなら標準出力)
		 * [logfile]: ログ出力(なしなら行わない)
		 *   - file: "./log.txt"に出力
		 *   - err: 標準エラー出力
		 * [loglevel]: ログレベル(なしでINFOまで)
		 *   - all: 全部
		 * [task]: 行うタスク
		 *   - lexer
		 *   - dist
		 *     [method]: 距離の定義をどうするか
		 *       - syntax: 字句的な距離
		 *         [whitelist]: $に変換しないトークンリスト(なしでwhitelist.txt)
		 *         [automatons]: ","区切りで文法方式指定
		 *           - indent: ネストをベクトル化
		 *           - if: ifの数
		 *   - diff
		 *   - test
		 *   - ngram
		 *     [n]: トークン区切り数
		 *     [whitelist]: $に変換しないトークンリスト(なしでwhitelist.list)
		 *   - ngcount
		 */
		System.err.println(usage);
	}
	
	public HashMap<String, String> argParser(String[] args) throws IllegalArgumentException {
		HashMap<String, String> option = new HashMap<>();
		String errorMsg = "引数の形式が違います";
		Logger.getGlobal().finest("Get args: " + Arrays.toString(args));
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
		String taskName = option.getOrDefault("task", "");
		Task task = Task.getTask(taskName);
		task.setIO(option.get("inpath"), option.get("outpath"));
		Logger.getGlobal().log(Level.FINEST, "start task: " + task);
		task.doTask(option);
		task.close();
	}
	
	public void setLogger() throws FileNotFoundException {
		Logger log = Logger.getGlobal();
		final Level level;
		final OutputStream out;
		switch(option.getOrDefault("logfile", "none")) {
		case "file":
			out = new FileOutputStream("./log.txt");
			break;
		case "err":
			out = System.err;
			break;
		default:
			out = null;
		}
		switch(option.getOrDefault("loglevel", "default")) {
		case "all":
			level = Level.ALL;
			break;
		default:
			level = Level.INFO;
		}
		log.addHandler(new StreamHandler(){
			{
				if(out != null) setOutputStream(out);
				setLevel(level);
			}
		});
		log.setUseParentHandlers(false);
		log.setLevel(level);
	}
}

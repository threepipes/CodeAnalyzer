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
			setLogger();
		} catch (IllegalArgumentException | FileNotFoundException e) {
			Logger.getGlobal().log(Level.SEVERE, e.toString());
			usage();
		}
	}
	
	public void usage() {
		String usage = "<java command> argkey0=argvalue0 argkey1=argvalue1 ...";
		System.err.println(usage);
	}
	
	public HashMap<String, String> argParser(String[] args) throws IllegalArgumentException {
		Logger.getGlobal().finest("Get args: " + Arrays.toString(args));
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

/*
 * memo
 *   option
 *     task:
 *       lexer
 *       diff
 *       dist --- ngram, syntax
 *     outpath:
 *       出力パス
 *     inpath:
 *       入力パス
 * 
 */

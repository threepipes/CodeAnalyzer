package analyzer;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import analyzer.dist.DistanceCalculator;
import analyzer.dist.SyntacticAnalyzer;
import utils.Printer;
import utils.Reader;

public abstract class Task {
	public static final String NAME = "Task"; 
	public static Task getTask(String name) {
		switch(name) {
		case LexerTask.NAME:
			return new LexerTask();
		case DistanceTask.NAME:
			return new DistanceTask();
		case DiffTask.NAME:
			return new DiffTask();
		case TestTask.NAME:
			return new TestTask();
		default:
			throw new IllegalArgumentException("Task option error: " + name);
		}
	}
	
	protected Printer out;
	protected Reader in;
	
	public void setIO(String reader, String printer) {
		try {
			this.out = printer == null ? new Printer() : new Printer(printer);
			this.in = reader == null ? new Reader() : new Reader(reader);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		out.close();
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public abstract void doTask(HashMap<String, String> option);
	
	@Override
	public String toString() {
		return NAME;
	}
}

class LexerTask extends Task {
	public static final String NAME = "lexer"; 
	
	@Override
	public void doTask(HashMap<String, String> option) {
		// TODO 自動生成されたメソッド・スタブ
		
	}
}

class DistanceTask extends Task {
	public static final String NAME = "dist"; 
	
	@Override
	public void doTask(HashMap<String, String> option) {
		String method = option.get("method");
		if(method == null) {
			Logger.getGlobal().log(Level.SEVERE, "no method chosen");
			return;
		}
		DistanceCalculator calc = null;
		switch(method) {
		case SyntacticAnalyzer.NAME:
			calc = new SyntacticAnalyzer(option);
			break;
		}
		if(calc == null) {
			Logger.getGlobal().log(Level.SEVERE, "wrong method name: " + method);
			return;
		}
		
		double[][] dist = calc.getDistanceTable(in.readAllLines());
		out.printlnJson(dist);
	}
}

class DiffTask extends Task {
	public static final String NAME = "diff"; 
	
	@Override
	public void doTask(HashMap<String, String> option) {
		// TODO 自動生成されたメソッド・スタブ
		
	}
}

class TestTask extends Task {
	public static final String NAME = "test";
	
	@Override
	public void doTask(HashMap<String, String> option) {
		Logger.getGlobal().log(Level.FINEST, "start test");
		Logger.getGlobal().log(Level.FINER,  "finer");
		Logger.getGlobal().log(Level.FINE,   "fine");
		Logger.getGlobal().log(Level.INFO,   "info");
		Logger.getGlobal().log(Level.CONFIG, "config");
		Logger.getGlobal().log(Level.WARNING,"warn");
		Logger.getGlobal().log(Level.SEVERE, "severe");
		out.printlnJson(new double[][]{{0.4, 0.1}, {1.3, 9.5}});
	}
}
package task;

import java.util.HashMap;

import analyzer.dist.DistanceCalculator;
import analyzer.dist.SyntacticAnalyzer;

class DistanceTask extends Task {
	public static final String NAME = "dist"; 
	
	@Override
	public void doTask(HashMap<String, String> option) {
		String method = option.get("method");
		if(method == null) {
			log.severe("no method chosen");
			return;
		}
		DistanceCalculator calc = null;
		switch(method) {
		case SyntacticAnalyzer.NAME:
			calc = new SyntacticAnalyzer(option);
			break;
		}
		if(calc == null) {
			log.severe("wrong method name: " + method);
			return;
		}
		
		double[][] dist = calc.getDistanceTable(in.readAllLines());
		out.printlnJson(dist);
	}
}
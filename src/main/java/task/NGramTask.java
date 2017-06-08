package task;

import java.util.HashMap;

import analyzer.dist.NGram;

class NGramTask extends Task {
	public static final String NAME = "ngram";
	private static final String DEFAULT_WHITELIST = "whitelist.list";
	@Override
	public void doTask(HashMap<String, String> option) {
		String nValue = option.get("n");
		final int n = toInt(nValue);
		if(n <= 0) {
			log.severe("Illegal value of n: " + nValue);
			return;
		}
		String whitelistFile = option.getOrDefault("whitelist", DEFAULT_WHITELIST);
		
		double[][] dist = getResult(whitelistFile, n);
		out.printlnJson(dist);
	}
	
	protected double[][] getResult(String whitelistFile, int n) {
		return new NGram(whitelistFile, n).getDistanceTable(in.readAllLines());
	}
	
	private int toInt(String value) {
		if(value == null) return 0;
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
}
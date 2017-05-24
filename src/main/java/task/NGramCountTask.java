package task;

import analyzer.dist.NGramCount;

class NGramCountTask extends NGramTask {
	public static final String NAME = "ngcound";
	@Override
	protected double[][] getResult(String whitelistFile, int n) {
		return new NGramCount(whitelistFile, n).getDistanceTable(in.readAllLines());
	}
}
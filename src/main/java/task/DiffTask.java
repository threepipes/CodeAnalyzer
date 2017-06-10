package task;

import analyzer.diff.DiffCalculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

class DiffTask extends Task {
	public static final String NAME = "diff";
	private static Logger log = Logger.getGlobal();
	
	@Override
	public void doTask(HashMap<String, String> option) {
		DiffCalculator diff = DiffCalculator.getCalculator(
				option.getOrDefault("method", ""));
		if(diff == null) {
			log.severe("failed to get method");
			return;
		}
		List<String[]> compareFileLists = in.readCsvLike(",");
		List<String> compareResult = new ArrayList<>();
		for(String[] compareList: compareFileLists) {
			compareResult.add(diff.getDiffResult(compareList));
		}
		out.println(compareResult);
	}
}
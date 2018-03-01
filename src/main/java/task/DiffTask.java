package task;

import analyzer.diff.DiffCalculator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.IntStream;

class DiffTask extends Task {
	public static final String NAME = "diff";
	private static Logger log = Logger.getGlobal();
	
	@Override
	public void doTask(HashMap<String, String> option) {
		DiffCalculator diff = DiffCalculator.getCalculator(
				option.getOrDefault("method", ""));
		log.finest("task diff started");
		if(diff == null) {
			log.severe("failed to get method");
			return;
		}
		List<String[]> compareFileLists = in.readCsvLike(",");
//		List<String> compareResult = new ArrayList<>();
		String[] compareResult = new String[compareFileLists.size()];
		IntStream.range(0, compareResult.length).parallel().forEach(i -> {
			compareResult[i] = diff.getDiffResult(compareFileLists.get(i));
		});
		log.finest("task diff completed");
		out.println(Arrays.toString(compareResult));
	}
}
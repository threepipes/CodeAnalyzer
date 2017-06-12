package analyzer.diff;

import utils.LevenshteinDistance;
import utils.Lexer;

import java.io.File;

class LevenshteinDiff implements DiffCalculator {
    public static final String NAME = "leven";
    @Override
    public String getDiffResult(String... filelist) {
        log.finest("starting getdiff result in LevenshteinDiff...");
        if(filelist.length == 1) return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        String[] pre = generateTokenList(filelist[0]);
        for(int i = 1; i < filelist.length; i++) {
            String[] nxt = generateTokenList(filelist[i]);
            if(i > 1) sb.append(",\n");
            sb.append(getDiff(pre, nxt));
        }
        sb.append("]\n");
        return sb.toString();
    }

    private String getDiff(String[] src, String[] dst) {
        LevenshteinDistance<String> leven = new LevenshteinDistance<>(src, dst);
        return String.valueOf(leven.getDist());
    }

    private String[] generateTokenList(String filename) {
        Lexer lexer = new Lexer(new File(filename));
        return lexer.getTokenList().toArray(new String[0]);
    }
}

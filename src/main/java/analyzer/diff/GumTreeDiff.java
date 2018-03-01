package analyzer.diff;

import com.github.gumtreediff.actions.ActionGenerator;
import com.github.gumtreediff.client.Run;
import com.github.gumtreediff.gen.srcml.SrcmlCppTreeGenerator;
import com.github.gumtreediff.io.ActionsIoUtils;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.TreeContext;

import java.io.IOException;
import java.io.StringWriter;
import java.util.stream.IntStream;

public class GumTreeDiff implements DiffCalculator {
    public static final String NAME = "gumtree";
    private static final String EMPTY_RESULT = "{\"matches\":[], \"actions\":[]}";
    public GumTreeDiff() {
        Run.initGenerators();
    }

    @Override
    public String getDiffResult(String... filelist) {
        log.finest("starting getdiff result in GumTreeDiff...");
        if(filelist.length == 1) return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        String[] result = new String[filelist.length - 1];
        IntStream.range(1, filelist.length).parallel().forEach(i -> {
            System.out.println(i);
            TreeContext pre = generateITree(filelist[i - 1]);
            TreeContext nxt = generateITree(filelist[i]);
            String res = "";
            if (i > 1) res += ",\n";
            log.finest(String.format("comparing: %s(%d) and %s(%d)",
                    filelist[i - 1], pre.hashCode(), filelist[i], nxt.hashCode()));
            res += getDiff(pre, nxt);
            result[i - 1] = res;
        });
        for(String res: result) {
            sb.append(res);
        }
        sb.append("]\n");
        log.finest("successful in getdiff");
        return sb.toString();
    }

    private String getDiff(TreeContext src, TreeContext dst) {
        if(src == null || dst == null) {
            log.warning("Src or dst are null. Return empty result.");
            return EMPTY_RESULT;
        }
        Matcher m = Matchers.getInstance().getMatcher(src.getRoot(), dst.getRoot());
        m.match();
        ActionGenerator g = new ActionGenerator(src.getRoot(), dst.getRoot(), m.getMappings());
        g.generate();
        StringWriter writer = new StringWriter();
        try {
            ActionsIoUtils.toJson(src, g.getActions(), m.getMappings()).writeTo(writer);
        } catch (Exception e) {
            log.severe(e.toString() + "\nfailed getting diff");
            log.fine(src.toString() + "\n" + dst.toString());
        }
        return writer.toString();
    }

    public static TreeContext generateITree(String filename) {
        log.finest("generating ITree: " + filename);
        try {
            return new SrcmlCppTreeGenerator().generateFromFile(filename);
        } catch (NullPointerException | IOException e) {
            log.warning("failed to generate ITree...");
            log.severe(e.toString());
            return null;
        }
    }
}

class Result implements Comparable<Result> {
    int idx;
    String result;
    public Result(int idx, String result) {
        this.idx = idx;
        this.result = result;
    }

    @Override
    public int compareTo(Result o) {
        return idx - o.idx;
    }
}
package analyzer.diff;


import com.github.gumtreediff.actions.ActionGenerator;
import com.github.gumtreediff.client.Run;
import com.github.gumtreediff.gen.Generators;
import com.github.gumtreediff.io.ActionsIoUtils;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.TreeContext;

import java.io.IOException;
import java.io.StringWriter;
import java.util.logging.Logger;

/**
 * ソースコードの差分を計算する
 *   leven: トークンによるlevenshtein距離の計算
 *   gumtree: gumtreediffを利用した計算
 */
public interface DiffCalculator {
    static Logger log = Logger.getGlobal();
    static DiffCalculator getCalculator(String name) {
        switch (name) {
            case LevenshteinDiff.NAME:
                return new LevenshteinDiff();
            case GumTreeDiff.NAME:
                return new GumTreeDiff();
        }
        log.severe("Wrong method name: " + name
                + "\nexpected [leven, gumtree]");
        return null;
    }
    String getDiffResult(String... filelist);
}

class LevenshteinDiff implements DiffCalculator {
    public static final String NAME = "leven";
    @Override
    public String getDiffResult(String... filelist) {
        // TODO
        return "";
    }
}

class GumTreeDiff implements DiffCalculator {
    public static final String NAME = "gumtree";
    public GumTreeDiff() {
        Run.initGenerators();
    }

    @Override
    public String getDiffResult(String... filelist) {
        log.finest("starting getdiff result in GumTreeDiff...");
        if(filelist.length != 2) {
            log.severe("size of filelist must be 2, but found: " + filelist.length);
            log.fine(filelist.toString());
        }
        try {
            TreeContext src = generateITree(filelist[0]);
            TreeContext dst = generateITree(filelist[1]);
            String diff = getDiff(src, dst);
            return diff;
        } catch (IOException e) {
            log.severe(e.toString() + "\nPlease check if your filelist is valid.");
            log.fine(filelist.toString());
        }
        return null;
    }

    private String getDiff(TreeContext src, TreeContext dst) {
        Matcher m = Matchers.getInstance().getMatcher(src.getRoot(), dst.getRoot());
        ActionGenerator g = new ActionGenerator(src.getRoot(), dst.getRoot(), m.getMappings());
        StringWriter writer = new StringWriter();
        try {
            ActionsIoUtils.toJson(src, g.getActions(), m.getMappings()).writeTo(writer);
        } catch (Exception e) {
            log.severe(e.toString() + "\nfailed getting diff");
            log.fine(src.toString() + "\n" + dst.toString());
        }
        return writer.toString();
    }

    private TreeContext generateITree(String filename) throws IOException{
        return Generators.getInstance().getTree(filename);
    }
}
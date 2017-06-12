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

class GumTreeDiff implements DiffCalculator {
    public static final String NAME = "gumtree";
    public GumTreeDiff() {
        Run.initGenerators();
    }

    @Override
    public String getDiffResult(String... filelist) {
        log.finest("starting getdiff result in GumTreeDiff...");
        if(filelist.length == 1) return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        try {
            TreeContext pre = generateITree(filelist[0]);
            for(int i = 1; i < filelist.length; i++) {
                TreeContext nxt = generateITree(filelist[i]);
                if(i > 1) sb.append(",\n");
                sb.append(getDiff(pre, nxt));
            }
            sb.append("]\n");
            return sb.toString();
        } catch (IOException e) {
            log.severe(e.toString() + "\nPlease check if your filelist is valid.");
            log.fine(filelist.toString());
        }
        return null;
    }

    private String getDiff(TreeContext src, TreeContext dst) {
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

    private TreeContext generateITree(String filename) throws IOException{
        return new SrcmlCppTreeGenerator().generateFromFile(filename);
    }
}

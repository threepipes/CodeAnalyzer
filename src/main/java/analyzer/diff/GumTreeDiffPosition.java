package analyzer.diff;

import com.github.gumtreediff.actions.ActionGenerator;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;
import net.arnx.jsonic.JSON;
import utils.INodeChecker;
import utils.NodeChecker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

import static analyzer.diff.GumTreeDiff.generateITree;

public class GumTreeDiffPosition implements DiffCalculator {
    public static final String NAME = "gumtreepos";
    private INodeChecker chekcer;
    public GumTreeDiffPosition() {
        chekcer = new NodeChecker();
    }

    @Override
    public String getDiffResult(String... filelist) {
        log.finest("starting getdiffpos in GumTreeDiff");
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
            res += getDiffPosition(pre, nxt, chekcer);
            result[i - 1] = res;
        });
        for(String res: result) {
            sb.append(res);
        }
        sb.append("]\n");
        log.finest("successful in getdiffpos");
        return sb.toString();
    }

    public String getDiffPosition(TreeContext src, TreeContext dst, INodeChecker checker) {
        List<HashMap<String, Object>> diffList = new ArrayList<>();
        List<Action> actions = getActions(src, dst);
        for (Action act: actions) {
            ITree node = act.getNode();
            String actname = act.getName();
            TreeContext context = actname.equals("INS") ? dst : src;
            List<String> info = checker.nodeType(node, context);
            HashMap<String, Object> map = new HashMap<>();
            map.put("type", actname);
            map.put("info", info);
            diffList.add(map);
        }
        return JSON.encode(diffList);
    }

    private List<Action> getActions(TreeContext src, TreeContext dst) {
        Matcher m = Matchers.getInstance().getMatcher(src.getRoot(), dst.getRoot());
        m.match();
        ActionGenerator g = new ActionGenerator(src.getRoot(), dst.getRoot(), m.getMappings());
        g.generate();
        return g.getActions();
    }
}



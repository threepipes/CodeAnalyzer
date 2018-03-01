package tester;

import analyzer.diff.GumTreeDiff;
import com.github.gumtreediff.actions.ActionGenerator;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;
import net.arnx.jsonic.JSON;
import utils.NodeChecker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

import static analyzer.diff.GumTreeDiff.generateITree;

public class TreeTester {
    public static void main(String[] args) {
        TreeWalker walker = new TreeWalker();
//        walker.testDiff();
        walker.dumpTree();
    }
}

class TreeWalker {
    String src, dst;
    public TreeWalker() {
        String[] path = new String[2];
        for(int i = 0; i < 2; i++) {
            path[i] = ClassLoader.getSystemResource(
                    String.format("sample/crlf_sample_%02d.cpp", i)
            ).getPath();
        }
        src = path[0];
        dst = path[1];
    }

    public void dumpTree() {
        TreeContext tree = generateITree(src);
        if(tree == null) return;
        ITree root = tree.getRoot();
        for(ITree node: root.preOrder()) {
            List<ITree> toRoot = walk2Root(node);
            for(ITree v: toRoot) {
                System.out.print("[" + nodeToString(v, tree) + "],\t");
            }
            System.out.println();
        }
    }

    private String nodeToString(ITree node, TreeContext tree) {
//        return tree.getTypeLabel(node)
//                    + " " + node.getId()
//                    + " " + node.getLabel() + " " + node.toShortString()
//                    + " " + node.toPrettyString(tree);
        return node.toPrettyString(tree);
    }

    public void test() {
        TreeContext srcTree = generateITree(src);
        TreeContext dstTree = generateITree(dst);
        List<Action> actions = getActions(srcTree, dstTree);
        for (Action act: actions) {
            ITree node = act.getNode();
            String actname = act.getName();
            TreeContext context = actname.equals("INS") ? dstTree : srcTree;
            List<String> info = new NodeChecker().nodeType(node, context);
            System.out.println(actname + " " + info);
        }
    }

    ITree getRoot(ITree node) {
        int dep = node.getDepth();
        for (int i = 0; i < dep; i++) {
            node = node.getParent();
        }
        return node;
    }

    List<ITree> walk2Root(ITree node) {
        List<ITree> list = new ArrayList<>();
        int dep = node.getDepth();
        list.add(node);
        for (int i = 0; i < dep; i++) {
            node = node.getParent();
            list.add(node);
        }
        return list;
    }

    public void testDiff() {
        String res = getDiffPosition(generateITree(src), generateITree(dst));
        System.out.println(res);
    }

    public String getDiffPosition(TreeContext src, TreeContext dst) {
        List<HashMap<String, Object>> diffList = new ArrayList<>();
        List<Action> actions = getActions(src, dst);
        for (Action act: actions) {
            ITree node = act.getNode();
            String actname = act.getName();
            TreeContext context = actname.equals("INS") ? dst : src;
            List<String> info = new NodeChecker().nodeType(node, context);
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

    private void walkTree(ITree current, ITree parent, Consumer<ITree> func) {
        func.accept(current);
        for(ITree next: current.getChildren()) {
            walkTree(next, current, func);
        }
    }
}


package tester;

import analyzer.diff.GumTreeDiff;
import com.github.gumtreediff.actions.ActionGenerator;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public class TreeTester {
    public static void main(String[] args) {
        TreeWalker walker = new TreeWalker();
        walker.dumpTree();
    }
}

class TreeWalker {
    String src, dst;
    public TreeWalker() {
        String[] path = new String[2];
        for(int i = 0; i < 2; i++) {
            path[i] = ClassLoader.getSystemResource(
                    String.format("sample/crlf_sample_%02d.cpp", 0)
            ).getPath();
        }
        src = path[0];
        dst = path[1];
    }

    private TreeContext generateITree(String path) {
        try {
            return GumTreeDiff.generateITree(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void dumpTree() {
        TreeContext tree = generateITree(src);
        if(tree == null) return;
        ITree root = tree.getRoot();
        for(ITree node: root.preOrder()) {
            System.out.println(tree.getTypeLabel(node));
        }
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

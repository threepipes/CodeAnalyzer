package task;

import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;
import utils.MultiSet;
import utils.Printer;

import java.util.HashMap;

public class TabCountTask implements TreeSubTask {
    public static final String NAME = "tabcount";

    @Override
    public HashMap treeTask(TreeContext tree, Printer out) {
        ms = new MultiSet<>();
        this.tree = tree;
        walk(tree.getRoot(), 0);
        return ms;
    }

    TreeContext tree;
    MultiSet<Integer> ms;
    private void walk(ITree current, int depth) {
        ms.add(depth);
        for (ITree next: current.getChildren()) {
            walk(next, depth + (tree.getTypeLabel(current).equals("block") ? 1 : 0));
        }
    }
}
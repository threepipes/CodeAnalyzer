package utils;

import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;

import java.util.ArrayList;
import java.util.List;

public class NodeCheckerAll implements INodeChecker {
    @Override
    public List<String> nodeType(ITree node, TreeContext context) {
        int dep = node.getDepth();
        List<String> list = new ArrayList<>();
        for (int i = 0; i <= dep; i++) {
            int typeId = node.getType();
            String typeLabel = context.getTypeLabel(typeId);
            list.add(typeLabel);
            node = node.getParent();
        }
        return list;
    }
}

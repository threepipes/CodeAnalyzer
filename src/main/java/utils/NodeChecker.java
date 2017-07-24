package utils;

import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;

import java.util.ArrayList;
import java.util.List;

public class NodeChecker implements INodeChecker {
    @Override
    public List<String> nodeType(ITree node, TreeContext context) {
        int dep = node.getDepth();
        /*
        condition: ifやforの条件
        control: for(whileも？)のループ制御部
        index: []の中身
        expr: 式
        decl: 宣言
         */
        String[] cond = {
                "condition",
                "control",
                "index",
                "expr",
                "decl"
        };
        List<String> list = new ArrayList<>();
        for (int i = 0; i <= dep; i++) {
            int typeId = node.getType();
            String typeLabel = context.getTypeLabel(typeId);
            for(String c: cond) {
                if(c.equals(typeLabel)) {
                    list.add(c);
                    break;
                }
            }
            node = node.getParent();
        }
        return list;
    }
}
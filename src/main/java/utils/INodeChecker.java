package utils;

import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;

import java.util.List;

public interface INodeChecker {
    List<String> nodeType(ITree node, TreeContext context);
}

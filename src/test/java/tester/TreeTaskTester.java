package tester;

import com.github.gumtreediff.tree.TreeContext;
import task.TabCountTask;
import utils.Printer;
import utils.SampleSource;

public class TreeTaskTester {
    public static void main(String[] args) {
        TabCountTester tester = new TabCountTester();
        tester.testTabCount();
    }
}

class TabCountTester {
    public void testTabCount() {
        TabCountTask task = new TabCountTask();
        TreeContext tree = SampleSource.getSourceTree(0);
        Printer out = new Printer();
        task.treeTask(tree, out);
    }
}

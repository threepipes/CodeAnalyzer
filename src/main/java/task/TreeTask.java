package task;

import analyzer.diff.GumTreeDiff;
import com.github.gumtreediff.tree.TreeContext;
import utils.Printer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TreeTask extends Task{
    public static final String NAME = "tree";

    public void doTask(HashMap<String, String> option) {
        // do subtask
        String opt = option.getOrDefault("subtask", "");
        TreeSubTask subtask = null;
        if(opt.equals(TabCountTask.NAME)) {
            subtask = new TabCountTask();
        }
        if(subtask == null) return;
        List<HashMap> result = new ArrayList<>();
        for(String filename: in.readAllLines()) {
            TreeContext tree = GumTreeDiff.generateITree(filename);
            HashMap map = subtask.treeTask(tree, out);
            result.add(map);
        }
        out.printlnJson(result);
        out.flush();
    }
}

interface TreeSubTask {
    String NAME = "tabcount";
    HashMap treeTask(TreeContext tree, Printer out);
}

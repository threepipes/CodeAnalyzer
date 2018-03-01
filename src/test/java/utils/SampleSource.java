package utils;

import analyzer.diff.GumTreeDiff;
import com.github.gumtreediff.tree.TreeContext;

public class SampleSource {
    public static String getSampleSource(int id) {
        return ClassLoader.getSystemResource(
                    String.format("sample/crlf_sample_%02d.cpp", id)
        ).getPath();
    }

    public static TreeContext getSourceTree(int id) {
        return GumTreeDiff.generateITree(getSampleSource(id));
    }
}

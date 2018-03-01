package analyzer.diff;


import java.util.logging.Logger;

/**
 * ソースコードの差分を計算する
 *   leven: トークンによるlevenshtein距離の計算
 *   gumtree: gumtreediffを利用した計算
 */
public interface DiffCalculator {
    static Logger log = Logger.getGlobal();
    static DiffCalculator getCalculator(String name) {
        switch (name) {
            case LevenshteinDiff.NAME:
                return new LevenshteinDiff();
            case GumTreeDiff.NAME:
                return new GumTreeDiff();
            case GumTreeDiffPosition.NAME:
                return new GumTreeDiffPosition();
        }
        log.severe("Wrong method name: " + name
                + "\nexpected [leven, gumtree]");
        return null;
    }
    String getDiffResult(String... filelist);
}

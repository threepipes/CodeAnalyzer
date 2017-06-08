package analyzer.diff;

import org.junit.Test;
import utils.Reader;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.Assert.*;

public class GumTreeDiffTest {
    @Test
    public void testDiff() {
        DiffCalculator diff = DiffCalculator.getCalculator(GumTreeDiff.NAME);
        String path = ClassLoader.getSystemResource("compfile.txt").getPath();
        try {
            Reader in = new Reader(path);
            List<String[]> list = in.readCsvLike(" ");
            for(String[] files: list) {
                for(int i = 0; i < files.length; i++) {
                    files[i] = ClassLoader.getSystemResource(files[i]).getPath();
                }
                String result = diff.getDiffResult(files);
                System.out.println("[result]");
                System.out.println(result);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail();
        }
    }
}

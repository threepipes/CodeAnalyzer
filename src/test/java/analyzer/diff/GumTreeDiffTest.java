package analyzer.diff;

import junit.framework.TestCase;
import org.junit.Test;
import utils.Reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

import static org.junit.Assert.*;

public class GumTreeDiffTest extends TestCase {
    private Logger log;
    public GumTreeDiffTest() {
        try {
            Level level = Level.ALL;
            String filename = "./log.txt";
            log = Logger.getGlobal();
            log.addHandler(new StreamHandler() {
                {
                    setOutputStream(new FileOutputStream(filename));
                    setLevel(level);
                }
            });
            log.setUseParentHandlers(false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDiff() {
        Logger log = Logger.getGlobal();
        log.info("testing");
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
//                System.out.println(result);
                log.info(result);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail();
        }
    }
}

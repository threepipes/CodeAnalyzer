package analyzer.diff;

import com.github.gumtreediff.tree.Tree;
import com.github.gumtreediff.tree.TreeContext;
import junit.framework.TestCase;
import net.arnx.jsonic.JSON;
import org.junit.Test;
import utils.Reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
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
                List<HashMap> map = JSON.decode(result);
                List actions = (List)map.get(0).getOrDefault("actions", new ArrayList());
                System.out.println("[result]");
//                System.out.println(result);
                log.info(JSON.encode(actions));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGenerateITree()
            throws NoSuchMethodException,
            IllegalAccessException,
            InvocationTargetException {
        Logger log = Logger.getGlobal();
        log.info("test generate itree");
        GumTreeDiff gumtree = new GumTreeDiff();
        Method method = GumTreeDiff.class.getDeclaredMethod("generateITree", String.class);
        method.setAccessible(true);

        for(int i = 0; i < 2; i++) {
            String path = ClassLoader.getSystemResource(String.format("sample/crlf_sample_%02d.cpp", i)).getPath();
            log.info("generating: " + path);
            TreeContext tree = generateITree(path, method, gumtree);
            assertNotNull(tree);
            System.out.println(tree);
        }

        log.info("success generating itree");
    }

    private TreeContext generateITree(String path, Method method, GumTreeDiff gumtree)
            throws IllegalAccessException,
            InvocationTargetException {
        return (TreeContext)method.invoke(gumtree, path);
    }

    @Test
    public void testCheckITreeChange()
            throws NoSuchMethodException,
            IllegalAccessException,
            InvocationTargetException {

        Logger log = Logger.getGlobal();
        log.info("test checking itree");
        GumTreeDiff gumtree = new GumTreeDiff();
        Method method = GumTreeDiff.class.getDeclaredMethod("generateITree", String.class);
        method.setAccessible(true);

        List<TreeContext> list = new ArrayList<>();
        for(int i = 0; i < 2; i++) {
            String path = ClassLoader.getSystemResource(String.format("sample/crlf_sample_%02d.cpp", i)).getPath();
            log.info("generating: " + path);
            TreeContext tree = generateITree(path, method, gumtree);
            list.add(tree);
            assertNotNull(tree);
        }

        Method diffMethod = GumTreeDiff.class.getDeclaredMethod("getDiff", TreeContext.class, TreeContext.class);
        diffMethod.setAccessible(true);
        String diff = (String)diffMethod.invoke(gumtree, list.get(0), list.get(1));

        List<TreeContext> list2 = new ArrayList<>();
        for(int i = 0; i < 2; i++) {
            String path = ClassLoader.getSystemResource(String.format("sample/crlf_sample_%02d.cpp", i)).getPath();
            TreeContext tree = generateITree(path, method, gumtree);
            list2.add(tree);
        }

        String diff2 = (String)diffMethod.invoke(gumtree, list.get(0), list2.get(0));
        System.out.println(diff2);

        log.info("finish checking chenge");
    }
}

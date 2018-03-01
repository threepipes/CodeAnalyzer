package analyzer;

import junit.framework.TestCase;
import org.junit.Test;
import utils.FileUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class CodeAnalyzerTest extends TestCase{
    private static final String DIR = "./testDir/";
    private static final String LOG_PATH = DIR + "log.txt";
    private static final String INPUT_PATH = DIR + "input.txt";
    private static final String OUTPUT_PATH = DIR + "output.txt";

    private static final String SOURCE_A = "" +
            "#include <iostream>\n" +
            "using namespace std;\n" +
            "void main() {\n" +
            "    int n, m, a;\n" +
            "    cin >> n >> m;\n" +
            "    cout << n + m << endl;\n" +
            "}";
    private static final String SOURCE_B = "" +
            "#include <iostream>\n" +
            "using namespace std;\n" +
            "void main() {\n" +
            "    int n, m;\n" +
            "    cin >> n >> m;\n" +
            "    n = n * n;\n" +
            "    cout << n * m << endl;\n" +
            "}";

    public CodeAnalyzerTest() {
        initTest();
    }

    private void initTest() {
        File testDir = new File(DIR);
        if(!testDir.exists()) testDir.mkdir();
    }

    private CodeAnalyzer generateInstance(HashMap<String, String> option)
            throws Exception{
        HashMap<String, String> base = new HashMap<>();
        // add initial option
        base.put("logfile", LOG_PATH);
        base.put("loglevel", "all");
        base.put("inpath", INPUT_PATH);
        base.put("outpath", OUTPUT_PATH);

        // overwrite option
        base.putAll(option);
        String[] args = new String[base.size()];
        int idx = 0;
        for(Map.Entry<String, String> e: base.entrySet()) {
            args[idx++] = e.getKey() + "=" + e.getValue();
        }
        return new CodeAnalyzer(args);
    }

    void generateTestSource() {
        String pathA = DIR + "aaa.cpp";
        String pathB = DIR + "bbb.cpp";
        String fileList = pathA + "," + pathB + "\n";
        String fileA = SOURCE_A;
        String fileB = SOURCE_B;
        FileUtil.writeToFile(fileList, INPUT_PATH);
        FileUtil.writeToFile(fileA, pathA);
        FileUtil.writeToFile(fileB, pathB);
    }

    @Test
    public void testGumTreeDiff() {
        HashMap<String, String> option = new HashMap<String, String>() {
            {
                put("task", "diff");
                put("method", "gumtree");
            }
        };
        generateTestSource();
        try {
            CodeAnalyzer analyzer = generateInstance(option);
            analyzer.execute();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        String result = FileUtil.readFromFile(OUTPUT_PATH);
        System.out.println(result);
    }

    @Test
    public void testGumTreeDiffPos() {
        HashMap<String, String> option = new HashMap<String, String>() {
            {
                put("task", "diff");
                put("method", "gumtreepos");
            }
        };
        generateTestSource();
        try {
            CodeAnalyzer analyzer = generateInstance(option);
            analyzer.execute();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        String result = FileUtil.readFromFile(OUTPUT_PATH);
        System.out.println("GumTreePos");
        System.out.println(result);
    }
}

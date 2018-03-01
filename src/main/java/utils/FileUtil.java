package utils;

import java.io.*;

public class FileUtil {
    public static void writeToFile(String value, String filename) {
        try(BufferedWriter out = new BufferedWriter(new FileWriter(new File(filename)))) {
            out.write(value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFromFile(String filename) {
        StringBuilder sb = new StringBuilder();
        try(BufferedReader in = new BufferedReader(new FileReader(new File(filename)))) {
            for(int c = in.read(); c != -1; c = in.read()) {
                sb.appendCodePoint(c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}

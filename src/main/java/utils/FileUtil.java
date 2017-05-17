package utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileUtil {
	static List<String> loadLines(String filename){
		Logger log = Logger.getGlobal();
		log.log(Level.FINEST, "loadLines: loading " + filename);
		List<String> list = new ArrayList<>();
		try {
			BufferedReader in =
					new BufferedReader(
					new InputStreamReader(
					new FileInputStream(filename)
					));
			for(String line=in.readLine();
					line!=null; line=in.readLine()){
				list.add(line);
			}
			in.close();
		} catch (FileNotFoundException e) {
			Logger.getGlobal().log(Level.SEVERE, "Error: file not found: " + filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
}

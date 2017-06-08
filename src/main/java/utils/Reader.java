package utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Reader extends BufferedReader {
	private String filename;
	public Reader(String filename) throws FileNotFoundException {
		super(new FileReader(filename));
		this.filename = filename;
	}
	
	public Reader() {
		super(new InputStreamReader(System.in));
		this.filename = "System.in";
	}
	
	public List<String> readAllLines() {
		Logger log = Logger.getGlobal();
		log.log(Level.FINEST, "loadLines: loading " + filename);
		List<String> list = new ArrayList<>();
		try {
			for(String line=this.readLine();
					line!=null; line=this.readLine()){
				list.add(line);
			}
		} catch (FileNotFoundException e) {
			Logger.getGlobal().log(Level.SEVERE, "Error: file not found: " + filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<String[]> readCsvLike(String separator) {
		Logger log = Logger.getGlobal();
		log.finest("readCsvLike: loading: " + filename);
		List<String[]> result = new ArrayList<>();
		try {
			for(String line=this.readLine();
				line!=null; line=this.readLine()){
				result.add(line.split(separator));
			}
		} catch (FileNotFoundException e) {
			Logger.getGlobal().log(Level.SEVERE, "Error: file not found: " + filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}

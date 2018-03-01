package utils;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import net.arnx.jsonic.JSON;

public class Printer extends PrintWriter{
	public Printer(String filename) throws IOException {
		super(new BufferedWriter(
				new OutputStreamWriter(
						new FileOutputStream(new File(filename)), "utf-8"
				)));
	}
	
	public Printer() {
		super(new BufferedOutputStream(System.out));
	}
	
	public void printlnJson(Jsonable obj) {
		String jsonStr = JSON.encode(obj);
		this.println(jsonStr);
	}
	
	public void printlnJson(double[][] arr) {
		String jsonStr = JSON.encode(arr);
		this.println(jsonStr);
	}
	
	public void printlnJson(Collection obj) {
		String jsonStr = JSON.encode(obj);
		this.println(jsonStr);
	}
}

/**
 * JSONICによってJson化可能であることを示すためのマーカーインターフェース．
 * このインターフェースをimplementsしたクラスは，Json化すべきフィールドの
 * getter/setterを実装する義務がある．
 *
 */
interface Jsonable {}

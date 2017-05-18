package utils;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import net.arnx.jsonic.JSON;

public class Printer extends PrintWriter{
	public Printer(String filename) throws IOException {
		super(new BufferedWriter(new FileWriter(filename)));
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
}

/**
 * JSONICによってJson化可能であることを示すためのマーカーインターフェース．
 * このインターフェースをimplementsしたクラスは，Json化すべきフィールドの
 * getter/setterを実装する義務がある．
 *
 */
interface Jsonable {}

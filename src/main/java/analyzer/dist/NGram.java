package analyzer.dist;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Queue;

import utils.MultiSet;

public class NGram extends DistanceCalculator {
	public static final String REPLACE = "$";
	
	private HashSet<String> whitelist;
	private HashMap<String, Integer> mapping;
	private List<String> table;
	
	private int n;
	
	public NGram(String whitelistFile, int n) {
		this.n = n;
		whitelist = new HashSet<>();
		try(BufferedReader in = new BufferedReader(new FileReader(new File(whitelistFile)))){
			for(String line=in.readLine(); line!=null; line=in.readLine()){
				whitelist.add(line);
			}
		} catch (FileNotFoundException e) {
			System.err.println("File not found: " + whitelistFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		initIdTable();
	}
	
	protected HashSet<String> getWhitelist() {
		return (HashSet<String>) whitelist.clone();
	}
	
	@Override
	protected Vector vectorize(List<String> tokens) {
		NGramVector vector = new NGramVector();
		Queue<Integer> tmpVec = new ArrayDeque<>();
		for(String token: tokens){
			if(skip(token)) continue;
			if(needReplace(token)) token = REPLACE;
			final int id = toId(token);
			tmpVec.add(id);
			if(tmpVec.size() > n) tmpVec.poll();
			final int hash = toHash(tmpVec);
			vector.add(hash);
		}
		return vector;
	}

	public void initIdTable(){
		mapping = new HashMap<>();
		table = new ArrayList<>();
	}
	
	private int toId(String token) {
		if(mapping.containsKey(token)){
			return mapping.get(token);
		}else{
			int id = table.size();
			mapping.put(token, id);
			table.add(token);
			return id;
		}
	}
	
	static final int BASE = 257;
	private int toHash(Queue<Integer> ids) {
		int hash = 0;
		for(int id: ids){
			hash *= BASE;
			hash += id;
		}
		return hash;
	}
	
	private boolean skip(String token){
		return token.indexOf('#') >= 0;
	}
	
	private boolean needReplace(String token){
		if(whitelist.contains(token)) return false;
		char c = token.charAt(0);
		return c == '"' || Character.isLetterOrDigit(c);
	}
}

class NGramVector extends MultiSet<Integer> implements Vector {
	@Override
	public double dist(Vector vec) {
		if(!(vec instanceof NGramVector)) return 1;
		NGramVector v = (NGramVector) vec;
		double numer = 0;
		double denom1 = 0, denom2 = 0;
		for(Entry<Integer, Integer> e: this.entrySet()) {
			final int id = e.getKey();
			final int freq = e.getValue();
			denom1 += freq * freq;
			numer += freq * v.get(id);
		}
		for(int freq: v.values()){
			denom2 += freq * freq;
		}
		double denom = Math.sqrt(denom1) * Math.sqrt(denom2);
		if(denom == 0) return 0;
		return 1 - numer / denom;
	}
	
	@Override
	public Vector merge(Vector vec) {
		if(!(vec instanceof NGramVector)) return this;
		NGramVector v = (NGramVector) vec;
		for(Entry<Integer, Integer> e: v.entrySet()) {
			this.add(e.getKey(), e.getValue());
		}
		return this;
	}
}

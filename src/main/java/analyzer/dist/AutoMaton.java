package analyzer.dist;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface AutoMaton {
	public void reset();
	public void readToken(String token);
	public Vector getValue();
	
	public static List<AutoMaton> getter(String option) {
		List<AutoMaton> list = new ArrayList<>();
		if(option == null) {
			list.add(new IfCounter());
			list.add(new IndentCounter());
		} else {
			String[] select = option.split(",");
			for(String choice: select) {
				switch(choice) {
				case IfCounter.NAME:
					list.add(new IfCounter());
					break;
				case IndentCounter.NAME:
					list.add(new IndentCounter());
					break;
				}
			}
		}
		return list;
	}
}

class IndentCounter implements AutoMaton {
	public static final String NAME = "indent";
	/**
	 * ブロック内のみインデントを数える
	 *  -> if後の単一文などは，ifとまとめて1文とする
	 */
	private int depth;
//	private ListVector indents;
	private int[] indents;
	public IndentCounter() {
		depth = 0;
		indents = new int[10];
	}
	
	@Override
	public void reset() {
		Arrays.fill(indents, 0);
		depth = 0;
	}
	
	@Override
	public void readToken(String token) {
		if(token.equals("{")) {
			depth++;
		} else if(token.equals("}")) {
			depth--;
		}
		
		if(token.equals(";") || token.equals("{")) {
			// "}"も追加すべきかは議論
			indents[Math.min(9, depth)]++;
		}
	}
	
	@Override
	public Vector getValue() {
		return new ListVector(indents);
	}
	
	@Override
	public String toString() {
		return indents.toString();
	}
}

class IfCounter implements AutoMaton {
	public static final String NAME = "if";
	private int counter;
	public IfCounter() {
		counter = 0;
	}
	
	@Override
	public void reset() {
		counter = 0;
	}
	
	@Override
	public void readToken(String token) {
		if(token.equals("if")) counter++;
	}
	
	@Override
	public Vector getValue() {
		return new ListVector(new int[]{ counter });
	}
	
	@Override
	public String toString() {
		return String.format("[%d]", counter);
	}
}

class CCCounter {
	/**
	 * 2行
	 */
}


class ListVector extends ArrayList<Integer> implements Vector {
	public ListVector() {
		super();
	}
	
	public ListVector(int[] a) {
		super(a.length);
		for(int v: a) add(v);
	}
	
	public ListVector(List<Integer> list) {
		super(list);
	}
	
	@Override
	public double dist(Vector vec) {
		if(!(vec instanceof ListVector)) return 1;
		ListVector vs = (ListVector) vec;
		final int len = Math.min(vs.size(), this.size());
		double numer = 0;
		for(int i = 0; i < len; i++) {
			numer += vs.get(i) * this.get(i);
		}
		final double denom1 =   vs.stream().reduce(0, (e1, e2) -> e1 + sq(e2));
		final double denom2 = this.stream().reduce(0, (e1, e2) -> e1 + sq(e2));
		final double denom = Math.sqrt(denom1) * Math.sqrt(denom2);
		if(denom == 0) return 0;
		return 1 - numer / denom;
	}
	
	public int[] toIntArray() {
		int[] result = new int[this.size()];
		for(int i = 0; i < result.length; i++) {
			result[i] = this.get(i);
		}
		return result;
	}
	
	private static int sq(int a) {
		return a * a;
	}
	
	@Override
	public Vector merge(Vector vec) {
		if(!(vec instanceof ListVector)) return this;
		ListVector v = (ListVector) vec;
		this.addAll(v);
		return this;
	}
}

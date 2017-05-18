package analyzer.dist;
import java.util.ArrayList;
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
			// TODO select[]からインスタンス生成
		}
		return list;
	}
}

class IndentCounter implements AutoMaton {
	/**
	 * ブロック内のみインデントを数える
	 *  -> if後の単一文などは，ifとまとめて1文とする
	 */
	private int depth;
	private ListVector indents;
	public IndentCounter() {
		depth = 0;
		indents = new ListVector();
		indents.add(0);
	}
	
	@Override
	public void reset() {
		indents.clear();
		indents.add(0);
		depth = 0;
	}
	
	@Override
	public void readToken(String token) {
		if(token.equals("{")) {
			depth++;
			if(indents.size() < depth + 1) indents.add(0);
		} else if(token.equals("}")) {
			depth--;
		}
		
		if(token.equals(";") || token.equals("{")) {
			// "}"も追加すべきかは議論
			indents.set(depth, indents.get(depth) + 1);
		}
	}
	
	@Override
	public Vector getValue() {
		return indents;
	}
	
	@Override
	public String toString() {
		return indents.toString();
	}
}

class IfCounter implements AutoMaton {
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

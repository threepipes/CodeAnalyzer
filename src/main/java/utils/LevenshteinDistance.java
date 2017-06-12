package utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * 距離を求める場合，インスタンスを作成した後getDistを行う
 * 適用された手順(挿入・削除・置換)を得たい場合はfindOperationsを行う
 */
public class LevenshteinDistance<T>{
	T[] t1, t2;
	int len1, len2;
	int[][] table;
	int costIns, costDel, costRep;
	List<Operation> operation; 
	public LevenshteinDistance(T[] from, T[] to) {
		t2 = from;
		t1 = to;
		len1 = t1.length;
		len2 = t2.length;
		table = new int[len1+1][len2+1];
		costIns = costDel = costRep = 1;
		culcTable();
	}
	
	public LevenshteinDistance(T[] from, T[] to, int ins, int del, int rep) {
		t2 = from;
		t1 = to;
		len1 = t1.length;
		len2 = t2.length;
		table = new int[len1+1][len2+1];
		costIns = ins;
		costDel = del;
		costRep = rep;
		culcTable();
	}
	
	// 距離の計算
	private void culcTable(){
		for(int i=0; i<=len1; i++) table[i][0] = i;
		for(int i=0; i<=len2; i++) table[0][i] = i;
		for(int i=1; i<=len1; i++){
			for(int j=1; j<=len2; j++){
				final int cost = t1[i-1].equals(t2[j-1]) ? 0 : costRep;
				table[i][j] = Math.min(table[i-1][j]+costIns
						, Math.min(table[i][j-1]+costDel,
								table[i-1][j-1]+cost));
			}
		}
	}
	
	public int getDist(){
		return table[len1][len2];
	}
	
	public List<Operation> getOperations(){
		if(operation == null) operation = findOperations();
		return operation;
	}
	
	static final int[] revX = {-1, 0, -1};
	static final int[] revY = {0, -1, -1};
	static final String[] op= {"delete", "insert", "replace"};
	static final int DEL = 0;
	static final int INS = 1;
	static final int REP = 2;
	private List<Operation> findOperations(){
		List<Operation> res = new ArrayList<>();
		BIT bit = new BIT(Math.max(len1, len2)+1);
		for(int i=0; i<len2; i++){
			bit.add(i, 1);
		}
		int y = len1;
		int x = len2;
		while(y!=0 || x!=0){
			int nxt = -1;
			int min = Integer.MAX_VALUE;
			for(int i=0; i<3; i++){
				int ny = y+revY[i];
				int nx = x+revX[i];
				if(ny<0||nx<0) continue;
				if(table[ny][nx]<min){
					min = table[ny][nx];
					nxt = i;
				}
			}
			if(nxt==DEL){
				res.add(new Operation(t2[x-1], x-1, DEL));
			}else if(nxt==INS){
				res.add(new Operation(t1[y-1], x, INS));
			}else if(table[y][x]==table[y+revY[nxt]][x+revX[nxt]]+costRep){
				res.add(new Operation(t1[y-1], x-1, REP));
			}
			y = y+revY[nxt];
			x = x+revX[nxt];
		}
		Collections.reverse(res);
		for(Operation o: res){
			int tmp = o.idx;
			o.idx = bit.sum(tmp)-1;
			if(o.type==DEL){
				bit.add(tmp, -1);
			}else if(o.type==INS){
				bit.add(tmp, 1);
			}
			System.out.println(tmp);
		}
		return res;
	}
	
	class Operation{
		T token;
		int idx;
		int type;
		Operation(T token, int idx, int type){
			this.token = token;
			this.idx = idx;
			this.type = type;
		}
		@Override
		public String toString() {
			return "["+token+", "+idx+", "+op[type]+"]";
		}
	}
}
class BIT{
	int n;
	int[] bit;
	public BIT(int n){
		this.n = n;
		bit = new int[n+1];
	}
	
	void add(int idx, int val){
		for(int i=idx+1; i<=n; i+=i&(-i)) bit[i-1] += val;
	}
	
	int sum(int idx){
		int res = 0;
		for(int i=idx+1; i>0; i-=i&(-i)) res += bit[i-1];
		return res;
	}
	
	int sum(int begin, int end){
		if(begin == 0) return sum(end);
		return sum(end)-sum(begin-1);
	}
	
	int find(int sum){
		// find minimum index which sum equals given value
		int low = 0;
		int up  = n;
		int tmp = -1;
		int oldIdx = -1;
		if(sum == 0) return 0;
		while(low < up){
			int mid = (low+up)/2;
			tmp = sum(mid);
			if(tmp < sum){
				if(oldIdx > 0 && low == mid) break;
				low = mid;
			}else{
				if(tmp == sum) oldIdx = mid;
				up = mid;
			}
		}
		if(oldIdx > 0 && sum(oldIdx-1) == sum) oldIdx--;
		return oldIdx;
	}
}

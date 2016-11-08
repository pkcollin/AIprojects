
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.lang.StringBuilder;

public class TicTacToe {
	
	
	static int rows;
	static String board;
	static ArrayList<node> open;
	static ArrayList<node> closed;
	static TicTacToe t = new TicTacToe();

	static boolean shouldStop = false;
	
	
	static int xos;
	
	public static void main(String[] args) throws IOException{
		open = new ArrayList<node>();
		closed = new ArrayList<node>();
		rows = 0;
		board = "";
		char turn = 'X';
		xos = 0;
		
		BufferedReader br = new BufferedReader(new FileReader(args[0]));
		String dog = "";
		while((dog = br.readLine())!=null){
			if(dog.contains("X") || dog.contains("O") || dog.contains(".")){
			rows++;
			board = board + dog;
			}
		}
		br.close();
		board = boardify(board);
		
		if(xos > 0){
			turn = 'O';
		}
		
		
	        Thread timer = new Thread() {
	            public void run() {
	                try {
	                    sleep(19550);
	                } catch (InterruptedException e) {
	                }
	                shouldStop = true;
	            }
	        };
	        

		
		
		
		
		node root = t.new node(board);
		node head = root;
		
		root.t= turn;
		int done = isSolved(root);
		if(root.solved){
			root.val = done;
		}
		open.add(root);

		if(root.solved){
			open.remove(root);
		}
		node current = root;
		
		
		timer.start();
		while(!open.isEmpty() && !shouldStop){
			//p(current.status + current.val);
	    
			if(current.expanded == false){
				if(!current.solved){
		expand(current);
	    //pat(current.kids);
				}
		current.expanded = true;
				
			}
			
			//p(open.size() + ""); 
		open.remove(current);
		if(!current.solved){
		closed.add(current);
		}
		//p(open);
		if(!open.isEmpty()){
		
			
		current = (TicTacToe.node)open.get(open.size() -1);
		done = isSolved(current);
		if(current.solved){
		current.val = isSolved(current);
		if(current.dad.t == 'X'){
			if(current.val == 1){
				current.dad.val = 1;
				for(int j = 0; j<current.dad.kids.size(); j++){
					 if(open.contains(current.dad.kids.get(j))){
						open.remove(current.dad.kids.get(j));
						
					}
					if(closed.contains(current.dad.kids.get(j))){
						closed.remove(current.dad.kids.get(j));
						
					}
					
				}
				current.dad.kids.clear();
			}
		}
		if(current.dad.t == 'O'){
			if(current.val == -1){
				current.dad.val = -1;
				for(int j = 0; j<current.dad.kids.size(); j++){
					if(open.contains(current.dad.kids.get(j))){
						open.remove(current.dad.kids.get(j));
					}
					if(closed.contains(current.dad.kids.get(j))){
						closed.remove(current.dad.kids.get(j));
					}
					
				}
				current.dad.kids.clear();
			}
		}
		
		}
		//
		
		}
		}
		
		while(!closed.isEmpty() && !shouldStop){
			//System.out.println(closed.size());
			for(int i = 0; i < closed.size(); i++){
				if(shouldStop){
					break;
				}
				node pp = closed.get(i);
				if(hasFullKids(pp)){
					
						for(int j = 0; j<pp.kids.size(); j++){
							if(shouldStop){
								break;
							}
							if(pp.t == 'X'){
							if(pp.val == 7){
								pp.val = pp.kids.get(j).val;
							}
							else {
							if(pp.kids.get(j).val > pp.val){
								
								pp.val = pp.kids.get(j).val;
								
								
							}
							}
						}
							else if(pp.t == 'O'){
								if(pp.val == 7){
									pp.val = pp.kids.get(j).val;
								}
								else {
									if(pp.kids.get(j).val < pp.val){
										pp.val = pp.kids.get(j).val;
										
										
									}
									}
								
							}
					}
						closed.remove(pp);
				}
			}
		}
		
		
		
		if(shouldStop){
			System.out.println("0.0");
		}
		else{
			System.out.println(head.val);
			System.exit(0);
		}
	}
	
	
	public static String boardify(String b){
		for(int i=0; i<b.length(); i++){
			char a = b.charAt(i);
			if(a == 'X'){
				xos++;
			}
			if(a == 'O'){
				xos -- ;
			}
			
			if(a != 'X'){
				
				if(a != 'O'){
					
					if(a != '.'){
						b = b.replace(a, ' '); 
					}
				}
			
			}
		}
		b = b.replaceAll(" ", "");
		return b;
	}
	
	
	public class node{
		
		ArrayList<node> kids;
		String status;
		int blanks;
		int val;

		node dad;
		boolean expanded;
		char t;
		boolean solved;
		
		public node(String b){
			kids = new ArrayList<node>();
			t = 'a';
			val = 7;
			status = b;
			dad = null;
			expanded = false;
			for(int i=0; i<rows*rows; i++){
				String a = "" + status.charAt(i);
				if(a.equals(".")){
					blanks++;
				}
			}
			if(!b.contains(".")){
				this.solved = true;
			}
			else{this.solved = false;}
		}
		
		public boolean hasDad(){
			if(this.dad != null){
				return true;
			}
			return false;
		}
		
	}
	
	public TicTacToe(){
		
	}
	
	public static void expand(node n){
		for(int i = 0; i < n.status.length(); i++){
			if(n.status.charAt(i) == '.'){
				node b = t.new node(move(n,i));
				
				b.dad = n;
				b.t = flip(n.t);
				n.kids.add(b);
				open.add(b);
				
			}
		}
	}
	
	
	
	public static String move(node n, int x){
		StringBuilder m = new StringBuilder(n.status);
		m.setCharAt(x, n.t);
		return m.toString();
	}
	
	public static int isSolved(node n){
		String xcor = "";
		String ycor = "";
		for(int i = 0; i<rows; i++){
			xcor = xcor + "X";
			ycor = ycor + "O";
		}

		
		
		String complete = "";
		for(int i =0; i< n.status.length(); i++){
			if(i%(rows) == 0){
				complete = "";
			}
			complete = complete + n.status.charAt(i);
			
			
			
			if(complete.equals(xcor)){
				
				n.solved = true;
				return 1;
			}
			if(complete.equals(ycor)){
				n.solved = true;
				return -1;
			}
		}
		for(int i =0; i< rows; i++){
			complete = "";
			for(int j =0; j< rows; j++){
				complete = complete + n.status.charAt((j* rows)+ i);
				
			}
			if(complete.equals(xcor)){
				n.solved = true;
				return 1;
			}
			if(complete.equals(ycor)){
				n.solved = true;
				return -1;
			}
		}
		complete = "";
		String comp = "";
		for(int i = 0; i<rows; i++){
			complete = complete + n.status.charAt(i*(rows+1));
			comp = comp + n.status.charAt((rows - 1) + i*(rows -1));

		}
		if(comp.equals(xcor)){
			n.solved = true;
			return 1;
		}
		if(comp.equals(ycor)){
			n.solved = true;
			return -1;
		}
		if(complete.equals(xcor)){
			n.solved = true;
			return 1;
		}

		if(complete.equals(ycor)){
			n.solved = true;
			return -1;
		}
		if(n.blanks == 0){
			n.solved = true;
		}
		return 0;
}
	
	public static char flip(char turn){
		if(turn == 'X'){
			turn = 'O';
			return turn;
		}
			turn = 'X';
		return turn;
	}
	
	public static void pat(String a){
		
		System.out.println(a.toString());
	}
	public static void pat(ArrayList<node> a){
		for(int i=0; i<a.size(); i++){
			String q =a.get(i).status;
			System.out.println(q);
		}
		System.out.println();
	}
	
	public static boolean hasFullKids(node n){
		for(int i=0; i<n.kids.size(); i++){
			if(n.kids.get(i).val == 7){
				return false;
			}
			
		}
		return true;
	}
}

















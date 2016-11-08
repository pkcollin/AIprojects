
//Patrick Collins

import java.io.*;
import java.util.*;

public class Solver {
	static int[][] matrix;
	ArrayList<node> open;
	ArrayList<node> closed;
	static int rows;
	static int cols;
	static Validator v;
	
	public static void main(String[] args)throws IOException{
		
		
		Solver s = new Solver();
		v = new Validator();
		
		
		BufferedReader br = new BufferedReader(new FileReader(args[0]));
		BufferedReader ka = new BufferedReader(new FileReader(args[0]));
		rows = 1;
		cols =0;
		String dog[] = null;
		try{
		dog = ka.readLine().trim().split(" ");
		}catch(IOException e){};
	
		cols = dog.length;
		
		//get rows
		String cat = "";
		while(((cat = ka.readLine()) != null) && !(cat.startsWith("u") || cat.startsWith("d") || cat.startsWith("l") || cat.startsWith("r"))){
			rows++;
		}
			
		//make matrix
			matrix = new int[rows][cols];
		int row = 0;
		String line = "";
		String[] liner = null;
		
		while(((line = br.readLine()) != null) && !(line.startsWith("u") || line.startsWith("d") || line.startsWith("l") || line.startsWith("r"))){
		liner = line.trim().split(" ");
		for(int i=0; i<liner.length; i++){
			matrix[row][i] = Integer.parseInt(liner[i]);
		}
		row++;
		}
		//print(matrix);
		if(line != null){
			//System.out.println(line);
			liner = line.split(" ");
			s.move(matrix, liner[0], Integer.parseInt(liner[1]));
			//print(matrix);
		}
	
	while((line = ka.readLine()) != null){
		//System.out.println(line);
		liner = line.split(" ");
		s.move(matrix, liner[0], Integer.parseInt(liner[1]));
		//print(matrix);
	}
	
	s.solve();

	br.close();
	ka.close();

}
	
	
	
	
	
	public Solver(){
		
		open = new ArrayList<node>();
		closed = new ArrayList<node>();
	}
	//////////
	
	
	
	
	
	
	
	public int solve(){
		print(matrix);
		int bigH=0;
		node start = new node(matrix);
		bigH = start.calcH();
		if(bigH == (rows*cols)){
			
			return 1;
		}
		else{
			
			boolean notSolved=true;
			
			while(notSolved){
				bigH=0;
				
				closed.add(start);
			
				open.remove(start);
				
				start.expand();
				for(int i=0; i<open.size(); i++){
					if(open.get(i).heur > bigH){
						//System.out.println(open.get(i).heur);
						bigH = open.get(i).heur;
						start = open.get(i);
					}
				}
				if(bigH == (rows*cols)){
					//print(start.board);
					for(int i=0; i<start.moves.size(); i++){
					System.out.println(start.moves.get(i));
					}
					notSolved = false;
					return 1;
				}
			}
			
			
			
			
			return 0;
			
		}
			
		}
	
	
	
	
	
	
	
	//////////
	
	public int[][] move(int[][] k, String mo, int pos){
		int temp = 0;
		if(mo.toLowerCase().equals("u")){
			for(int i=0; i<rows -1; i++){
				if(i-1 < 0){
					temp = k[0][pos];
					k[i][pos] = k[1][pos];
				} else{
				k[i][pos] = k[(i+1)%rows][pos];
				}
			}
			k[rows - 1][pos] = temp;
		}
		else if(mo.toLowerCase().equals("d")){
			temp = k[rows - 1][pos];
			for(int i=rows -1; i>=0; i--){
				
				if(i == 0){
					k[i][pos] = temp;
				} else{
				k[i][pos] = k[(i-1)%rows][pos];
				}
			}
			k[0][pos] = temp;
		}
		else if(mo.toLowerCase().equals("l")){
			for(int i=0; i<cols -1; i++){
				if(i-1 < 0){
					temp = k[pos][0];
					k[pos][i] = k[pos][1];
				} else{
				k[pos][i] = k[pos][(i+1)%cols];
				}
			}
			k[pos][cols - 1] = temp;
		}
		else if(mo.toLowerCase().equals("r")){
			temp = k[pos][cols - 1];
			for(int i=cols -1; i>=0; i--){
				
				if(i == 0){
					k[pos][i] = temp;
				} else{
				k[pos][i] = k[pos][(i-1)%cols];
				}
			}
			k[pos][0] = temp;
		}
		return k;
	}
	
	
	
	
	
	
	public static void print(int[][] board){
		for(int i=0; i<rows; i++){
			for(int j=0; j<cols; j++){
				System.out.print(board[i][j] + " ");
			}
			System.out.println();
		}
	
}
	
	
	
	
	
	
	
	public class node{
		int[][] board;
		
		int heur;// how many are in the right spot
		
		ArrayList<String> moves;
		public node(int[][] b, ArrayList<String> pp){
			moves = new ArrayList<String>();
			board = new int[rows][cols];
			heur = 0;
			for(int i=0; i<rows; i++){
				for(int j=0; j<cols; j++){
					board[i][j] = b[i][j];
				}
			}
			
			for(int i=0; i<pp.size(); i++){
				moves.add(pp.get(i));
			}
			
		}
		public node(int[][] b){
			board = b;
			heur = 0;
			moves = new ArrayList<String>();
		}
		
		
		
		
		public void addMove(String mov){
			this.moves.add(mov);
		}
		
		
		
		
		
		
		public void expand(){
			for(int i=0; i< cols; i++){
				boolean in = true;
				node up = new node(move(this.board, "u", i), this.moves);
				up.calcH();
				move(this.board, "d", i);
				
				for(int l=0; l<closed.size(); l++){
					if(closed.get(l).eqs(up.board)){
						in=false;
					}
				}
				for(int l=0; l<open.size(); l++){
					if(open.get(l).eqs(up.board)){
						in=false;
					}
				}
				if(in){
					up.addMove("u " + i);
					open.add(up);
				}
				

				//System.out.println(in);
				
			boolean ik = true;
			node down = new node(move(this.board, "d", i), this.moves);
			down.calcH();
			move(this.board, "u", i);
			//print(down.board);
			
			for(int l=0; l<closed.size(); l++){
				if(closed.get(l).eqs(down.board)){
					//System.out.println(closed.get(0).board[0][0]);
					ik=false;
				}
			}
			for(int l=0; l<open.size(); l++){
				if(open.get(l).eqs(down.board)){
					ik=false;
				}
			}
			if(ik){
				down.addMove("d " + i);
				open.add(down);
			}
			

			//System.out.println(ik);
			}
			
			
			for(int i=0; i< rows; i++){
				
				boolean ik = true;
				node left = new node(move(board, "l", i), this.moves);
				left.calcH();
				move(this.board, "r", i);
				for(int l=0; l<closed.size(); l++){
					if(closed.get(l).eqs(left.board)){
						ik=false;
						
					}
				}
				for(int l=0; l<open.size(); l++){
					if(open.get(l).eqs(left.board)){
						ik=false;
					}
				}
				if(ik){
					left.addMove("l " + i);
					open.add(left);
				}

				
				
				boolean ir = true;
				node right = new node(move(board, "r", i), this.moves);
				right.calcH();
				move(this.board, "l", i);
				for(int l=0; l<closed.size(); l++){
					if(closed.get(l).eqs(right.board)){
						ir=false;
					}
				}
				for(int l=0; l<open.size(); l++){
					if(open.get(l).eqs(right.board)){
						ir=false;
					}
				}
				if(ir){
					right.addMove("r " + i);
					open.add(right);
				}
				

			}
		}
		
		
		
		
		
		
		
		
		public int calcH(){
			this.heur = 0;
			ArrayList<Integer> colors = new ArrayList<Integer>();
			for(int i=0; i<rows; i++){
				for(int j=0; j<cols; j++){
					if(!colors.contains(this.board[i][j])){
						colors.add(this.board[i][j]);
					}
				}
			}   
			//System.out.println(colors.size());
			int[] wtp = new int[rows];
			
			while(!colors.isEmpty()){
				for(int i =0; i<rows; i++){
				int smallest = colors.get(0);
			for(int j = 0; j< colors.size(); j++){
				if(colors.get(j) < smallest){
					smallest = colors.get(j);
					
				}
			}
			wtp[i] = smallest;
			colors.remove((Integer)smallest);
				}
			}
			for(int i=0; i<wtp.length; i++){
				colors.add(wtp[i]);
			}
			//for(int i =0; i<colors.size(); i++){
				//System.out.println(colors.get(i));
			//}
			for(int i=0; i<rows; i++){
				for(int j=0; j<cols; j++){
					int current = this.board[i][j];
					if(current == colors.get(i)){
						this.heur = this.heur +1;
					}
				}
				
			}
			//System.out.println(this.heur);
			return this.heur;
			
		}
		
		public boolean eqs(int[][] a){
			for(int i=0; i<rows; i++){
				for(int j=0; j<cols; j++){
					if(this.board[i][j] != a[i][j]){
						return false;
					}
				}
			}
			return true;
		}
		
	}
}

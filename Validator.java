//Patrick Collins
//26794792

import java.io.*;

public class Validator {
	
	static int[][] matrix;
	
	public static void main(String[] args)throws IOException{
		Validator v = new Validator();
		BufferedReader br = new BufferedReader(new FileReader(args[0]));
		BufferedReader ka = new BufferedReader(new FileReader(args[0]));
		int rows = 1;
		int cols =0;
		String dog[] = null;
		try{
		dog = ka.readLine().trim().split(" ");
		}catch(IOException e){};
	
		cols = dog.length;
		
		//get rows
		String cat = "";
		while(((cat = ka.readLine()) != null) && !(cat.startsWith("u") || cat.startsWith("d") || cat.startsWith("l") ||
cat.startsWith("r"))){
			rows++;
		}
			
		//make matrix
			matrix = new int[rows][cols];
		int row = 0;
		String line = "";
		String[] liner = null;
		
		while(((line = br.readLine()) != null) && !(line.startsWith("u") || line.startsWith("d") || line.startsWith("l")
|| line.startsWith("r"))){
		liner = line.trim().split(" ");
		for(int i=0; i<liner.length; i++){
			matrix[row][i] = Integer.parseInt(liner[i]);
		}
		row++;
		}
		//print(matrix, rows, cols);
		if(line != null){
			//System.out.println(line);
			liner = line.split(" ");
			v.move(rows, cols, liner[0], Integer.parseInt(liner[1]));
			//print(matrix, rows, cols);
		}
	
	while((line = ka.readLine()) != null){
		//System.out.println(line);
		liner = line.split(" ");
		v.move(rows, cols, liner[0], Integer.parseInt(liner[1]));
		//print(matrix, rows, cols);
	}
	
	System.out.println(v.valid(matrix, rows, cols));
	br.close();
	ka.close();

}
	
	public Validator(){
		
	}
	
	public String valid(int[][] b, int ro, int co){
		int lowest = b[0][0];
		int current = b[0][0];
		for(int i=0; i<ro; i++){
			current = b[i][0];
			for(int j=0; j<co; j++){
				if(b[i][j] != current || b[i][j] < lowest){
					return "invalid";
				}
				
			}
			
		}
		return "valid";
	}
	
	public void move(int raw, int caw, String mo, int pos){
		int temp = 0;
		if(mo.toLowerCase().equals("u")){
			for(int i=0; i<raw -1; i++){
				if(i-1 < 0){
					temp = matrix[0][pos];
					matrix[i][pos] = matrix[1][pos];
				} else{
				matrix[i][pos] = matrix[(i+1)%raw][pos];
				}
			}
			matrix[raw - 1][pos] = temp;
		}
		else if(mo.toLowerCase().equals("d")){
			temp = matrix[raw - 1][pos];
			for(int i=raw -1; i>=0; i--){
				
				if(i == 0){
					matrix[i][pos] = temp;
				} else{
				matrix[i][pos] = matrix[(i-1)%raw][pos];
				}
			}
			matrix[0][pos] = temp;
		}
		else if(mo.toLowerCase().equals("l")){
			for(int i=0; i<caw -1; i++){
				if(i-1 < 0){
					temp = matrix[pos][0];
					matrix[pos][i] = matrix[pos][1];
				} else{
				matrix[pos][i] = matrix[pos][(i+1)%caw];
				}
			}
			matrix[pos][caw - 1] = temp;
		}
		else if(mo.toLowerCase().equals("r")){
			temp = matrix[pos][caw - 1];
			for(int i=caw -1; i>=0; i--){
				
				if(i == 0){
					matrix[pos][i] = temp;
				} else{
				matrix[pos][i] = matrix[pos][(i-1)%caw];
				}
			}
			matrix[pos][0] = temp;
		}
	}
	
	
	public static void print(int[][] board, int r, int c){
		for(int i=0; i<r; i++){
			for(int j=0; j<c; j++){
				System.out.print(board[i][j] + " ");
			}
			System.out.println();
		}
	}
}


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DecisionTree {

	static ArrayList<String[]> trainD;
	
	public static void main(String[] args) throws IOException{
		DecisionTree root = new DecisionTree();
		trainD = new ArrayList<String[]>();
		int reps = 0;
		int dems = 0;
		
		BufferedReader br = new BufferedReader(new FileReader(args[0]));
		BufferedReader b = new BufferedReader(new FileReader(args[1]));
		String dog = "";
		
		//Get input
		while((dog = br.readLine())!=null){
			String[] trainer = dog.split(",");
			if(trainer[0].equals("republican")){
				trainer[0] = "r";
				reps++;
			}else{
				trainer[0] = "d";
				dems++;
			}
			trainD.add(trainer);
			
		}
		br.close();
		
		while((dog = b.readLine()) != null){
			String[] testData = dog.split(",");
			int[] qList = new int[testData.length];
			Tree boot = root.new Tree(dems, reps, qList, trainD);
			Tree fin = boot.traverse(testData);
			double bob = (fin.r)/(fin.r + fin.d);
			//System.out.println("fin.r: " + fin.r);
			//System.out.println("fin.d " + fin.d);
			if(bob < 0.5){
				System.out.println("democrat," + (1.0 - bob));
			}else{
				System.out.println("republican," + bob);
			}
		}
		b.close();
	}
	
	public DecisionTree(){
		
	}
	
	
	public class Tree {

		Tree par;
		double d;
		double r;
		int[] qList;
		double yesd;
		double yesr;
		double nod;
		double nor;
		ArrayList<String[]> examples;
		
		
		public Tree(double d, double r, int[] b, ArrayList<String[]> x){
			this.d = d;
			this.r = r;
			qList = b;
			par = null;
			yesd = 0;
			nod = 0;
			yesr =0;
			nor = 0;
			examples = x;
		}
		//ArrayList<String[]> x
		public Tree(int[] b, Tree p, double d, double r, ArrayList<String[]> x){
			this.d = d;
			this.r = r;
			qList = b;
			par = p;
			yesd = 0;
			nod = 0;
			yesr =0;
			nor = 0;
			examples = x;
		}
		
		public Tree traverse(String[] data){
			if(d == 0 && r == 0){
				//System.out.println("par");
				return this.par;
			}
			if(d == 0){
				//System.out.println("d");
				return this;
			}else if(r == 0){
				//System.out.println("r");
				return this;
			}
			
			int a = importance(qList);
			
			if(a == -1){
				//System.out.println("-1");
				return this;
			}
			
			int[] q2 = new int[data.length];
			for(int i =0; i<q2.length; i++){
				q2[i] = qList[i];
			}
			q2[a] = 1;
			ArrayList<String[]> t2 = new ArrayList<String[]>();
			//System.out.println("question: " + a);
			if(data[a].equals("y")){
			for(int i =0; i<examples.size(); i++){
				if(examples.get(i)[a+1].equals("y")){
					t2.add(examples.get(i));
				if(examples.get(i)[0].equals("r")){
					yesr++;
				}else{
					yesd++;
				}
			}
			}
			}else{
				for(int i =0; i<examples.size(); i++){
					if(examples.get(i)[a+1].equals("n")){
						t2.add(examples.get(i));
					if(examples.get(i)[0].equals("r")){
						nor++;
					}else{
						nod++;
					}
				}
				}
			}
			/*
			boolean empty = true;
			for(int i=0; i<q2.length; i++){
				if(q2[i] == 0){
					empty = false;
				}
				//return this;
			}
			if(empty){
				return this;
			}*/
			Tree next = null;
			if(data[a].equals("y")){
				next = new Tree(q2, this, yesd, yesr, t2);
			}else{
				next  = new Tree(q2, this, nod, nor, t2);
			}
			return next.traverse(data);
		}
		
		
		
		
		public int importance(int[] ql){
			double[] prio = new double[ql.length];
			for(int j=0; j<ql.length; j++){
				if(ql[j] == 0){
					
					double remain = 0;
					String f = "";
					for(int i=0; i<2; i++){ //in order yes, no
						if(i == 0){
							f = "y";
						}else{
							f = "n";
						}
						double pk =0;
						double nk = 0;
						
						for(int k=0; k<examples.size(); k++){
							if(examples.get(k)[j+1].equals(f)){
							if(examples.get(k)[0].equals("r")){
								
									pk++;
								
								}else{
									
									nk++;
									
								}
							}
						}
						
						double q = (pk)/(pk + nk);
						
						double y = 0;
						if(q != 0){
						y = q*(Math.log(q)/Math.log(2));
						}
						double p2 = 0;
						if(q != 1){
						p2 = ((1-q)*(Math.log(1-q)/Math.log(2)));
						}
						double x = -(y+p2);
						
						double m = ((pk + nk)/(this.r+this.d))*x;
						
						remain = remain + m;
					}
					//System.out.println(remain + " " + j);
					double a = (this.r)/(this.r +this.d);
					double z = -(a*(Math.log(a)/Math.log(2))+((1-a)*(Math.log(1-a)/Math.log(2))));
					prio[j] = z-remain;
				}
			}//End Main loop
			double biggest = prio[0];
			int spot = 0;
			for(int i=0; i<prio.length; i++){
				if(prio[i] > biggest){
					spot = i;
					biggest = prio[i];
				}
			}
			if(biggest == 0){
				return -1;//no biggest
			}else{
				
				return spot;
			}
		}//End Method
	}
	
	
	
	
	
	
}


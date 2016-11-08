
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Viterbi {
	
	//Half this stuff isn't even used lol
  
 //THIS IS NOT THE VITERBI, IT IS A CHEATY VERSION OF IT IT DOESN'T ACTUALLY ALWAYS WORK
	
	static float[] unfinalw;
	static float[] finale;
	static float[] step1o;
	static float[] unstep1o;
	
	public boolean Fair = true;
	public boolean unFair = false;
	
	static float unFairCoinH;
	
	static float fairToUn;
	static float fairToFair;
	static float unToFair;
	static float unToUn;
	static String[] sequence;
	
	static float[][] transition;
	static float[][] emission;
	
	static float[][] ati;
	static float[] initial;
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(args[0]));
		String dog = "";
		dog = br.readLine();//PROB THAT UNFAIR IS HEADS
		unFairCoinH = Float.parseFloat(dog);
		dog = br.readLine(); // PROB THAT THE FAIR CHANGES TO UNFAIR
		fairToUn = Float.parseFloat(dog);
		fairToFair = 1 - fairToUn;
		dog = br.readLine(); //PROB THAT THE UNFAIR GOES TO FAIR
		unToFair = Float.parseFloat(dog);
		unToUn = 1 - unToFair;
		dog = br.readLine();
		sequence = dog.split(" ");
		br.close();
		
		transition = new float[2][2];
		emission = new float[2][2];
		ati = new float[sequence.length][2];
		initial = new float[2];
		
		initial[0] = 1;
		initial[1] = 0;
		
		transition[0][0] = fairToFair;
		transition[0][1] = fairToUn;
		transition[1][0] = unToFair;
		transition[1][1] = unToUn;
		
		emission[0][0] = (float) 0.5;//in state fair and returns heads
		emission[0][1] = (float) 0.5;
		emission[1][0] = (float) unFairCoinH;//in state unfair and returns heads
		emission[1][1] = (float)(1 - unFairCoinH);
		
		
		
		unfinalw = new float[sequence.length + 1];
		finale = new float[sequence.length + 1];
		step1o = new float[sequence.length + 1];
		unstep1o = new float[sequence.length + 1];
		for(int i=1; i<=sequence.length; i++){
			unfinalw[i] = -1;
			finale[i] = -1;
			step1o[i] = -1;
			unstep1o[i] = -1;
		}
		
//		float probXisFairGivenLast = (float)0.5;
//		float fairAtT = step1(sequence.length);
//		float uFairAtT = unStep1(sequence.length);
		
		//float alpha = getAlpha(sequence.length);
		
		for(int i=0; i<sequence.length; i++){
			float pFairAtTGObservationAtT = finalEq(i+1);
			if(pFairAtTGObservationAtT >= 0.5){
				System.out.print("f ");
			}else{
				System.out.print("u ");
			}
		}
		System.out.println();
	}
	
	public static float step1(int t){
		if(t == 1){
			return 1;
		}if(step1o[t] != -1){
			return step1o[t];
		}
		step1o[t] = fairToFair*finalEq(t-1) + unToFair*unfinalEq(t-1);
		return step1o[t];
	}
	public static float unStep1(int t){
		if(t == 1){
			return 0;
		}
		if(unstep1o[t] != -1){
			return unstep1o[t];
		}
		unstep1o[t] = fairToUn*finalEq(t-1) + unToUn*unfinalEq(t-1);
		
		return unstep1o[t];
	}
	
	public static float finalEq(int t){
		float f = (float)0.5;
		float u = 0;
		if(sequence[t-1].equals("h")){
			u=unFairCoinH;
		}else{
			u=1-unFairCoinH;
		}
		if(finale[t] != -1){
			return finale[t];
		}
		finale[t] = (f*step1(t))/((f*step1(t))+(u*unStep1(t)));
		return finale[t];
	}
	public static float unfinalEq(int t){
		float f = (float)0.5;
		float u = 0;
		if(sequence[t-1].equals("h")){
			u=unFairCoinH;
		}else{
			u=1-unFairCoinH;
		}
		if(unfinalw[t] != -1){
			return unfinalw[t];
		}
		unfinalw[t] = (u*unStep1(t))/((f*step1(t))+(u*unStep1(t)));
		return unfinalw[t];
	}
//	
//	public static float pObFair(int t){
//		if(sequence[t-1].equals("h")){
//			
//		}
//	}
}








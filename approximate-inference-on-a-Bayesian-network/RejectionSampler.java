
import java.awt.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;


/**
 * Created by liberato on 10/25/14.
 */
public class RejectionSampler {
	
	//static boolean count = false;

    public static String readEntireFile(File f) {
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        

        // tomfoolery relying on '\A' meaning "separate tokens using only the
        // beginning of the input as a boundary"
        java.util.Scanner scanner = new java.util.Scanner(fin,"UTF-8").useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }

    public static void main(String[] args) {

        String fileContents = readEntireFile(new File(args[0]));
        String query = readEntireFile(new File(args[1]));
        Double samples = Double.parseDouble(args[2]);
        Map<String, Node> nodeMap = Node.nodesFromString(fileContents);
        
        ArrayList<String> order = new ArrayList<String>();
        fileContents = fileContents.trim();
        int quotes = 0;
        String word = "";
        int brackets = 0;
        for(int i = 1; i<fileContents.length() - 1; i++){
        	char letter = fileContents.charAt(i);
        	
        	if(letter == '['){
        		brackets++;
        		
        	}
        	if(letter == ']'){
        		brackets--;
        		if(brackets == 0){
            		order.add(word);
            		quotes = 0;
            		word = "";
            		
            	}
        	}
        	if(brackets <= 1){
        		if(letter == '"'){
        			quotes++;
        		}else{
        		if(quotes <= 1 && letter != ']' && letter != '[' && letter != ' ' && letter != ',' && letter != '\n'){
        			word = word + letter;
        		}
        		}
        	}
        }
        
        
        
        ArrayList<Node> list = new ArrayList<Node>(nodeMap.values());
        //because for some reason the last node gets put first
        
        for(int i =0; i<order.size(); i++){
        	for(int j =0; j<list.size(); j++){
        		if(list.get(j).name.equals(order.get(i))){
        			Node temp = list.get(i);
        			list.set(i, list.get(j));
        			list.set(j, temp);
        		}
        	}
        }
        for(int i = 0; i<list.size(); i++){
        	String na = list.get(i).name.replace(" ", "");
        	na = na.replace("[", "");
        	na = na.replace("]", "");
        	na = na.replace(",", "");
        	na = na.replace("\n", "");
        	list.get(i).name = na;
        	list.set(i, list.get(i));
        }
        
        Node q = Node.getQNode(query);
        
        
        int a = (int)Math.pow(2, q.n.length);
        double[] probz = new double[a];
        
        System.gc();
        
        Random r = new Random();
        int rejectedSamples = 0;
        
        
        long time = System.nanoTime();
        
        for(int i =0; i< samples; i++){
        	ArrayList<Pair> net = new ArrayList<Pair>();
        	//int rand = r.nextInt(list.size());
        	boolean getOut = false;
        	for(int j =0; j<list.size(); j++){
        		Node k = list.get(j);
        		Pair p = new Pair(k.name, "");
        		
        		if(k.parents.length == 0){
        			double aw = k.probs[0];
        			double rand = r.nextDouble();
        			
        			if(rand < aw){
        			  p.val="true";
        			}else{
        				p.val="false";
        			}
        			for(int m = 0; m<q.g.length; m++){
        				if(q.g[m].equals(p.s)){
        					if(!q.cond[m].equals(p.val)){
        						rejectedSamples++; 
        						getOut = true;
        						break;
        					}
        				}
        			}if(getOut){break;}
        		}else{
        			
        			String[] checkem = new String[k.parents.length];
        			for(int l =0; l< k.parents.length; l++){ 
        				String t = k.parents[l].name;
        				for(int woot = 0; woot< net.size(); woot++){
        					//System.out.println(net.size());
        					// The quotation marks??
        					if(net.get(woot).s.equals(t)){
        						//System.out.println(net.get(woot).s);
        						//System.out.println(net.get(woot).val);
        						if(net.get(woot).val.equals("true")){
        							checkem[l]="true";
        							
        						}else{
        							checkem[l]="false";
        						}
        					}
        				}
        			}
        			double proz = generateRan(checkem, list.get(j));
        			double rand = r.nextDouble();
        			//System.out.println(rand + " " + proz + " " + p.s);
        			
        			if(rand < proz){
        			  p.val="true";
        			}else{
        				p.val="false";
        			}
        			//check if its a given
        			for(int x = 0; x < q.g.length;x++){
        				//System.out.println(q.g[x] + " lloo");
        				//System.out.println(q.cond[x]);
        			
        			
        			}
        			for(int m = 0; m<q.g.length; m++){
        				//System.out.println(m);
        				if(q.g[m].equals(p.s)){
        					if(!q.cond[m].equals(p.val)){
        						rejectedSamples++;
        						getOut=true;
        						break;
        					}
        				}
        			}
        			if(getOut){break;}
        		}
        		net.add(p);
        	}//End Sample Net
        	if(!getOut){
        	int index = probz.length;
        	int adder = probz.length/2;
        	for(int n = 0; n< q.n.length; n++){
        		String name = q.n[n];
        		for(int d = 0; d<net.size(); d++){
        			if(net.get(d).s.equals(name)){
        				if(net.get(d).val.equals("true")){   					
        						index = index - adder;
        						adder = adder/2;
        				}else{
        					adder = adder/2;
    						
        				}
        			}
        		}
        	}
        	//System.out.println(index);
        	index--;
        	//System.out.println(probz.length);
        	probz[index]++;
        	}//getOut
        	}//End All Samples
        long time2 = System.nanoTime();
        Double totalTime = (time2 - time)/1000000000.0;
        
        //System.out.println(totalTime);
        
        int total = (int) (samples - rejectedSamples);
        DecimalFormat df = new DecimalFormat("#.####");
        if(rejectedSamples == samples){
        	System.out.println("[]");
        }
        else{
        	System.out.print("[");
        	for(int i =0; i<probz.length; i++){
        		//System.out.print(probz[i] + "<-count");
        		if(i!=probz.length-1){
        		System.out.print(df.format(probz[i]/total) + ", ");
        		}else{
        			System.out.print(df.format(probz[i]/total));
        		}
        	}
        	System.out.println("]");
        }
        
        
        df.format(samples/totalTime);
        System.err.println(df.format(samples/totalTime));
        
        
        
        
        
        
        
        
        
    }
    
    
    public static double generateRan(String[] parVals, Node b){
    	//String[] miniAr = new String[a.length];
    	ArrayList<Double> miniAr = new ArrayList<Double>();
    	for(int i=0; i<b.probs.length; i++){
    		miniAr.add( b.probs[i]);
    	}
    	
    	for(int i =0; i<parVals.length; i++){
    		if(parVals[i].equals("true")){
    			//remove 2nd half
    			//miniAr = removeSecond(miniAr);
    			int h = miniAr.size()/2;
    			for(int j = miniAr.size() - 1; j >= h; j--){
    				miniAr.remove(j);
    			}
    			
    		}else{
    			//remove 1st half
    			//miniAr = removeFirst(miniArr);
    			for(int j = 0; j < miniAr.size()/2; j++){
    				miniAr.remove(j);
    			}
    			
    		}
    	}
    	//System.out.println(miniAr.size());
    	return miniAr.get(0);
    }
    
    
   
    	
    	

}





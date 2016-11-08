import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liberato on 10/25/14.
 */
public class Node {
    public final String name;
    public final Node[] parents;
    public final double[] probs;
    
    public final String[] n;
    public final String[] cond;
    public final String[] g;

    public Node(String name, Node[] parents, double[] probs) {
        this.name = name;
        this.parents = parents;
        this.probs = probs;
        cond = null;
        n = null;
        g = null;
    }
    
    public Node(String[] names, String[] given, String[] tf){
    	this.n = names;
        this.parents = null;
        probs = null;
        name = null;
        this.g = given;
        this.cond = tf;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        //sb.append(name + "=");
        sb.append("{parents=[");
        for (Node parent : parents) {
            sb.append(parent.name);
            sb.append(',');
        }
        sb.append("], probs="+ Arrays.toString(probs) + '}');
        return sb.toString();
    }

    /**
     *
     * @param s a JSON encoded Bayes Net
     * @return a map, from node names to nodes
     */
    public static Map<String, Node> nodesFromString(String s) {
        Map<String, Node> nodeMap = new HashMap<String, Node>();

        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonArray jsonNodes = parser.parse(s).getAsJsonArray();

        for (JsonElement element : jsonNodes) {
            JsonArray jsonNode = element.getAsJsonArray();
            String name = jsonNode.get(0).getAsString();
            String[] parentNames = gson.fromJson(jsonNode.get(1), String[].class);
            Node[] parents = new Node[parentNames.length];
            for (int i=0; i < parentNames.length; i++) {
                parents[i] = nodeMap.get(parentNames[i]);
            }
            double[] probs = gson.fromJson(jsonNode.get(2), double[].class);

            Node node = new Node(name, parents, probs);
            nodeMap.put(name, node);
        }
        return nodeMap;
    }
    
    public static Node getQNode(String s) {
    	String[] Ps = null;
    	String[] Gs = null;
    	String[] Bs = null;
    	Node a;
    	int start = 0;
    	int end = 0;
    	int many = 0;
    		for(int i = 1; i<s.length() - 1; i++){
    			if(s.charAt(i) == '['){
    				start = i;
    			}
    			if(s.charAt(i) == ']'){
    				end = i;
    				//System.out.println(end);
    				if(many == 0){
    					Ps = getPs(s, start, end);
    					many++;
    					
    				}else{
    				if(many == 1){
    					Gs = getPs(s, start, end);
    					many++;
    					
    				}else{
    				if(many == 2){
    					Bs = getPs(s, start, end);
    					many++;
    					
    				
    				}}}
    			}
    		}
    		
    	a = new Node(Ps,Gs,Bs);
    	//System.out.println(a.cond[0]);
        
        return a;
    }
    
    public static String[] getPs(String s, int a, int b){
    	s = s.substring(a,b);
    	
    	s = s.replace("\"", "");
    	s = s.replace(" ", "");
    	s = s.replace("[", "");
    	s = s.replace("]", "");
    	
    	
    	String[] dog = s.split(",");
    	for(int i =0; i < dog.length; i++){
    		dog[i] = dog[i].trim();
    	}
    	return dog;
    }
   
    
    
   
}



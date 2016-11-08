
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class KMeans {

	static int k;
	static ArrayList<double[]> centers;
	static ArrayList<Point> alls;
	static ArrayList<ArrayList<Point>> clusters;
	
	static ArrayList<double[]> oldCenters;
	
	public static void main(String[] args) throws IOException{
	BufferedReader br = new BufferedReader(new FileReader(args[0]));
	String dog = "";
	centers = new ArrayList<double[]>();
	oldCenters = new ArrayList<double[]>();
	clusters = new ArrayList<ArrayList<Point>>();
	alls = new ArrayList<Point>();
	k = Integer.parseInt(args[1]);
	int count = 0;
	while((dog = br.readLine())!=null){
		
		Point line = new Point(dog, count);
		if(count != k){
			double[] cent = new double[line.coordinatz.length];
			double[] ocent = new double[line.coordinatz.length];
			for(int i=0; i <cent.length; i++){
				cent[i] = line.coordinatz[i];
				ocent[i] = line.coordinatz[i];
			}
			centers.add(cent);
			oldCenters.add(ocent);
			count++;
			ArrayList<Point> arbitrary = new ArrayList<Point>();
			clusters.add(arbitrary);
		}
		alls.add(line);
	
	}//End data input while
	br.close();
	boolean notConverged = true;
	
	
	//Now Try to Converge the Stuff
	while(notConverged){
		for(int i=0; i<clusters.size(); i++){
			clusters.get(i).clear();
		}
		//Assign each thing a cluster
		for(int i=0; i<alls.size(); i++){
			Point current = alls.get(i);
			int clust = 0;
			double closest = current.dist(centers.get(0));
			
			for(int j = 1; j<centers.size(); j++){
				double distance = current.dist(centers.get(j));
				if(distance < closest){
					closest = distance;
					clust = j;
				}
			}
			current.cluster = clust;
			clusters.get(clust).add(current);
		}
		//Now go through the clusters and get the new centroids
		for(int i=0; i<clusters.size(); i++){
			ArrayList<Point> currentClust = clusters.get(i);
			double[] mean = centers.get(i);
			//initialize mean to everything being 0 
			//so that we can count up and then divide at the end
			for(int l=0; l<mean.length; l++){
				mean[l] = 0;
			}
			//System.out.println(currentClust.size());
			for(int j =0; j<currentClust.size(); j++){
				Point currentPoint = currentClust.get(j);
				for(int l =0; l<currentPoint.coordinatz.length; l++){
					mean[l] = mean[l] + currentPoint.coordinatz[l];
				}
			}
			for(int l=0; l<mean.length; l++){
				//System.out.println(mean[l]);
				mean[l] = mean[l]/currentClust.size();
			}
		}
		//check to see if the centroids changed or not
		boolean forReal = false;
		for(int i=0; i<centers.size(); i++){
			if(!forReal){
			notConverged = false;
			}
			for(int j=0; j<centers.get(i).length; j++){
				//System.out.println(oldCenters.get(i)[j] + " " + centers.get(i)[j]);
				if(centers.get(i)[j] != oldCenters.get(i)[j]){
					notConverged = true;
					forReal = true;
				}
				oldCenters.get(i)[j] = centers.get(i)[j];
				
			}
		}
		
	}//End Convergence
	
	//print
	for(int i=0; i<alls.size(); i++){
		System.out.println(alls.get(i).cluster);
	}
	
	
	
	}//End Main
	
}//End Class










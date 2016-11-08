
public class Point {

	double[] coordinatz;
	String label;
	int cluster;
	
	public Point(String s, int c){
		cluster = c;
		String[] data = s.split(" ");
		coordinatz = new double[data.length - 1];
		for(int i=1; i< data.length; i++){
		coordinatz[i-1] = Double.parseDouble(data[i]);
		}
		label = data[0];
	}
	
	public double dist(double[] ds){
		double dist =0;
		for(int i=0; i<coordinatz.length; i++){
			dist = dist + Math.pow((this.coordinatz[i] - ds[i]), 2);
		}
		dist = Math.sqrt(dist);
		return dist;
	}
	
}


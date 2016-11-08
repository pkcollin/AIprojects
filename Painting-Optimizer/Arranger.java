
import java.io.*;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import static java.util.Collections.sort;

public class Arranger{
	
	static String cat = "";
	static boolean stop = false;
	
	public static int getCov(List<Placement> a){
		int sum =0;
		for(Placement q : a){
			sum = sum + (q.width* q.height);
		}
		
		return sum;
	}
	
	public static double getbb(List<Placement> a){
		
		double sum =0;
		for(Placement q : a){
			
			if(q.width < q.height){
				sum = sum + q.width*q.width;
			}
			else if(q.width > q.height){
				sum = sum + q.height*q.height;
			}
			else{
				sum = sum +q.height*q.height;
				
			}
		}
		
		
		return sum;
	}
	


    @SuppressWarnings("unchecked")
	public static void main(String[] args) {
    	
    	stop = false;
    	Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(17500);
                } catch (InterruptedException e) {
                }
                stop = true;
            }
        };
        stop = false;
        
        String func = args[0];
        if(func.equals("coverage")){
        	cat = "coverage";
        }
        if(func.equals("bigger")){
        	cat = "bigger";
        }

    
        File f = new File(args[1]);
        int wallWidth = 0;
        int wallHeight = 0;
        List<Picture> pictures = new Vector<Picture>();
        List<SubWall> subWalls = new Vector<SubWall>();
        List<Placement> placements = new Vector<Placement>();
        
        List<Picture> pics = new Vector<Picture>();
        List<SubWall> subWas = new Vector<SubWall>();
        List<Placement> places = new Vector<Placement>();
        
        List<Picture> startPics = new Vector<Picture>();
        List<SubWall> startSubs = new Vector<SubWall>();
        List<Placement> startPlace = new Vector<Placement>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line = br.readLine();
            String[] splitLine = line.split(" ");
            wallWidth = Integer.parseInt(splitLine[0]);
            wallHeight = Integer.parseInt(splitLine[1]);
            

            while ((line = br.readLine()) != null) {
                splitLine = line.split(" ");
                Picture p = new Picture(splitLine[0], Integer.parseInt(splitLine[1]), Integer.parseInt(splitLine[2]));
                pictures.add(p);
                pics.add(p);
                startPics.add(p);           
                }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
       

        System.out.println(wallWidth + " " + wallHeight);

        // TODO: could randomly choose and place the first k pictures, and deterministically place the rest
        // if done many times (keeping best) it would be a kind of random restart
        subWalls.add(new SubWall(0, 0, wallWidth, wallHeight));
        subWas.add(new SubWall(0, 0, wallWidth, wallHeight));
        startSubs.add(new SubWall(0, 0, wallWidth, wallHeight));
        
        while (!subWalls.isEmpty()) {
            SubWall subWall = subWalls.remove(0);

            // TODO: PriorityQueue might be better since we only care about the best picture
            List<Picture> candidates = new Vector<Picture>();
            for (Picture p : pictures) {
                if ((p.width <= subWall.width) && (p.height <= subWall.height)) {
                    candidates.add(p);
                }
            }

            if (candidates.size() == 0) {
                continue;
            }

            sort(candidates);

            // TODO: instead of choosing best, could make a random choice (probably weighted by heuristic)
            // if done many times, is a kind of stochastic search
            Picture best = candidates.remove(candidates.size() - 1);

            // TODO: here, we're always placing in the upper-left; there are many other placement strategies;
            // if they had multiple possibilities, you could choose at random for another type of stochastic
            // search
            placements.add(new Placement(best.name, best.width, best.height, subWall.x, subWall.y));

            pictures.remove(best);

            // TODO: could be smarter about dividing subwalls; could also treat this as a parameter
            // to explore (e.g., pick division at random), again a kind of stochastic search
            subWalls.add(new SubWall(subWall.x + best.width, subWall.y, subWall.width - best.width, best.height));
            subWalls.add(new SubWall(subWall.x, subWall.y + best.height, subWall.width, subWall.height - best.height));
        }
           
       timer.start();
        while(!stop){
        		while (!subWas.isEmpty()) {
                    SubWall subWall = subWas.remove(0);
                    List<Picture> candidates = new Vector<Picture>();
                    for (Picture p : pics) {
                        if ((p.width <= subWall.width) && (p.height <= subWall.height)) {
                            candidates.add(p);
                        }
                    }
                    if (candidates.size() == 0) {
                        continue;
                    }
                   
                    Random rand = new Random();

                    int  n = rand.nextInt(candidates.size());
                    //System.out.println(n);

                    Picture best = candidates.remove(n);
                    places.add(new Placement(best.name, best.width, best.height, subWall.x, subWall.y));

                    pics.remove(best);
                    subWas.add(new SubWall(subWall.x + best.width, subWall.y, subWall.width - best.width, best.height));
                    subWas.add(new SubWall(subWall.x, subWall.y + best.height, subWall.width, subWall.height - best.height));
                }
        	
        	
        	
        	if(cat.equals("coverage")){
        	if(getCov(places) > getCov(placements)){
        		placements = places;
        	}
        }
        else if(cat.equals("bigger")){
        		if(getbb(places) > getbb(placements)){
        			
        			placements = places;
        		}
        	}
        
        	places = startPlace;
        	pics = startPics;
        	subWas = startSubs;
        	
        }
        
        for (Placement p : placements) {
            System.out.println(p);
        }
        
    }
}


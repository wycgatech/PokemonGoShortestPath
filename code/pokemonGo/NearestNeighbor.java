package pokemonGo;
/*
	an implementation of Nearest neighbor. It finds the nearest node for every iteration. 
	Created by Yichuan Wang
 */

import java.util.HashMap;
import java.util.LinkedList;

public class NearestNeighbor {
	
	private HashMap<Integer, Location> unvisited = new HashMap<Integer, Location>();
	private LinkedList<Integer> TSP = new LinkedList<Integer>();
	
	public NearestNeighbor(HashMap m){
		unvisited.putAll(m);
	}
	//return the TSP using nearest neighbor
	public LinkedList<Integer> findTSP(){
		int current = 1;
		int minVertex = 0;
		Location cur = unvisited.get(current);
		TSP.add(current);
		unvisited.remove(current);
		// traverse all nodes that has not been visited
		while(!unvisited.isEmpty()){
			double minLength = Double.MAX_VALUE;
			// find the closest neighbor
			for(int i : unvisited.keySet()){
				double curDis = cur.distanceTo(unvisited.get(i));
				if (curDis < minLength){
					minLength = curDis;
					minVertex = i;
				}
			}
			current = minVertex;
			cur = (Location) unvisited.get(current);
			TSP.add(minVertex);
			unvisited.remove(minVertex);
		}
		
		return TSP;
	}
	
}

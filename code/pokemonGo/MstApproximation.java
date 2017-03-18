package pokemonGo;
/*
	an implementation of MST Approximation, the findTSP method will return the path 
	Created by Yichuan Wang
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

public class MstApproximation {
	private LinkedList<Edge>[] euler;
	private LinkedList<Edge> MST = new LinkedList<Edge>();
	
	public MstApproximation(int size, LinkedList<Edge> MST){
		this.MST.addAll(MST);
		
		euler = new LinkedList[size];
		for(int i = 0; i < size; i ++){
			euler[i] = new LinkedList<Edge>();
		}
	}
	
	public LinkedList<Integer> findTSP(){
		// build Euler graph
		for(Edge e : MST){
			euler[e.getStarting() - 1].add(e);
			Edge reverse = new Edge(e.getEnding(), e.getStarting(), e.getWeight());
			euler[e.getEnding() - 1].add(reverse);
		}		
		
		// find Euler tour
		int totalSize = MST.size() * 2;
		Stack st = new Stack();
		int current = 1;
		ArrayList<Integer> tour = new ArrayList<Integer>();
		LinkedList<Integer> TSP = new LinkedList<Integer>();
		HashMap m = new HashMap();
		// build a Euler graph and find the tour, adding one additional edge for each edge in MST
		while(totalSize != 0){
			if (euler[current - 1].isEmpty()){
				tour.add(current);
				current = (Integer) st.pop();
			}
			else{
				Edge temp = euler[current - 1].removeFirst();
				totalSize --;
				st.push(current);
				current = temp.getEnding();
			}
		}
		
		tour.add(current);
		while(!st.isEmpty()){
			tour.add((int) st.pop());
		}
		// find the TSP from the Euler 
		for(int i : tour){
			if(!m.containsKey(i)){
				TSP.add(i);
				m.put(i," ");
			}
		}
				
		return TSP;
	}
}

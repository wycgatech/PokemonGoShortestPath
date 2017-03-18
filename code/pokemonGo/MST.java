package pokemonGo;
/*
	An implementation of Kruskal's algorithm.
	Created by Yichuan Wang
 */

import java.util.*;

public class MST {
	
	private LengthComparator comparator = new LengthComparator();
	private PriorityQueue<Edge> queue = new PriorityQueue<Edge>(comparator);
	private ArrayList<Location> locations = new ArrayList<Location>();
	private LinkedList<Edge> MST = new LinkedList();
	
	public MST(ArrayList<Location> locations){
		this.locations.addAll(locations);		
	}
	
	/*
	 * Compute edges between every two vertices and add them to the queue
	 */
	public void addEdges(){
		Location[] locArray = new Location[locations.size()];
		locArray = locations.toArray(locArray);
		for (int i = 0; i < locArray.length - 1; i ++){
			for (int j = i + 1; j < locArray.length; j ++){
				Edge temp = new Edge(locArray[i].getId(), locArray[j].getId(), locArray[i].distanceTo(locArray[j]));
				queue.add(temp);
			}
		}
	}
	
	/*
	 * Build MST
	 */
	public LinkedList<Edge> buildMST(){
		addEdges();
		AdjacencyList A = new AdjacencyList(locations.size() + 1);
		while(!queue.isEmpty()){
			if(!queue.isEmpty()){
				Edge temp = queue.poll();
				if (!isCycle(A, temp)){
					MST.add(temp);
				}
				else{
					A.removeEdge(temp);
				}
			}
		}

		return MST;
	}
	
	/*
	 * Detect if there forms a cycle by adding a new edge
	 */
	public static boolean isCycle(AdjacencyList a, Edge e){
		
		int n = a.getNumofVer();
		a.addEdge(e);
        boolean visited[] = new boolean[n];
        for (int i = 0; i < n; i++){
            visited[i] = false;
        }
        for (int j = 0; j < n; j++){
        	if(!visited[j]){
        		if(a.detectCycle(j, visited, -1)){
        			return true;
        		}
        	}
        }
		return false;
	}
}

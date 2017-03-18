/*
	Implement a adjancency List for detecting cycles in MST
	Created by Yichuan Wang
 */
package pokemonGo;
import java.util.*;

public class AdjacencyList {
	
	private int num_vertices;
	private LinkedList<Integer> adj[];
	
	// Constructor
	public AdjacencyList(int n){
		num_vertices = n;
		adj  = new LinkedList[n];
		for (int i = 0; i < n; i ++){
			adj[i] = new LinkedList();
		}
	}
	
	/**
	 * Get number of vertices 
	 * @return number of vertices 	 
	 */
	public int getNumofVer(){
		return num_vertices;
	}
	
	/**
	 * Print the list 
	 */
	public void printAll(){
		System.out.println("Current A list: ");
		for(int i = 0; i < num_vertices; i ++){
			System.out.println("next adj[" + i + "]");
			for(int j : adj[i]){
				System.out.println(i + " " + j);
			}
		}
	}
	
	/**
	 * Add an edge to the adjacency list
	 * @param e The edge to be added
	 */
	public void addEdge(Edge e){
		adj[e.getStarting()].add(e.getEnding());
		adj[e.getEnding()].add(e.getStarting());
	}
	
	/** 
	 * Remove an edge
	 * @param e The edge to remove
	 */
	public void removeEdge(Edge e){
		int u = e.getStarting();
		int v = e.getEnding();
		int index = adj[u].indexOf(v);
		adj[u].remove(index);
		index = adj[v].indexOf(u);
		adj[v].remove(index);
	}
	
	/**
	 * Detect if there is a cycle
	 * @param  cur     current node
	 * @param  visited visited node
	 * @param  parent  parent node
	 * @return         Whether there is a cycle
	 */
	public boolean detectCycle(int cur, boolean[] visited, int parent){
		
		int temp;
		visited[cur] = true;
		
		Iterator<Integer> it = adj[cur].iterator();
		while (it.hasNext()){
			temp = it.next();
			
			if (!visited[temp]){
				if (detectCycle(temp, visited, cur))
					return true;
			}
			else if (temp != parent){
				return true;
			}
		}
		return false;
	}

}

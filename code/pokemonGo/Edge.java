package pokemonGo;
/*
 * Implementation of the graph, store all the edges in the graph
 */
public class Edge {
	private int starting;
	private int ending;
	private double weight;
	
	
	public Edge(int starting, int ending, double length){
		this.starting = starting;
		this.ending = ending;
		this.weight = length;
	}
	
	// Copy constructor
	public Edge(Edge input){
		starting = input.starting;
		ending = input.ending;
		weight = input.weight;
	}

	public int getStarting(){
		return starting;
	}
	
	public int getEnding(){
		return ending;
	}
	
	public double getWeight(){
		return weight;
	}
	
	public String getAll(){
		return starting + " " + ending + " " + weight;
	}
	
	public void setStarting(int starting){
		this.starting = starting;
	}
	
	public void setEnding(int ending){
		this.ending = ending;
	}
	
	public void setLength(int weight){
		this.weight = weight;
	}
	
}

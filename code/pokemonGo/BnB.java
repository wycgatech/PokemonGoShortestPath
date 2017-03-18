package pokemonGo;
/**
 * This is an implementation of Branch and Bound algorithm. The findoptimal method will return 
 * an optimal solution before cutoff runs out
 * Created by youssefhammoud and Yichuan on 12/1/16.
 */
import pokemonGo.Node;
import pokemonGo.ReducedMatrix;

import java.io.PrintWriter;
import java.util.*;


public class BnB {
    // Comparator for Priority queue
    private static class LbComparator implements Comparator<Node>{
        @Override
        public int compare(Node x, Node y){

            if (x.getPath().getLocations().size() < y.getPath().getLocations().size()) {
                return 1;
            } else if (x.getPath().getLocations().size() > y.getPath().getLocations().size()) {
                return -1;
            } else {
                if (x.getLowerBound() < y.getLowerBound()) {
                    return -1;
                } else if (x.getLowerBound() > y.getLowerBound()){
                    return 1;
                } else {
                    return (int) x.getPath().getTotalDistance() - (int) y.getPath().getTotalDistance();
                }
            }
        }
    }
    private LbComparator comparator = new LbComparator();
    private double[][] adj; // a list of input
    private Map<Integer, Location> locations; // a hashmap of locations
    private int n; // number of vertices
    double initial;
    private Random randy;
    public PrintWriter output;
    String outputFile;
    long cutoff_time;




    public BnB(ArrayList<Location> adj, HashMap locations, long cutOff, int seed, String path, String city) throws Exception{
        // Need a seed to shuffle
        randy = new Random(seed);
        initial = new Tour(adj, randy).getTotalDistance();
        n = adj.size();
        this.adj = new double[n][n];
        for (int i = 0; i < adj.size(); i++) {
            for (int j = 0; j< adj.size(); j++) {
                if (i != j) {
                    double dist = adj.get(i).distanceTo(adj.get(j));
                    this.adj[i][j] = dist;
                } else {
                    this.adj[i][j] = Double.MAX_VALUE;
                }
            }
        }
        this.locations = new HashMap<Integer, Location>();
        this.locations.putAll(locations);
        this.cutoff_time = (long) (cutOff * Math.pow(10, 9));
        outputFile = path + city + "_BnB_" + cutOff + ".trace";
        this.output = new PrintWriter(outputFile, "UTF-8");
        MST mst = new MST(adj);
        LinkedList<Edge> MST = mst.buildMST();
        MstApproximation mstApp = new MstApproximation(adj.size(), MST);
        LinkedList<Integer> TSP = mstApp.findTSP();
        int previous = 1;
        this.initial = 0;
        for (int s : TSP){
            Location temp = (Location) locations.get(s);
            this.initial = initial + temp.distanceTo((Location) locations.get(previous));
            previous = s;
        }
    }

    /**
     * Find the optimal tour. Terminate when the queue is empty or there is no time left
     * @return the optimal tour
     */
    public Tour findOptimal() {
        long startTime = System.nanoTime();
        PriorityQueue<Node> queue = new PriorityQueue<Node>(comparator);
        double bestTourCost = initial;
        ReducedMatrix initialMatrix = new ReducedMatrix(adj);
        initialMatrix.rowReduction();
        initialMatrix.columnReduction();
        Node dumParent = new Node(-1);
        Node start = new Node(dumParent, locations.get(1), new Tour(new ArrayList<>()), initialMatrix);
        start.appendPath();
        queue.add(start);
        Node best = new Node(-1);

        while(!queue.isEmpty()) {
            if ((System.nanoTime() - startTime) < cutoff_time) {
                Node current = queue.poll();
                double parentLB = current.computeLowerBound();
                //check if the lower bound is smaller than the global bound
                if (parentLB < bestTourCost) {
                    current.setLevel(current.getParent().getLevel() + 1);
                    if (current.getLevel() == n - 1) {
//                    current.appendPath(start.getLocation());
//                    // If there is a leaf, check if it is smaller than the current bound and update accordingly
                        if (parentLB < bestTourCost) {
                            bestTourCost = parentLB;
                            best = current;
                            output.format("%.2f,%d%n", (System.nanoTime() - startTime) / 1e9, (int) bestTourCost);
                        }
                    } else {
                        // add the remaining node into the queue
                        for (int i = 2; i <= n; i++) {
                            if (!current.getPath().containsLocation(locations.get(i))) {
                                Node temp = new Node(current, locations.get(i), current.getPath(), current.getRm());
                                temp.appendPath();
                                temp.computeLowerBound();
                                if (temp.getLowerBound() < bestTourCost) {
                                    queue.add(temp);
                                }
                            }
                        }
                    }
                }
            } else {
                output.close();
                return best.getPath();
            }
        }
        output.close();
        return best.getPath();
    }
}

package pokemonGo;

/**
 * a class which stores variables we need in branch and bound. 
 * Created by youssefhammoud on 12/2/16.
 */
public class Node {
    private Node parent;    // the last node
    private double lowerBound;   
    private Location location;  
    private int level;   
    private Tour path;   // partial path for the node
    ReducedMatrix rm;   //reduced matrix that can calculate the lower bound
    boolean notInQueue;     // check if it has been included in the queue
    private int id;

    public Node(int level) {
        this.level = level;
        this.lowerBound = 0;
    }

    public Node(double lb, int id) {
        this.lowerBound = lb;
        this.id = id;
    }

    public Node(Node parent, Location location, Tour path, ReducedMatrix rm) {
        this.parent = parent;
        this.location = location;
        this.path = new Tour(path.getLocations());
        this.notInQueue = false;

        if (parent.getLevel() != -1) {
            this.rm = new ReducedMatrix(rm.getRm());
            this.rm.setIthRowAndJthColumnToInfinity(parent.getLocation().getId(), this.getLocation().getId());
            this.rm.setCellToInfinity(this.getLocation().getId(), parent.getLocation().getId());
            this.rm.rowReduction();
            this.rm.columnReduction();
        } else {
            this.rm = new ReducedMatrix(rm);
        }
    }
    // huge amount of getters and setters
    public int getLevel() {
        return this.level;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNotInQueue(boolean notInQueue) {
        this.notInQueue = notInQueue;
    }

    public boolean getNotInQueue() {
        return this.getNotInQueue();
    }

    public Tour getPath() {
        return path;
    }

    public ReducedMatrix getRm() {
        return rm;
    }



    public double getLowerBound() {
        return lowerBound;
    }

    public Location getLocation() {
        return this.location;
    }

    public Node getParent() {
        return parent;
    }

    public void setRm(ReducedMatrix rm) {
        this.rm = rm;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setPath(Tour path) {
        this.path = path;
    }


    public void setId(Location id) {
        this.location = location;
    }

    public void setLowerBound(double lowerBound) {
        this.lowerBound = lowerBound;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public double computePath() {
        return this.path.getTotalDistance();
    }
    // compute the lower bound based on the reduced matrix
    public double computeLowerBound() {
        if (parent.getLevel() == -1) {
            this.lowerBound = this.getRm().getReductionCost();
            return this.lowerBound;
        }

        double parentLB = parent.getLowerBound();
        double cost = parent.rm.getRm()[parent.location.getId()-1][this.location.getId()-1];
        double r = this.getRm().getReductionCost();
        this.lowerBound = parentLB + cost + r;


        return this.lowerBound;
    }
    // add node to the path
    public void appendPath() {
        this.path.addLocationToTour(this.location);
    }

    public void appendPath(Location l) {
        this.path.addLocationToTour(l);
    }

    // update the reduced matrix for invalid nodes
    public void setNodeAndParentRMToInfinity() {
        parent.getRm().getRm()[this.location.getId()][this.parent.location.getId()] = Integer.MAX_VALUE;
    }

    public String toString() {
        String str = "";
        for (Location l : this.getPath().getLocations()) {
            str += l.getId() + " ";
        }
        return str;
    }



}

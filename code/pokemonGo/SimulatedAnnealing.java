/*
    An implementation of simuated annealing
 */
package pokemonGo;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class SimulatedAnnealing {

    private double init_temperature;
    private double alpha;
    private double min_temperature;
    private int seed;
    private Random randy;
    long cutoff_time;
    public PrintWriter output;
    String outputFile;

    private Set<Tour> visited = new HashSet<Tour>();

    public SimulatedAnnealing(String city, double alpha, double init_temperature, double min_temperature, int seed, long cutOff, String path) throws IOException {
        this.init_temperature = init_temperature;
        this.min_temperature = min_temperature;
        this.alpha = alpha;
        this.randy = new Random(seed);
        this.seed = seed;
        this.cutoff_time = (long) (cutOff * Math.pow(10, 9));
        outputFile = path + city + "_LS2_" + cutOff + "_" + seed + ".trace";
        this.output = new PrintWriter(outputFile, "UTF-8");


    }

    /**
     * Get the initial temparture
     * @return initial temparture
     */
    public double getInit_temperature() {
        return init_temperature;
    }
    /**
     * Get the alpha value
     * @return alpha
     */
    public double getAlpha() {
        return alpha;
    }

    /**
     * Get the minimum temperature
     * @return minimum temperature      
     */
    public double getMin_temperature() {
        return min_temperature;
    }

    /**
     * Get the seed
     * @return seed
     */
    public int getSeed() {
        return seed;
    }

    /**
     * Get the visited set
     * @return visited     
     */
    public Set<Tour> getVisited() {
        return visited;
    }

    /**
     * Calculate the accpetance probability and evaluate
     * @param  dist_old  olution qualtity of the previous search
     * @param  dist_new solution qualtity of the curr search
     * @param  temp     tempearture
     * @return  whether should accpet the current solution   
     */
    public boolean acceptance_probability(double dist_old, double dist_new, double temp) {
        double ap = Math.exp(((dist_old-dist_new)/Math.pow(10,4))/temp);
        double r = this.randy.nextDouble();

        if (ap > r) {
            return true;
        }
        return false;
    }

    /**
     * nitiate a simulated annealing search
     * @param  tour A initial tour to start
     * @return      The best tour found
     */
    public Tour findtour(Tour tour) {
        long start_time = System.nanoTime();
        double temperature = getInit_temperature();
        int jumps = 0;
        Tour best_tour = tour;
        double best_dist = best_tour.getTotalDistance();
        Tour globalBest = tour;
        while (temperature > min_temperature)  {
            if (System.nanoTime() - start_time >= cutoff_time) {
                return globalBest;
            }
            Tour new_tour = twoOpt(best_tour);
            double dist_new = new_tour.getTotalDistance();
            boolean ap = acceptance_probability(best_dist, dist_new, temperature);
            if (dist_new < best_dist || ap) {
                best_tour = new_tour;
                best_dist = dist_new;
                if (dist_new < globalBest.getTotalDistance()) {
                    globalBest = new_tour;
                    long timestamp = System.nanoTime() - start_time;
                    // System.out.println(timestamp/1e9 + "\t" + globalBest.getTotalStringDistance());
                    output.format("%.2f,%d%n", timestamp/1e9, (int)globalBest.getTotalDistance());

                }
            }
            temperature = temperature * alpha;
        }
        return globalBest;
    }


    /** 
     * Helper method for generating a new tour based on perturbation
     * @param  candidate current tour
     * @return         a  new tour
     */
    private Tour twoOpt(Tour candidate) {

        int length = candidate.getLocations().size();
        Location[] output = new Location[length];
        int cut1 = 0;
        int cut2 = 0;
        while (cut1 >= cut2) {
            cut1 = (int) (candidate.getLocations().size() * randy.nextDouble());
            cut2 = (int) (candidate.getLocations().size() * randy.nextDouble());
        }
        Location[] array = candidate.getLocations().toArray(new Location[length]);

        System.arraycopy(array, 0, output, 0, cut1);
        Location[] middle = new Location[cut2 - cut1];
        System.arraycopy(array, cut1, middle, 0, cut2 - cut1);
        for (int i = 0; i < cut2 - cut1; i ++) {
            middle[i] = array[cut2 - i - 1];
        }
        System.arraycopy(middle, 0, output, cut1, cut2 - cut1);
        System.arraycopy(array, cut2, output, cut2, array.length - cut2);
        ArrayList<Location> newList = new ArrayList<>(
                Arrays.asList(output)
        );

        Tour newTour = new Tour(newList);

        return newTour;
    }

    /**
     * Get the random number generator
     * @return random number generator
     */
    public Random getRandy() {
        return randy;
    }







}
/**
 * Created by chenzhijian on 11/19/16.
 */
import pokemonGo.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Driver {

    public static void main(String[] args) throws Exception {

        //Validate enough arguments
        if (args.length < 8) {
            System.out.println("Please enter arguments required correctly.");
            System.exit(-1);
        }
        String city = args[1];

        String city_file = city + ".tsp";


        String alg = args[3];
        int cutoff = Integer.parseInt(args[5]);
        int seed = Integer.parseInt(args[7]);




        String path = "./";

        //Read corresponding input file

        BufferedReader br = new BufferedReader(new FileReader(city_file));
        String line = null;
        HashMap<Integer, Location> m = new HashMap<>();


        ArrayList<Location> route = new ArrayList<>();
        int i = 0;

        //Get rid of the first 4 lines

        while(i <= 4) {
            line = br.readLine();
            i++;

        }

        //Read in from the files
        while ((line = br.readLine()) != null && !line.contains("EOF")) {
            String[] splitLine = line.split(" ");
            int id = Integer.parseInt(splitLine[0]);
            double longitude = Double.parseDouble(splitLine[1]);
            double lat = Double.parseDouble(splitLine[2]);
            Location l = new Location(id, longitude, lat);
            route.add(l);
            m.put(id, l);
        }


        //Run LS1
        if (alg.equals("LS1")) {
            IterativeLocalSearch ils = new IterativeLocalSearch(city, cutoff, seed, path);

            Tour r = new Tour(route);

            Tour bestTour = ils.run(r);
            printSolution(bestTour, path, city, alg, cutoff, seed, 1);
        } else if (alg.equals("LS2")) { //Run LS2
            SimulatedAnnealing sa = new SimulatedAnnealing(city, 1 - Math.pow(10, -6), 1.0, 0.00001, seed, cutoff, path);
            Tour r = new Tour(route, sa.getRandy());

            Tour bestTour = sa.findtour(r);
            sa.output.close();
            printSolution(bestTour, path, city, alg, cutoff, seed, 1);
        } else if (alg.equals("APP1")) {
            //MST-Approximation
            MST mst = new MST(route);
            long start = System.nanoTime();
            LinkedList<Edge> MST = mst.buildMST();
            MstApproximation mstApp = new MstApproximation(route.size(), MST);
            LinkedList<Integer> TSP = mstApp.findTSP();
            
            long end = System.nanoTime();
            double time = (end - start) / 1e9;
            ArrayList<Location> result= new ArrayList<Location>();
            for (int wyc : TSP){
                result.add(m.get(wyc));
            }
            Tour bestTour = new Tour(result);
            printSolution(bestTour, path, city, alg, cutoff, seed, 0);
            String outputFile = path + city + "_" + alg + "_" + cutoff + ".trace";
            PrintWriter output = new PrintWriter(outputFile, "UTF-8");
            output.format("%.2f,%d%n", time, (int)bestTour.getTotalDistance());
            output.close();

        } else if (alg.equals("APP2")) { //Run Approximation using nearest neighbor
            NearestNeighbor nearNb = new NearestNeighbor(m);
            long start = System.nanoTime();
            LinkedList<Integer> TSP = nearNb.findTSP();
            long end = System.nanoTime();
            double time = (end - start) / 1e9;
            
            ArrayList<Location> result= new ArrayList<Location>();
            for (int wyc : TSP){
                result.add(m.get(wyc));
            }
            Tour bestTour = new Tour(result);
            printSolution(bestTour, path, city, alg, cutoff, seed, 0);
            String outputFile = path + city + "_" + alg + "_" + cutoff  + ".trace";
            PrintWriter output = new PrintWriter(outputFile, "UTF-8");
            output.format("%.2f,%d%n", time, (int)bestTour.getTotalDistance());
            output.close();

        } else if (alg.equals("BNB")){ //RUN branch and bound
            BnB bnb = new BnB(route, m, cutoff, seed, path, city);
            Tour bestTour = bnb.findOptimal();
            // System.out.println(bestTour.toString());
            bnbPrintSolution(bestTour, path, city, alg, cutoff, seed);
        }
        else {
            System.out.println("INVALID INPUT");
        }
    }

    /** Print a solution to a sol file, used for both local search and approximation algorithms
     * @param bestTour
     * @param path
     * @param city
     * @param alg
     * @param cutoff
     * @param seed
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public static void printSolution(Tour bestTour, String path, String city, String alg, int cutoff, int seed, int hasseed) throws FileNotFoundException, UnsupportedEncodingException {
        String solutionFile;
        if (hasseed == 0) {
            solutionFile = path + city + "_" + alg + "_" + cutoff+ ".sol";      
        } else {
            solutionFile = path + city + "_" + alg + "_" + cutoff + "_" + seed + ".sol"; 
        }

        PrintWriter solution = new PrintWriter(solutionFile, "UTF-8");

        ArrayList<Location> locations = bestTour.getLocations();
        int cost;
        int tourSize = bestTour.getSize();
        solution.format("%d%n", (int)bestTour.getTotalDistance());

        for (int i = 0; i < tourSize -1; i++) {
            cost = (int)locations.get(i).distanceTo(locations.get(i + 1));
            solution.format("%d %d %d%n", locations.get(i).getId(), locations.get(i+1).getId(), cost);
        }

        cost = (int)locations.get(tourSize-1).distanceTo(locations.get(0));
        solution.format("%d %d %d%n", locations.get(tourSize-1).getId(), locations.get(0).getId(), cost);
        solution.close();
    }




    /** Print a solution to a sol file, used for branch and bound
     * @param bestTour
     * @param path
     * @param city
     * @param alg
     * @param cutoff
     * @param seed
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public static void bnbPrintSolution(Tour bestTour, String path, String city, String alg, int cutoff, int seed) throws FileNotFoundException, UnsupportedEncodingException {
        String solutionFile = path + city + "_" + alg + "_" + cutoff + ".sol";
        PrintWriter solution = new PrintWriter(solutionFile, "UTF-8");

        ArrayList<Location> locations = bestTour.getLocations();
        int cost;
        int tourSize = bestTour.getSize();
        solution.format("%d%n", (int)bestTour.getTotalDistance());

        for (int i = 0; i < tourSize; i++) {
//
            cost = (int) Math.ceil(locations.get(i).distanceTo(locations.get(i + 1)));
            solution.format("%d %d %d%n", locations.get(i).getId(), locations.get(i+1).getId(), cost);


        }
        cost = (int) locations.get(tourSize).distanceTo(locations.get(0));
        solution.format("%d %d %d%n", locations.get(tourSize).getId(), locations.get(0).getId(), cost);

        solution.close();
    }
}
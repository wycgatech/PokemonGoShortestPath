package pokemonGo;

/**
 * Used for calculating the lower bound. 
 * Created by youssefhammoud on 12/3/16.
 */


public class ReducedMatrix {
    private double[][] rm;
    private double reductionCost;

    public ReducedMatrix(double[][] adj) {

        this.rm = new double[adj.length][adj.length];
        for (int i = 0; i < rm.length; i++) {
            for (int j = 0; j < rm.length; j++) {
                rm[i][j] = adj[i][j];
            }
        }

        this.reductionCost = 0;
    }

    public ReducedMatrix(ReducedMatrix rm) {
        double[][] temp = rm.getRm();
        this.rm = new double[temp.length][temp.length];
        for (int i = 0; i < this.rm.length; i++) {
            for (int j = 0; j < this.rm.length; j++) {
                this.rm[i][j] = temp[i][j];
            }
        }
        this.reductionCost = rm.getReductionCost();
    }


    public void rowReduction() {
        double totalRowReduction = 0;
        for (int i = 0; i < rm.length; i++) {
            double min = findMinRow(rm, i);
            if (min != Double.MAX_VALUE) {
                totalRowReduction += min;
                for (int j = 0; j < rm.length; j++) {
                    rm[i][j] = rm[i][j] - min;
                }
            } else {
                for (int j = 0; j < rm.length; j++) {
                    rm[i][j] = rm[i][j];
                }
            }
        }
        this.reductionCost += totalRowReduction;
    }

    public void columnReduction() {
        double totalColReduction = 0;
        for (int i = 0; i < rm.length; i++) {
            double min = findMinCol(rm, i);
            if (min != Double.MAX_VALUE) {
                totalColReduction += min;
                for (int j = 0; j < rm.length; j++) {
                    rm[j][i] = rm[j][i] - min;
                }
            } else {
                for (int j = 0; j < rm.length; j++) {
                    rm[j][i] = rm[j][i];
                }
            }
        }
        this.reductionCost += totalColReduction;
    }

    public double findMinRow(double[][] m, int i) {
        double min = Double.MAX_VALUE;
        for (int j = 0; j < m.length; j++) {
            if (min > m[i][j]) {
                min = m[i][j];
            }
        }
        return min;
    }

    public double findMinCol(double[][] m, int j) {
        double min = Double.MAX_VALUE;
        for (int i = 0; i < m.length; i++) {
            if (min > m[i][j]) {
                min = m[i][j];
            }
        }
        return min;
    }

    public void setReductionCost(double lowerBound) {
        this.reductionCost = lowerBound;
    }

    public void setRm(double[][] rm) {
        this.rm = rm;
    }

    public double getReductionCost() {
        return reductionCost;
    }

    public double[][] getRm() {
        return rm;
    }

    public String toString() {
        String printRM = "";

        for (int i = 0; i < this.rm.length; i++) {
            for (int j = 0; j < this.rm.length; j++) {
                if (j == this.rm.length - 1) {
                    if (this.rm[i][j] == Double.MAX_VALUE) {
                        printRM += "--\n";
                    } else {
                        printRM += this.rm[i][j] + "\n";
                    }
                } else {
                    if (this.rm[i][j] == Double.MAX_VALUE) {
                        printRM += "--\t";
                    } else {
                        printRM += this.rm[i][j] + "\t";
                    }
                }

            }
        }
        printRM += "\nReduction Cost: " + this.reductionCost;

        return printRM;
    }

    public void setIthRowAndJthColumnToInfinity(int i, int j) {
        for (int k = 0; k < rm.length; k++) {
            this.rm[i-1][k] = Double.MAX_VALUE;
            this.rm[k][j-1] = Double.MAX_VALUE;
        }
    }

    public void setCellToInfinity(int i, int j) {
        this.rm[i-1][j-1] = Double.MAX_VALUE;
    }

}

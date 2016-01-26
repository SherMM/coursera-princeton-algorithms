/******************************************************************************
*  Compilation:  javac-algs4 PercolationStats.java
*  Execution:    java-algs4 PercolationStats N T
*  Dependencies: StdRandom.java, StdStats.java, StdIn.java
*
*
*  A mutable grid system representing a system we want to test for
*  percolation. A system percolation if any site on the top is connected
*  to any site on the bottom of the grid through adjacent grid squares.
*
*
******************************************************************************/

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdIn;

/**
 * PercolationStats class is used to run T tests on an N*N
 * percolation system to calculate statistics related to the
 * percolation threshold
 *
 * @author Ian Laurain
 *
 */
public class PercolationStats {
private int n;   // grid size
private int t;   // number of times to repeat experiment
private double[] total;   // stores percolation probability sum for t trials

public PercolationStats(int N, int T) {
        if (N <= 0) {
                throw new IllegalArgumentException("N must be greater than 0");
        }
        if (T <= 0) {
                throw new IllegalArgumentException("T must be greater than 0");
        }

        n = N;
        t = T;
        int i; // indices for random spot to open
        int j;
        double count; // nubmer of open sites
        Percolation p;
        total = new double[t]; // array storing count / spaces results
        double spaces = (double)n*n;
        for (int k = 0; k < t; k++) {
                count = 0;
                p = new Percolation(n);
                while (!p.percolates()) {
                        i = StdRandom.uniform(1, n+1);
                        j = StdRandom.uniform(1, n+1);
                        if (!p.isOpen(i, j)) {
                                p.open(i, j);
                                count++;
                        }
                }
                total[k] = count / spaces;
        }

}

/**
 * Returns mean of count / spaces results for T tests
 *
 * @return the mean of total results array
 */
public double mean() {
        return StdStats.mean(total);
}

/**
 * Returns stddev of count / spaces results for T tests
 *
 * @return the stddev of total results array
 */
public double stddev() {
        return StdStats.stddev(total);
}

/**
 * Returns lower 0.95 confidence bound of count / spaces results for T tests
 *
 * @return the lower conf intv of total results array
 */
public double confidenceLo() {
        double level = 1.96;
        return this.mean() - (level*this.stddev()) / Math.sqrt(t);
}

/**
 * Returns upper 0.95 confidence bound of count / spaces results for T tests
 *
 * @return the upper conf intv of total results array
 */
public double confidenceHi() {
        double level = 1.96;
        return this.mean() + (level*this.stddev()) / Math.sqrt(t);
}

/**
 * Testing section
 */
public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);         // N-by-N percolation system
        int T = Integer.parseInt(args[1]);         // T number of trials

        PercolationStats ps = new PercolationStats(N, T);
        System.out.println("mean                    = " + ps.mean());
        System.out.println("stddev                  = " + ps.stddev());
        System.out.println("95% confidence interval = " + ps.confidenceLo() + ", " + ps.confidenceHi());
}
}

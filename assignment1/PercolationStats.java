import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/*
** PercolationStats performs some computational
** experiments on Percolation
*/
public class PercolationStats {

    // percolation points of the trials
    private final double[] result;
    // number of trials
    private final int t;
    //  confidence interval for 0.95 confidence
    private final double CONFIDENCE_95 = 1.96;

    public PercolationStats(int n, int trials) {
        // perform trials independent experiments
        // on a n-by-n grid
        if (n < 1) 
            throw new IllegalArgumentException("grid size out of bound: " + n);
        if (trials < 1)
            throw new IllegalArgumentException("numebr of trials out of bound: " + trials);
        
        this.t = trials;
        this.result = new double[trials];

        for (int i = 0; i < t; i++) {
            Percolation p = new Percolation(n);

            // open random sites until p percolates
            while (!p.percolates()) {
                // open a random site in closed sites
                while (true) {
                    int row = StdRandom.uniform(1, n+1);
                    int col = StdRandom.uniform(1, n+1);
                    if (p.isOpen(row, col)) continue;
                    else {
                        p.open(row, col); 
                        break;
                    }
                }
            }

            result[i] = (double) p.numberOfOpenSites() / (n*n); 
        }
    }

    public double mean() {
        // sample mean of percolation threshold
        return StdStats.mean(result);
    }

    public double stddev() {
        // sample standard deviation of percolation
        // threshold
        return StdStats.stddev(result);
    }

    public double confidenceLo() {
        // low end point of 95% confidence interval
        return mean() - CONFIDENCE_95 * stddev()/Math.sqrt(t);
    }

    public double confidenceHi() {
        // high end point of 95% confidence interval
        return mean() + CONFIDENCE_95 * stddev()/Math.sqrt(t);
    }
    
    public static void main(String[] args) {
        // unit test
        for (int i = 10; i <= 100; i = i+10) {
            PercolationStats pStats = new PercolationStats(i, 100);
            System.out.println("percolation threshold for " + i + " * " + i + " grid :");
            System.out.println("mean: " + pStats.mean());
            System.out.println("standard deviation: " + pStats.stddev());
            System.out.println("confidence range: (" + pStats.confidenceLo() + ", " + pStats.confidenceHi() + ")\n");
        }
    }

}
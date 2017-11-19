import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/*
** a Percolation system for a n-by-n grid, the sites are
** indexed from 1 to n. The virtual top & virtual bottom
** method is not used so that backwash can be avoided.
*/
public class Percolation {

    // the union-find data structure for grid sites
    private final WeightedQuickUnionUF uf;
    // open[row][col] == true if site (row, col) is open
    private boolean[][] open;
    // connectsToTop[ufRootIndex(row, col)] == true if 
    // site (row, col) connects to top
    private boolean[] connectsToTop;
    // connectsToBtm[ufRootIndex(row, col)] == true if 
    // site (row, col) connects to bottom
    private boolean[] connectsToBtm;
    // number of open sites
    private int numberOfOpenSites;
    // number of sites on one grid edge
    private final int n;
    private boolean percolates; // true if the grid percolates

    public Percolation(int n) {
        if (n < 1) 
            throw new IllegalArgumentException("non-positive grid size: " + n);
        
        // the 2-d array is n+2 * n+2 so that the wall
        // (row or col = 0 or n+1) can be represented
        // as open[row][col] == false
        this.open = new boolean[n+2][n+2];
        this.connectsToTop = new boolean[n*n+1];
        // all sites on row 1 are connected to the top
        for (int i = 1; i <= n; i++) connectsToTop[i] = true;
        this.connectsToBtm = new boolean[n*n+1];
        // all sites on row n are connected to the bottom
        for (int i = 1; i <= n; i++) connectsToBtm[n*(n-1) + i] = true;
        this.numberOfOpenSites = 0;
        this.n = n;
        // create a n-by-n grid, with all sites blocked
        this.uf = new WeightedQuickUnionUF(n*n+1);
    }

    public void open(int row, int col) {
        if (outOfRange(row, col)) 
            throw new IllegalArgumentException("grid site out of range: (" + row + ", " + col + ")");
        
        // open site (row, col) if it is not open already
        if (open[row][col]) return;
        
        // can the newly opened site reach the top?
        boolean canReachTop = connectsToTop[ufRootIndex(row, col)];
        // can the newly opened site reach the bottom?
        boolean canReachBtm = connectsToBtm[ufRootIndex(row, col)];
        open[row][col] = true;
        numberOfOpenSites++;
        // check neighboring sites, if already opened, then connect the two
        // if any of the neighboring sites connects to top/bottom then the
        // resulting connected component connects to top/bottom
        if (open[row-1][col]) {
            if (connectsToTop[ufRootIndex(row-1, col)]) canReachTop = true;
            if (connectsToBtm[ufRootIndex(row-1, col)]) canReachBtm = true;
            uf.union(ufIndex(row-1, col), ufIndex(row, col));
        }
        if (open[row+1][col]) {
            if (connectsToTop[ufRootIndex(row+1, col)]) canReachTop = true;
            if (connectsToBtm[ufRootIndex(row+1, col)]) canReachBtm = true;
            uf.union(ufIndex(row+1, col), ufIndex(row, col));
        }
        if (open[row][col-1]) {
            if (connectsToTop[ufRootIndex(row, col-1)]) canReachTop = true;
            if (connectsToBtm[ufRootIndex(row, col-1)]) canReachBtm = true;
            uf.union(ufIndex(row, col-1), ufIndex(row, col));
        }
        if (open[row][col+1]) {
            if (connectsToTop[ufRootIndex(row, col+1)]) canReachTop = true;
            if (connectsToBtm[ufRootIndex(row, col+1)]) canReachBtm = true;
            uf.union(ufIndex(row, col+1), ufIndex(row, col));
        }
        
        // update connectsToTop/Btm for the resulting connected component
        connectsToTop[ufRootIndex(row, col)] = canReachTop;
        connectsToBtm[ufRootIndex(row, col)] = canReachBtm;
        
        if (canReachTop && canReachBtm) percolates = true;
    }

    public boolean isOpen(int row, int col) {
        if (outOfRange(row, col)) 
            throw new IllegalArgumentException("grid site out of range: (" + row + ", " + col + ")");
        
        // is site (row, col) open?
        return open[row][col];
    }

    public boolean isFull(int row, int col) {
        if (outOfRange(row, col)) 
            throw new IllegalArgumentException("grid site out of range: (" + row + ", " + col + ")");
        
        if (!isOpen(row, col)) return false;

        return connectsToTop[ufRootIndex(row, col)];
    }

    public int numberOfOpenSites() {
        // number of open sites
        return numberOfOpenSites;
    }

    public boolean percolates() {
        // does the system percolate?
        // that is: the virtual top site is connected
        // to the virtual bottom site
        if (numberOfOpenSites == 0) return false; // for corner case n = 1
        return percolates;
    }

    private int ufIndex(int row, int col) {
        // helper function: site (row, col)'s index
        // in the union-find data structure
        return (row-1)*n + col;
    }
    
    private int ufRootIndex(int row, int col) {
        // helper function: index of the root of the connected component
        // that contains site (row, col)
        return uf.find(ufIndex(row, col));
    }
    
    private boolean outOfRange(int row, int col) {
        // helper function: check if site (row, col)
        // is out of range
        return (row < 1 || row > n || col < 1 || col > n);
    }
}
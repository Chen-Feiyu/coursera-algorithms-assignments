import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Board {
    
    private final int[][] blocks;
    private final int dimension;
    
    public Board(int[][] blocks) {
        // construct a board from an n-by-n array of blocks
        // (where blocks[i][j] = block in row i, column j)
        this.blocks = blocksCopy(blocks);
        this.dimension = blocks.length;
    }
                                           
    public int dimension() {
        // board dimension n
        return dimension;
    }
    
    public int hamming() {
        // number of blocks out of place
        int numOutOfPlace = 0;
        for (int i = 0; i < dimension; i++)
            for (int j = 0; j < dimension; j++)
                // if row i, column j is not empty, then the block there
                // should be i*dimension+j+1
                if (blocks[i][j] != 0 && blocks[i][j] != i*dimension+j+1)
                    numOutOfPlace++;
        return numOutOfPlace;
    }
    
    public int manhattan() {
        // sum of Manhattan distances between blocks and goal
        int disAccum = 0; // accumulated Manhattan distance
        for (int i = 0; i < dimension; i++)
            for (int j = 0; j < dimension; j++) {
                if (blocks[i][j] == 0) continue;
                int block = blocks[i][j];
                int iGoal = (block-1) / dimension; // block should be put in row iGoal
                int jGoal = (block-1) % dimension; // block should be put in col iGoal
                disAccum += Math.abs(i-iGoal) + Math.abs(j-jGoal);
            }
        return disAccum;
    }
    
    public boolean isGoal() {
        // is this board the goal board?
        for (int i = 0; i < dimension; i++)
            for (int j = 0; j < dimension; j++)
                if (blocks[i][j] != 0 && blocks[i][j] != i*dimension+j+1) 
                    return false;
        return true;
    }
    
    public Board twin() {
        // a board that is obtained by exchanging any pair of blocks
        Board twinBoard;
        if (blocks[0][0] != 0 && blocks[0][1] != 0) {
            // if the first two blocks in the first row are not empty, exchange them
            twinBoard = exchanged(0, 0, 0, 1);
        } else {
            // if not, exchange the first two blocks in the second row
            twinBoard = exchanged(1, 0, 1, 1);
        }
        return twinBoard;
    }
    
    public boolean equals(Object y) {
        // does this board equal y?
        if (y == null)             return false;
        if (y.getClass() != this.getClass()) 
                                   return false;
        else { 
            Board that = (Board) y; 
            if (this.dimension != that.dimension) 
                                   return false;
            for (int i = 0; i < this.dimension; i++)
                for (int j = 0; j < this.dimension; j++)
                    if (this.blocks[i][j] != that.blocks[i][j]) 
                                   return false;
            }
        return true;
    }
    
    public Iterable<Board> neighbors() {
        // all neighboring boards
        
        // the row and column of the empty position
        int iEmpty = 0, jEmpty = 0;
        outerloop:
        for (int i = 0; i < dimension; i++)
            for (int j = 0; j < dimension; j++)
                if (blocks[i][j] == 0) { 
                    iEmpty = i; 
                    jEmpty = j; 
                    break outerloop;
                }
        
        // add neighboring boards for all blocks that neighbors the empty position
        int[] iDelta = {1, 0, -1, 0};
        int[] jDelta = {0, 1, 0, -1};
        Stack<Board> neighbors = new Stack<Board>();
        for (int k = 0; k < 4; k++) {
            int iNeighbor = iEmpty + iDelta[k];
            int jNeighbor = jEmpty + jDelta[k];
            if (iNeighbor < 0 || jNeighbor < 0 || iNeighbor >= dimension || jNeighbor >= dimension) 
                continue;
            else neighbors.push(exchanged(iEmpty, jEmpty, iNeighbor, jNeighbor));
        }
        return neighbors;
    }
    
    public String toString() {
        // string representation of this board (in the output format specified below)
        StringBuilder s = new StringBuilder();
        s.append(dimension);
        for (int i = 0; i < dimension; i++) {
            s.append("\n");
            for (int j = 0; j < dimension; j++)
                s.append(String.format("\t" + blocks[i][j]));
        }
        return s.toString();
    }
    
    private Board exchanged(int i, int j, int m, int n) {
        // helper function: create a Board with the block in row i, column j
        // exchanged with the block in row m, column n
        int[][] blocksNew = blocksCopy(blocks);
        int x = blocksNew[i][j];
        blocksNew[i][j] = blocksNew[m][n];
        blocksNew[m][n] = x;
        return new Board(blocksNew);
    }
    
    private int[][] blocksCopy(int[][] blocksOrgn) {
        // helper function: return a clone of blocks
        int size = blocksOrgn.length;
        int [][] copy = new int[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                copy[i][j] = blocksOrgn[i][j];
        return copy;
    }

    public static void main(String[] args) {
        // unit tests (not graded)
        int[][] blocks1 = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};
        int[][] blocks2 = {{3, 0, 4}, {1, 2, 5}, {6, 7, 8}};
        Board board1 = new Board(blocks1);
        Board board2 = new Board(blocks2);
        
        StdOut.println("-------Board1-------\n" + board1);
        StdOut.println("testing isGoal(): " + passed(board1.isGoal()));
        StdOut.println("testing manhattan(): " + passed(board1.manhattan() == 0));
        StdOut.println("testing hamming(): " + passed(board1.hamming() == 0));
        StdOut.println("testing twin(): \n" + board1.twin());
        
        StdOut.println("-------Board2-------\n" + board2);
        StdOut.println("testing isGoal(): " + passed(!board2.isGoal()));
        StdOut.println("testing manhattan(): " + passed(board2.manhattan() == 13));
        StdOut.println("testing hamming(): " + passed(board2.hamming() == 8));
        StdOut.println("testing twin(): \n" + board2.twin());
        StdOut.println("testing neighbors():");
        for (Board b : board2.neighbors()) {
            StdOut.println(b);
        }
    }
    private static String passed(boolean bool) {
        // helper function: return "passed" for true input
        return bool ? "passed" : "failed";
    }
}
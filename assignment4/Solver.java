import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    
    private Node goal;
    
    private class Node implements Comparable<Node> {
        Board board;
        Node parent;
        int moves = 0, manhattan;
        
        public Node(Board board) {
            this.board = board;
        }
        
        public Node(Board board, Node parent) {
            this.board = board;
            this.parent =  parent;
            this.moves = parent.moves + 1;
            this.manhattan = board.manhattan();
            
        }
        public int compareTo(Node that) {
            return (this.manhattan + this.moves) - (that.manhattan + that.moves);
        }
    }
    
    public Solver(Board initial) {
        // find a solution to the initial board (using the A* algorithm)
        
        if (initial == null)
            throw new IllegalArgumentException("null input");
        
        // perform A* search, Manhattan distance acting as the heuristic function 
        
        // the reachable Node (Board) that has been discovered but not explored
        MinPQ<Node> discoveredOrgn = new MinPQ<Node>();
        discoveredOrgn.insert(new Node(initial));        // for the original problem
        MinPQ<Node> discoveredTwin = new MinPQ<Node>();
        discoveredTwin.insert(new Node(initial.twin())); // for the twin problem
        
        while (!discoveredOrgn.isEmpty() && !discoveredTwin.isEmpty()) {
            
            // the node to be explored in the original problem
            Node nodeOrgn = discoveredOrgn.delMin(); 
            if (nodeOrgn.board.isGoal()) {
                goal = nodeOrgn;
                break;
            }
            explore(nodeOrgn, discoveredOrgn);

            // the node to be explored in the twin problem
            Node nodeTwin = discoveredTwin.delMin(); 
            if (nodeTwin.board.isGoal()) {
                // the original problem is unsolvable if the twin problem is solvable
                goal = null; // unsolvable
                break; 
            }
            explore(nodeTwin, discoveredTwin);
        }
    }
    
    private void explore(Node node, MinPQ<Node> discovered) {
        for (Board b : node.board.neighbors()) 
                // add all neighboring Boards to discovered except the one that is
                // identical to its grandparent
                if (node.parent == null || !b.equals(node.parent.board)) 
                    discovered.insert(new Node(b, node));
    }
    
    public boolean isSolvable() {
        // is the initial board solvable?
        return goal != null;
    }
    
    public int moves() {
        // min number of moves to solve initial board; -1 if unsolvable
        return isSolvable() ? goal.moves : -1;
    }
    
    public Iterable<Board> solution() {
        // sequence of boards in a shortest solution; null if unsolvable
        if (!isSolvable()) return null;
        
        Stack<Board> solution = new Stack<Board>();
        Node node = goal;
        while(node != null) {
            solution.push(node.board);
            node = node.parent;
        }
        return solution;
    }
    
    public static void main(String[] args) {
        // solve the slider puzzle

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
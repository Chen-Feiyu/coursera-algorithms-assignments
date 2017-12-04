import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    
    private Node root; // the root of the Kdtree
    private int size;
    
    private static class Node {
        private final Point2D p;   // the point
        private final RectHV rect; // the coordinate-aligned rectangle corresponding to this node
        private Node lb;     // the left/bottom subtree
        private Node rt;     // the right/top tree

        public Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
        }
    }
    
    public KdTree() {
        // construct an empty set of points
        size = 0;
    }
    
    public boolean isEmpty() {
        // is the set empty? 
        return size == 0;
    }
    
    public int size() {
        // number of points in the set 
        return size;
    }
    
    public void insert(Point2D p) {
        // add the point to the set (if it is not already in the set)
        if (p == null) throw new IllegalArgumentException("null input");
        
        if (contains(p)) return;
        root = insert(p, root, true, 0.0, 0.0, 1.0, 1.0);
        size++;
    }
    private Node insert(Point2D p, Node n, boolean vertical, double xmin, double ymin, double xmax, double ymax) {
        // helper function: insert the point under the node, here vertical denotes whether this node split the plane
        // vertically, xmin, ymin, xmax, ymax specifies the rectangle to be inserted. (I admit that using four parameters
        // for a rectangle looks ugly, but it avoids creating many useless RectHVs)
        if (n == null) return new Node(p, new RectHV(xmin, ymin, xmax, ymax));
        
        boolean goesLB = lb(p, n, vertical); // goes left/bottom
        if (goesLB) {
            if (vertical) n.lb = insert(p, n.lb, !vertical, xmin, ymin, n.p.x(), ymax);
            else          n.lb = insert(p, n.lb, !vertical, xmin, ymin, xmax, n.p.y());
        } else {
            if (vertical) n.rt = insert(p, n.rt, !vertical, n.p.x(), ymin, xmax, ymax);
            else          n.rt = insert(p, n.rt, !vertical, xmin, n.p.y(), xmax, ymax);
        }
        
        return n;
    }
    private boolean lb(Point2D p, Node n, boolean vertical) {
        // helper function: should the point be put in the left/bottom of the node?
        if ( vertical && p.x() < n.p.x()) return true;
        if (!vertical && p.y() < n.p.y()) return true;
        return false;
    }
    
    public boolean contains(Point2D p) {
        // does the set contain point p?
        if (p == null) throw new IllegalArgumentException("null input");
        
        return contains(p, root, true);
    }
    private boolean contains(Point2D p, Node n, boolean vertical) {
        // helper function: check if the point in contained under the node
        if (n == null)     return false;
        if (n.p.equals(p)) return true;
        if (lb(p, n, vertical)) return contains(p, n.lb, !vertical);
        else                    return contains(p, n.rt, !vertical);
    }
    
    public void draw() {
        // draw all points to standard draw 
        draw(root, true);
    }
    private void draw(Node n, boolean vertical) {
        if (n == null) return;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        n.p.draw();
        
        StdDraw.setPenRadius();
        if (vertical) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(n.p.x(), n.rect.ymin(), n.p.x(), n.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(n.rect.xmin(), n.p.y(), n.rect.xmax(), n.p.y());
        }
        draw(n.lb, !vertical);
        draw(n.rt, !vertical);
    }
    
    public Iterable<Point2D> range(RectHV rect) {
        // all points that are inside the rectangle (or on the boundary)
        if (rect == null) throw new IllegalArgumentException("null input");
        
        Bag<Point2D> pointsInRange = new Bag<Point2D>();
        if (!isEmpty()) addPoints(root, rect, pointsInRange);
        return pointsInRange;
    }
    private void addPoints(Node n, RectHV r, Bag<Point2D> b) {
        // helper function: add all points in this subtree to the bag
        if (r.contains(n.p)) b.add(n.p);
        if (n.lb != null && n.lb.rect.intersects(r)) addPoints(n.lb, r, b);
        if (n.rt != null && n.rt.rect.intersects(r)) addPoints(n.rt, r, b);
    }
    
    public Point2D nearest(Point2D p) {
        // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null) throw new IllegalArgumentException("null input");
        
        return isEmpty()? null : nearest(p, root, true, root.p);
    } 
    private Point2D nearest(Point2D p, Node n, boolean vertical, Point2D nearest) {
        // helper function: find the nearest point in the subtree and assign it to nearest
        if (n == null) return nearest;
        Point2D target = n.p;
        if (target.distanceSquaredTo(p) < nearest.distanceSquaredTo(p)) nearest = target;
        
        boolean goesLB = lb(p, n, vertical); // goes left/bottom
        Node first, next;
        if (goesLB) { // check the left/bottom subtree first if p is on the left/bottom side
            first = n.lb; next = n.rt;
        } else {
            first = n.rt; next = n.lb;
        }
        if (first != null && first.rect.distanceSquaredTo(p) < nearest.distanceSquaredTo(p)) {
            nearest = nearest(p, first, !vertical, nearest);
        }
        if (next  != null && next.rect.distanceSquaredTo(p) < nearest.distanceSquaredTo(p)) {
            nearest = nearest(p, next,  !vertical, nearest);
        }
        return nearest;
    }
}
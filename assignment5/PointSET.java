import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

public class PointSET {
    
    private final SET<Point2D> points;
    
    public PointSET() {
        // construct an empty set of points 
        points = new SET<Point2D>();
    }
    public boolean isEmpty() {
        // is the set empty? 
        return points.isEmpty();
    }
    public int size() {
        // number of points in the set 
        return points.size();
    }
    public void insert(Point2D p) {
        // add the point to the set (if it is not already in the set)
        if (p == null) throw new IllegalArgumentException("null input");
        
        points.add(p);
    }
    public boolean contains(Point2D p) {
        // does the set contain point p? 
        if (p == null) throw new IllegalArgumentException("null input");
        
        return points.contains(p);
    }
    public void draw() {
        // draw all points to standard draw 
        for (Point2D p : points) StdDraw.point(p.x(), p.y());
    }
    public Iterable<Point2D> range(RectHV rect) {
        // all points that are inside the rectangle (or on the boundary)
        if (rect == null) throw new IllegalArgumentException("null input");
        
        Bag<Point2D> pointsInRange = new Bag<Point2D>();
        for (Point2D p : points) if (rect.contains(p)) pointsInRange.add(p);
        return pointsInRange;
    }
    public Point2D nearest(Point2D p) {
        // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null) throw new IllegalArgumentException("null input");
        
        Point2D nearest = null;
        double minDistSq = Double.POSITIVE_INFINITY;
        for (Point2D q : points)
            if (p.distanceSquaredTo(q) < minDistSq) {
                nearest = q;
                minDistSq = p.distanceSquaredTo(q);
            }
        return nearest;
        
    } 

}
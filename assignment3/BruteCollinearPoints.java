import java.util.Arrays;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
    private LineSegment[] segmentArray;
    private int numberOfSegments;

    public BruteCollinearPoints(Point[] points) {
        // finds all line segments containing 4 points
        
        if (points == null)
                throw new IllegalArgumentException("null input array");
        
        points = points.clone(); // later operations will be on a defensive copy of points
        
        int numOfPoints = points.length;
        for (int i = 0; i < numOfPoints; i++)
            if (points[i] == null) 
                throw new IllegalArgumentException("null input array");
        
        Arrays.sort(points);
        for (int i = 0; i <numOfPoints-1; i++) 
            if (points[i].compareTo(points[i+1]) == 0)
                throw new IllegalArgumentException("repeated points");
        
        
        Bag<LineSegment> segmentBag = new Bag<LineSegment>();
        for (int i = 0; i < numOfPoints; i++) {
            for (int j = i+1; j < numOfPoints; j++) {
                double slopeIJ = points[i].slopeTo(points[j]);
                for (int k = j+1; k < numOfPoints; k++) {
                    double slopeIK = points[i].slopeTo(points[k]);
                    for (int m = k+1; m < numOfPoints; m++) {
                        double slopeIM = points[i].slopeTo(points[m]);
                        if (slopeIM == slopeIK && slopeIK == slopeIJ) {
                             segmentBag.add(new LineSegment(points[i], points[m]));
                        }
                    }
                }
            }
        }
        
        numberOfSegments = segmentBag.size();
        segmentArray = new LineSegment[numberOfSegments];
        int i = 0;
        for (LineSegment ls : segmentBag) segmentArray[i++] = ls;
    }
    
    public int numberOfSegments() {
        // the number of line segments
        return numberOfSegments;
    }
    
    public LineSegment[] segments() {
        // the line segments
        return segmentArray.clone();
    }
    
    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}

import java.util.Arrays;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
    
    private LineSegment[] segmentArray;
    private int numberOfSegments;

    public FastCollinearPoints(Point[] points) {
        // finds all line segments containing 4 or more points
        
        if (points == null)
                throw new IllegalArgumentException("null input array");
        
        Point[] pointsOrigin = points;
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
        for (Point p : pointsOrigin) { // find all LineSegemnts with Point p be its smallest(natural order) Point
            Arrays.sort(points);                 // sort according to Point's natural order, stable sorting
            Arrays.sort(points, p.slopeOrder()); // sort according to slope to Point p, p would be points[0]
            
            int end = 1; // points[end] be the largest Point in this LineSegment
            while (end < numOfPoints) {
                double slope = points[end].slopeTo(p);
                if (points[end].compareTo(p) < 0) { 
                    // if the first of the consecutive Points with same slope to Point p is smaller than p, 
                    // then p wouldn't be the smallest in this LineSegment, skip these Points
                    while (end < numOfPoints && points[end].slopeTo(p) == slope) end++;
                    continue;
                } else {
                    // if the first of the consecutive Points with same slope to Point p is larger than p, 
                    // so should the rest of the Points be, add this LineSegment if it is long enough
                    int numWithSameSlope = 1; // number of Points with same slope to Point p
                    while (end+1 < numOfPoints && points[end+1].slopeTo(p) == slope) {
                        // if the next Point has the same slope, then include it in this LineSegment
                        numWithSameSlope++;
                        end++;
                    } 
                    if (numWithSameSlope >= 3) {
                        segmentBag.add(new LineSegment(p, points[end]));
                    }
                    end++; // the next block of consecutive Points with the same slope to Point p
                }
            }
        }
        // BUG try only add those starts with p
        
        // add segments in segmentBag to segmentArray
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}

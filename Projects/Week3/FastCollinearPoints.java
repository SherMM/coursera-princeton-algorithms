import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
LineSegment[] segments;
ArrayList<LineSegment> lines;
public FastCollinearPoints(Point[] points) {
        lines = new ArrayList<LineSegment>();
        Point[] pts = Arrays.copyOf(points, points.length);
        for (int i = 0; i < points.length; i++) {
                Point p = points[i];
                Arrays.sort(pts, p.slopeOrder());
                // in slope order can ignore first item (p and itself, so slope is 0)
                for (int j = 1; j < pts.length; j++) {
                  StdOut.println("Slope " + p.toString() + "->" + pts[j].toString());
                  StdOut.println(p.slopeTo(pts[j]));
                }
                StdOut.println();
                StdOut.println();
                StdOut.println();
        }
}

public int numberOfSegments() {
        return 1;
}

//public LineSegment[] segments() {
//
//}

public static void main(String[] args) {
        StdOut.println("Compiled!");
        // read the N points from a file
        In in = new In(args[0]);
        int N = in.readInt();
        Point[] points = new Point[N];
        for (int i = 0; i < N; i++) {
                int x = in.readInt();
                int y = in.readInt();
                points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.show(0);
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
                p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
}
}

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Arrays;


public class BruteCollinearPoints {
ArrayList<LineSegment> lines;
HashSet<String> strSegs;
LineSegment[] segments;
int count;
public BruteCollinearPoints(Point[] points) {
        count = 0;
        Arrays.sort(points);
        lines = new ArrayList<LineSegment>();
        strSegs = new HashSet<String>();
        for (int i = 0; i < points.length; i++) {
                Point p = points[i];
                for (int j = i+1; j < points.length; j++) {
                        Point q = points[j];
                        double pq = p.slopeTo(q);
                        for (int k = j+1; k < points.length; k++) {
                                Point r = points[k];
                                double pr = p.slopeTo(r);
                                for (int l = k+1; l < points.length; l++) {
                                        Point s = points[l];
                                        double ps = p.slopeTo(s);
                                        if (pq == pr && pq == ps && pr == ps) {
                                                StdOut.println(p.toString() + q.toString() + r.toString() + s.toString());
                                                LineSegment line = new LineSegment(p, s);
                                                String seg = line.toString();
                                                if (!strSegs.contains(seg)) {
                                                        strSegs.add(seg);
                                                        lines.add(line);
                                                        count++;
                                                }
                                        }
                                }
                        }
                }
        }

        segments = new LineSegment[count];
        for (int i = 0; i < count; i++) {
                segments[i] = lines.get(i);
        }


}

public int numberOfSegments() {
        return count;
}

public LineSegment[] segments() {
        return segments;
}

public static void main(String[] args) {
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

        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
                StdOut.println(segment);
                segment.draw();
        }
}
}

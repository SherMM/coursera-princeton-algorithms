import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Comparator;


public class BruteCollinearPoints {
ArrayList<LineSegment> lines;
HashMap<String,LineSegment> strSegs;
LineSegment[] segments;
int count;
public BruteCollinearPoints(Point[] points) {
        count = 0;
        lines = new ArrayList<LineSegment>();
        strSegs = new HashMap<String,LineSegment>();
        for (int i = 0; i < points.length; i++) {
                Point p = points[i];
                for (int j = 0; j < points.length; j++) {
                        Point r = points[j];
                        for (int k = 0; k < points.length; k++) {
                                Point q = points[k];
                                for (int l = 0; l < points.length; l++) {
                                        Point s = points[l];
                                        if (checkIndexes(i, j, k, l)) {
                                                double pq = p.slopeTo(q);
                                                double pr = p.slopeTo(r);
                                                double ps = p.slopeTo(s);
                                                if (pq == pr && pq == ps && pr == ps && inYOrder(p, q, r, s)) {
                                                        StdOut.println(p.toString() + q.toString() + r.toString() + s.toString());
                                                        LineSegment line = new LineSegment(p, s);
                                                        String seg = line.toString();
                                                        if (!strSegs.containsKey(seg)) {
                                                                strSegs.put(seg, line);
                                                                lines.add(line);
                                                                count++;
                                                        }
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

private boolean checkIndexes(int i, int j, int k, int l) {
        return i != j && i != k && i != l && j != k && j != l && k != l;
}

private boolean inYOrder(Point p, Point q, Point r, Point s) {
  return r.compareTo(s) < 0 && q.compareTo(r) < 0 && p.compareTo(q) < 0;
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

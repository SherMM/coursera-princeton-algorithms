import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import java.util.HashMap;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;


public class BruteCollinearPoints {
private HashMap<String, LineSegment> segs;
private List<LineSegment> lines;
private int count = 0;

public BruteCollinearPoints(Point[] points) {
        if (points == null || hasNull(points)) {
                throw new NullPointerException("Null detected in points array");
        }

        Arrays.sort(points);

        if (hasDuplicatePoint(points)) {
                throw new IllegalArgumentException("Array contains identical point");
        }

        segs = new HashMap<String, LineSegment>();
        lines = new ArrayList<LineSegment>();
        for (int i = 0; i < points.length; i++) {
                Point p = points[i];
                for (int j = i+1; j < points.length; j++) {
                        Point q = points[j];
                        double pq = p.slopeTo(q);
                        for (int k = j+1; k < points.length; k++) {
                                Point r = points[k];
                                double pr = p.slopeTo(r);
                                if (pq == pr) {
                                        for (int l = k+1; l < points.length; l++) {
                                                Point s = points[l];
                                                double ps = p.slopeTo(s);
                                                if (pq == ps) {
                                                        LineSegment line = new LineSegment(p, s);
                                                        String seg = line.toString();
                                                        if (!segs.containsKey(seg)) {
                                                                segs.put(seg, line);
                                                                lines.add(line);
                                                                count++;
                                                        }
                                                }
                                        }
                                }

                        }
                }
        }
}

public int numberOfSegments() {
        return count;
}

public LineSegment[] segments() {
  LineSegment[] segments = new LineSegment[count];
  for (int i = 0; i < count; i++) {
          segments[i] = lines.get(i);
  }
  return segments;
}

private static boolean hasNull(Point[] points) {
        for (int i = 0; i < points.length; i++) {
                if (points[i] == null) {
                        return true;
                }
        }
        return false;
}

private static boolean hasDuplicatePoint(Point[] points) {
        Point p = points[0];
        for (int i = 1; i < points.length; i++) {
                if (points[i].compareTo(p) == 0) {
                        return true;
                }
                p = points[i];
        }
        return false;
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

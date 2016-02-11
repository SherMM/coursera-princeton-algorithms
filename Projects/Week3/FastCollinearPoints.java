import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import java.util.HashMap;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class FastCollinearPoints {
private HashMap<String, LineSegment> lines;
private List<LineSegment> segs;
private int count = 0;

public FastCollinearPoints(Point[] points) {
        lines = new HashMap<String, LineSegment>();
        segs = new ArrayList<LineSegment>();
        Point[] pts = Arrays.copyOf(points, points.length);
        for (int i = 0; i < points.length; i++) {
                Point p = points[i]; // origin point
                Arrays.sort(pts, p.slopeOrder());
                double slope = p.slopeTo(p);
                List<Point> segment = new ArrayList<Point>();

                // in slope order can ignore first item (p and itself, so slope is 0)
                for (int j = 1; j < pts.length; j++) {
                        double currSlope = p.slopeTo(pts[j]);
                        if (currSlope != slope) {
                                // if slope changed and we have at least three pts of similar slope to origin
                                if (segment.size() >= 3) {
                                        break;
                                }
                                segment.clear();
                                segment.add(pts[j]);
                                slope = currSlope;
                        } else {
                                segment.add(pts[j]);
                        }
                }

                // segment found has more at least 4 points
                if (segment.size() >= 3) {
                        segment.add(p); // add origin point back
                        Point maxPoint = Collections.max(segment);
                        Point minPoint = Collections.min(segment);
                        LineSegment line = new LineSegment(minPoint, maxPoint);
                        String seg = line.toString();
                        if (!lines.containsKey(seg)) {
                                lines.put(seg, line);
                                segs.add(line);
                                count++;
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
          segments[i] = segs.get(i);
  }
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

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
                StdOut.println(segment);
                segment.draw();
        }
}
}

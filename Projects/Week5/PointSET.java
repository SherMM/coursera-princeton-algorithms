import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;

public class PointSET {

private SET<Point2D> points;
private int count;


public PointSET() {
        points = new SET<Point2D>();
        count = 0;
}

public boolean isEmpty() {
        return points.isEmpty();
}

public int size() {
        return points.size();
}

public void insert(Point2D p) {
        if (p == null) throw new NullPointerException("must insert a point");
        points.add(p);
}

public boolean contains(Point2D p) {
        if (p == null) throw new NullPointerException("must search for a point");
        return points.contains(p);
}

public void draw() {
        // draw the points
        for (Point2D p : points) {
                StdOut.println(p);
                p.draw();
        }
        StdDraw.show();
}

public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new NullPointerException("input rectangle");
        Stack<Point2D> stack = new Stack<Point2D>();

        for (Point2D p : points) {
                if (rect.contains(p)) {
                        stack.push(p);
                }
        }
        return stack;
}

public Point2D nearest(Point2D p) {
        if (p == null) throw new NullPointerException("must search for a point");
        Point2D closest = null;
        double distance = Double.POSITIVE_INFINITY;
        for (Point2D nbr : points) {
                double dist = p.distanceSquaredTo(nbr);
                if (dist < distance && p.compareTo(nbr) != 0) {
                        closest = nbr;
                        distance = dist;
                }
        }
        return closest;
}


public static void main(String[] args) {
        // read the N points from a file
        In in = new In(args[0]);
        PointSET pts = new PointSET();
        while (!in.isEmpty()) {
                double x = in.readDouble();
                double y = in.readDouble();
                Point2D p = new Point2D(x, y);
                pts.insert(p);
        }

        Point2D testPT = new Point2D(0.5, 0.04);
        StdOut.println("Nearest: ");
        StdOut.println(pts.nearest(testPT));

        // draw the points
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
        pts.draw();

        StdDraw.setPenColor(StdDraw.BLUE);
        testPT.draw();
}
}

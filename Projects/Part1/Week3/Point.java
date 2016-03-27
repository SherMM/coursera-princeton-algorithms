/******************************************************************************
*  Compilation:  javac Point.java
*  Execution:    java Point
*  Dependencies: none
*
*  An immutable data type for points in the plane.
*  For use on Coursera, Algorithms Part I programming assignment.
*
******************************************************************************/

import java.util.Arrays;
import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

public class Point implements Comparable<Point> {

private final int x;         // x-coordinate of this point
private final int y;         // y-coordinate of this point

/**
 * Initializes a new point.
 *
 * @param  x the <em>x</em>-coordinate of the point
 * @param  y the <em>y</em>-coordinate of the point
 */
public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
}

/**
 * Draws this point to standard draw.
 */
public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
}

/**
 * Draws the line segment between this point and the specified point
 * to standard draw.
 *
 * @param that the other point
 */
public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
}

/**
 * Returns the slope between this point and the specified point.
 * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
 * is (y1 - y0) / (x1 - x0). For completness, the slope is defined to be
 * +0.0 if the line segment connecting the two points is horizontal;
 * Double.POSITIVE_INFINITY if the line segment is vertcal;
 * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
 *
 * @param  that the other point
 * @return the slope between this point and the specified point
 */
public double slopeTo(Point that) {
        if (this.y == that.y && this.x == that.x) {
                return Double.NEGATIVE_INFINITY;
        }
        if (this.x == that.x) {
                return Double.POSITIVE_INFINITY;
        }
        if (this.y == that.y) {
                return 0.0;
        }
        return (double) (that.y - this.y) / (that.x - this.x);
}

/**
 * Compares two points by y-coordinate, breaking ties by x-coordinate.
 * Formally, the invoking point (x0, y0) is less than the argument point
 * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
 *
 * @param  that the other point
 * @return the value <tt>0</tt> if this point is equal to the argument
 *         point (x0 = x1 and y0 = y1);
 *         a negative integer if this point is less than the argument
 *         point; and a positive integer if this point is greater than the
 *         argument point
 */
public int compareTo(Point that) {
        if (this.y == that.y && this.x == that.x) {
                return 0;
        }
        if (this.y == that.y) {
                return this.x - that.x;
        }
        return this.y - that.y;
}

/**
 * Compares two points by the slope they make with this point.
 * The slope is defined as in the slopeTo() method.
 *
 * @return the Comparator that defines this ordering on points
 */
public Comparator<Point> slopeOrder() {
        return new SlopeOrder();
}

private class SlopeOrder implements Comparator<Point> {
public int compare(Point p, Point q) {
        double slope1 = slopeTo(p);
        double slope2 = slopeTo(q);
        if (Double.compare(slope1, slope2) < 0) {
                return -1;
        } else if (Double.compare(slope1, slope2) > 0) {
                return +1;
        } else {
                return 0;
        }
}
}

/**
 * Returns a string representation of this point.
 * This method is provide for debugging;
 * your program should not rely on the format of the string representation.
 *
 * @return a string representation of this point
 */
public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
}

/**
 * Unit tests the Point data type.
 */
public static void main(String[] args) {

        int x0 = 0;
        int y0 = 0;
        int N = 100;
        StdDraw.setCanvasSize(800, 800);
        StdDraw.setXscale(0, 100);
        StdDraw.setYscale(0, 100);
        StdDraw.setPenRadius(0.007);

        Point[] points = new Point[N];
        for (int i = 0; i < N; i++) {
                int x = StdRandom.uniform(100);
                int y = StdRandom.uniform(100);
                points[i] = new Point(x, y);
                points[i].draw();
        }

        // draw p = (x0, x1) in red
        Point p = new Point(x0, y0);
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setPenRadius(.02);
        p.draw();

        StdDraw.setPenRadius();
        StdDraw.setPenColor(StdDraw.BLUE);
        Arrays.sort(points, p.slopeOrder());
        for (int i = 0; i < N; i++) {
                p.drawTo(points[i]);
                StdDraw.show(100);
        }
}
}
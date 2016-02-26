import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;

public class KdTree {
private Node root;
private int nodes;

private static class Node {
private Point2D point;
private RectHV rect;
private Node lb;
private Node rt;

public Node(Point2D p) {
        point = p;
        rect = null;
        lb = null;
        rt = null;
}
}

public KdTree() {
        nodes = 0;
        root = null;
}

public boolean isEmpty() {
        return nodes == 0;
}

public int size() {
        return nodes;
}

public void insert(Point2D p) {
        root = insert(root, p);
        nodes++;
}

private Node insert(Node x, Point2D p) {
        if (x == null) {
          return new Node(p);
        }
        int cmp = p.compareTo(x.point);
        if (cmp < 0) x.lb = insert(x.lb, p);
        else if (cmp > 0) x.rt = insert(x.rt, p);
        else x.point = p;
        return x;
}

/*
public contains(Point2D p) {

}
*/

public void draw() {

}

/*
public Iterable<Point2D> range(RectHV rect) {

}

public Point2D nearest(Point2D p) {

}
*/

public static void main(String[] args) {
      KdTree kd = new KdTree();
      Point2D p1 = new Point2D(0.7, 0.2);
      Point2D p2 = new Point2D(0.5, 0.4);
      kd.insert(p1);
      kd.insert(p2);
      StdOut.println(kd.size());
}
}

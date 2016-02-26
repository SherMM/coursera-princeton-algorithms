import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;

public class KdTree {
private Node root; // tree root
private boolean orientation; // boolean indicating orientation of root node
private int nodes; // number of nodes in tree

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
        root = insert(root, null, p, true);
}

private Node insert(Node x, Node prev, Point2D p, boolean orient) {
        if (x == null) {
                if (prev == null) {
                        node.rect = new RectHV(0.0, 0.0, 1.0, 1.0);
                }
                Node node = new Node(p);
                nodes++;
                return node;
        }
        // check orientation level to know what to compare against
        int cmp;
        if (orient) {
                cmp = Double.compare(p.x(), x.point.x()); // vert lines compare x-coord
        } else {
                cmp = Double.compare(p.y(), x.point.y()); // horiz lines compare y-coord
        }

        // insert into correct subtree
        if (cmp < 0) x.lb = insert(x.lb, x, p, !orient);
        else if (cmp > 0) x.rt = insert(x.rt, x, p, !orient);
        else x.point = p;
        return x;
}

public boolean contains(Point2D p) {
        return get(p) != null;
}

private Point2D get(Point2D p) {
        return get(root, p, true);
}

private Point2D get(Node x, Point2D p, boolean orient) {
        if (x == null) return null;

        int cmp;
        if (orient) {
                cmp = Double.compare(p.x(), x.point.x());
        } else {
                cmp = Double.compare(p.y(), x.point.y());
        }

        if (cmp < 0) return get(x.lb, p, !orient);
        else if (cmp > 0) return get(x.rt, p, !orient);
        else {
                StdOut.println(x.rect);
                return x.point;
        }
}

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
        Point2D p3 = new Point2D(0.2, 0.3);
        Point2D p4 = new Point2D(0.7, 0.2);
        kd.insert(p1);
        kd.insert(p2);
        kd.insert(p3);
        kd.insert(p4);
        StdOut.println(kd.size());
        StdOut.println(kd.contains(p1));
        StdOut.println(kd.contains(p2));
        StdOut.println(kd.contains(p3));
}
}

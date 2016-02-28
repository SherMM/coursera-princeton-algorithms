import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.Queue;

public class KdTree {
private Node root; // tree root
private boolean orientation; // boolean indicating orientation of root node
private int nodes; // number of nodes in tree

private static class Node {
private Point2D point;
private RectHV rect;
private Node lb;
private Node rt;
private boolean vert;

public Node(Point2D p) {
        point = p;
        rect = null;
        lb = null;
        rt = null;
        vert = true;
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
        root = insert(root, null, p, true, false);
}

private Node insert(Node x, Node prev, Point2D p, boolean orient, boolean lower) {
        if (x == null) {
                Node node = new Node(p);
                if (size() == 0) {
                        node.rect = new RectHV(0.0, 0.0, 1.0, 1.0);
                } else {
                        double xmin, ymin, xmax, ymax;
                        if (lower) {
                                xmin = prev.rect.xmin();
                                ymin = prev.rect.ymin();
                                if (orient) {
                                        xmax = prev.rect.xmax();
                                        ymax = prev.point.y();
                                } else {
                                        node.vert = false;
                                        xmax = prev.point.x();
                                        ymax = prev.rect.ymax();
                                }
                        } else {
                                xmax = prev.rect.xmax();
                                ymax = prev.rect.ymax();
                                if (orient) {
                                        xmin = prev.rect.xmin();
                                        ymin = prev.point.y();
                                } else {
                                        node.vert = false;
                                        xmin = prev.point.x();
                                        ymin = prev.rect.ymin();
                                }
                        }
                        node.rect = new RectHV(xmin, ymin, xmax, ymax);
                }
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
        if (cmp < 0) x.lb = insert(x.lb, x, p, !orient, true);
        else if (cmp > 0 || (cmp == 0 && orient)) x.rt = insert(x.rt, x, p, !orient, false);
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
        else return x.point;
}

public void draw() {
        Queue<Node> queue = new Queue<Node>();
        queue.enqueue(root);
        while (!queue.isEmpty()) {
                Node x = queue.dequeue();
                if (x == null) continue;
                double xmin = x.rect.xmin();
                double ymin = x.rect.ymin();
                double xmax = x.rect.xmax();
                double ymax = x.rect.ymax();
                double xcoord = x.point.x();
                double ycoord = x.point.y();
                if (x.vert) {
                    StdDraw.setPenRadius(.005);
                    StdDraw.setPenColor(StdDraw.RED);
                    StdDraw.line(xcoord, ymin, xcoord, ymax);
                } else {
                    StdDraw.setPenRadius(.005);
                    StdDraw.setPenColor(StdDraw.BLUE);
                    StdDraw.line(xmin, ycoord, xmax, ycoord);
                }
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.setPenRadius(.01);
                x.point.draw();
                queue.enqueue(x.lb);
                queue.enqueue(x.rt);
        }
}

/*
   public Iterable<Point2D> range(RectHV rect) {

   }

   public Point2D nearest(Point2D p) {

   }
 */

public static void main(String[] args) {
        /*
           KdTree kd = new KdTree();
           Point2D p1 = new Point2D(0.7, 0.2);
           Point2D p2 = new Point2D(0.5, 0.4);
           Point2D p3 = new Point2D(0.2, 0.3);
           Point2D p4 = new Point2D(0.4, 0.7);
           Point2D p5 = new Point2D(0.9, 0.6);
           kd.insert(p1);
           kd.insert(p2);
           kd.insert(p3);
           kd.insert(p4);
           kd.insert(p5);
           StdOut.println(kd.size());
           StdOut.println(kd.contains(p1));
           StdOut.println(kd.contains(p2));
           StdOut.println(kd.contains(p3));
           StdOut.println(kd.contains(p4));
           StdOut.println(kd.contains(p5));
         */

        String filename = args[0];
        In in = new In(filename);

        //StdDraw.show(0);
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
                double x = in.readDouble();
                double y = in.readDouble();
                Point2D p = new Point2D(x, y);
                kdtree.insert(p);
        }

        //Point2D testPT = new Point2D(0.206107, 0.095492);
        //StdOut.println(kdtree.contains(testPT));

        // draw the points
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
        kdtree.draw();
}
}

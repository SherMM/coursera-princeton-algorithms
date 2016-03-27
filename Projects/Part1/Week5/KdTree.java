import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.SET;

public class KdTree {
private Node root; // tree root
private int nodes; // number of nodes in tree

/**
 * Class for node in kdtree
 */
private static class Node {
private Point2D point; // x,y point in this node
private RectHV rect; // axis aligned rectangle represented by this point
private Node lb; // left subtree
private Node rt; // right subtree
private boolean vert; // signals orientation of node (compare by x or y)

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
        if (p == null) throw new NullPointerException("must insert a point");
        // start at root, prev starts as null, orientation is vertical to start,
        // and side of subtree starts as upper side (right)
        root = insert(root, null, p, true, false);
}

private Node insert(Node x, Node prev, Point2D p, boolean orient, boolean lower) {
        if (x == null) {
                // reached leaf or root is null
                Node node = new Node(p);
                if (size() == 0) {
                        // root represents entire unit square
                        node.rect = new RectHV(0.0, 0.0, 1.0, 1.0);
                } else {
                        // need to calculate rectangle dimensions
                        double xmin, ymin, xmax, ymax;
                        if (lower) {
                                // right subtree
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
                                // left subtree
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
        if (x.point.compareTo(p) == 0) x.point = p; // points are equal
        else if (cmp < 0) x.lb = insert(x.lb, x, p, !orient, true);
        else if (cmp > 0 || cmp == 0) x.rt = insert(x.rt, x, p, !orient, false);
        return x;
}

public boolean contains(Point2D p) {
        if (p == null) throw new NullPointerException("must search for a point");
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
        // one of the coordinates is equal, but points are not
        else if (cmp > 0 || (cmp == 0 && x.point.compareTo(p) != 0)) return get(x.rt, p, !orient);
        // points are equal
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
                        // red lines for vertical splits
                        StdDraw.setPenRadius(.0005);
                        StdDraw.setPenColor(StdDraw.RED);
                        StdDraw.line(xcoord, ymin, xcoord, ymax);
                } else {
                        // blue lines for horizontal splits
                        StdDraw.setPenRadius(.0005);
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

public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new NullPointerException("input rectangle");
        Stack<Point2D> stack = new Stack<Point2D>();
        Queue<Node> queue = new Queue<Node>();
        queue.enqueue(root);
        while (!queue.isEmpty()) {
                Node x = queue.dequeue();
                if (x == null) continue;
                RectHV curr = x.rect;
                if (rect.intersects(curr)) {
                        queue.enqueue(x.lb);
                        queue.enqueue(x.rt);
                        if (rect.contains(x.point)) {
                                stack.push(x.point);
                        }
                }
        }
        return stack;
}

public Point2D nearest(Point2D p) {
        if (p == null) throw new NullPointerException("must search for a point");
        Queue<Node> queue = new Queue<Node>();
        Point2D closest = null;
        double distance = Double.POSITIVE_INFINITY;
        queue.enqueue(root);
        while (!queue.isEmpty()) {
                Node x = queue.dequeue();
                if (x == null) continue;
                RectHV curr = x.rect;
                double dist = curr.distanceSquaredTo(p);
                if (dist <= distance) {
                        double d = p.distanceSquaredTo(x.point);
                        if (d < distance) {
                                distance = d;
                                closest = x.point;
                        }
                        queue.enqueue(x.lb);
                        queue.enqueue(x.rt);
                }
        }
        return closest;
}


public static void main(String[] args) {

        String filename = args[0];
        In in = new In(filename);

        SET<Point2D> set = new SET<Point2D>();

        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
                double x = in.readDouble();
                double y = in.readDouble();
                Point2D p = new Point2D(x, y);
                kdtree.insert(p);
                set.add(p);
        }

        // draw the points
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
        kdtree.draw();

        Point2D queryPT = new Point2D(0.6, 0.095492);
        StdDraw.setPenColor(StdDraw.RED);
        kdtree.nearest(queryPT).draw();

        StdDraw.setPenColor(StdDraw.BLUE);
        queryPT.draw();


}
}

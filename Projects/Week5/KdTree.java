import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;

public class KdTree {
  private Node root;

  public static class Node {
    private Point2D p;
    private RectHV rect;
    private Node lb;
    private Node rt;
  }
}

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;

public class SAP {
private static final int INFINITY = Integer.MAX_VALUE;
private Digraph sap;

// constructor takes a digraph (not necessarily a DAG)
public SAP(Digraph G) {
        if (G == null) throw new NullPointerException("Graph is null");
        sap = new Digraph(G);
}

// length of shortest ancestral path between v and w; -1 if no such path
public int length(int v, int w) {
        // handle input exceptions
        int limit = sap.V()-1; // highest possible vertex
        if (!isValidVertex(v, limit) || !isValidVertex(w, limit)) throw new IndexOutOfBoundsException("Vertex not in Graph");
        int a = this.ancestor(v, w);
        if (a == -1) {
                return -1;
        }
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(sap, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(sap, w);
        return bfsv.distTo(a) + bfsw.distTo(a);
}

// a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
public int ancestor(int v, int w) {
        // handle input exceptions
        int limit = sap.V()-1;
        if (!isValidVertex(v, limit) || !isValidVertex(w, limit)) throw new IndexOutOfBoundsException("Vertex not in Graph");
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(sap, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(sap, w);
        int a = -1; // ancestor
        int dist = INFINITY; // shortest distance from ancestor to node v or w
        for (int vert = 0; vert < sap.V(); vert++) {
                if (bfsv.hasPathTo(vert) && bfsw.hasPathTo(vert)) {
                        int dv = bfsv.distTo(vert);
                        int dw = bfsw.distTo(vert);
                        int minDist = dv + dw;
                        if (minDist < dist) {
                                a = vert;
                                dist = minDist;
                        }
                }
        }
        return a;
}

// length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
public int length(Iterable<Integer> v, Iterable<Integer> w) {
        // handle input exceptions
        if (v == null || w == null) throw new NullPointerException("Iterables are null");

        int limit = sap.V()-1;
        for (int vertex : v) {
                if (!isValidVertex(vertex, limit)) {
                        throw new IndexOutOfBoundsException("Vertex not in Graph");
                }
        }

        for (int vertex : w) {
                if (!isValidVertex(vertex, limit)) {
                        throw new IndexOutOfBoundsException("Vertex not in Graph");
                }
        }

        int a = this.ancestor(v, w);
        if (a == -1) {
                return -1;
        }
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(sap, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(sap, w);
        return bfsv.distTo(a) + bfsw.distTo(a);
}

// a common ancestor that participates in shortest ancestral path; -1 if no such path
public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        // handle input exceptions
        if (v == null || w == null) throw new NullPointerException("Iterables are null");

        int limit = sap.V()-1;
        for (int vertex : v) {
                if (!isValidVertex(vertex, limit)) {
                        throw new IndexOutOfBoundsException("Vertex not in Graph");
                }
        }

        for (int vertex : w) {
                if (!isValidVertex(vertex, limit)) {
                        throw new IndexOutOfBoundsException("Vertex not in Graph");
                }
        }
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(sap, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(sap, w);
        int a = -1; // ancestor
        int dist = INFINITY; // shortest distance from ancestor to node v or w
        for (int vert = 0; vert < sap.V(); vert++) {
                if (bfsv.hasPathTo(vert) && bfsw.hasPathTo(vert)) {
                        int dv = bfsv.distTo(vert);
                        int dw = bfsw.distTo(vert);
                        int minDist = dv + dw;
                        if (minDist < dist) {
                                a = vert;
                                dist = minDist;
                        }
                }
        }
        return a;
}

private static boolean isValidVertex(int v, int high) {
        return v >= 0 && v <= high;
}

// do unit testing of this class
public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);

        while (!StdIn.isEmpty()) {
                int v = StdIn.readInt();
                int w = StdIn.readInt();
                int length   = sap.length(v, w);
                int ancestor = sap.ancestor(v, w);
                StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
}
}

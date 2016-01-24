import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {

private WeightedQuickUnionUF comp;
private int[][] grid;
private int n;
private int top;
private int bottom;

public Percolation(int N) {
        if (N <= 0) {
                throw new IllegalArgumentException("N must be greater than 0");
        }

        n = N;
        top = 0;
        bottom = n+1;
        grid = new int[N+1][N+1];
        comp = new WeightedQuickUnionUF((n+1)*(n+1));
        // initialize all grid sites as blocked
        for (int i = 1; i <= N; i++) {
                for (int j = 1; j <= N; j++) {
                        grid[i][j] = 0;
                        if (i == 1) {
                                // connect to virtual top
                                comp.union(top, this.xyTo1D(i, j));
                        }
                        if (i == n) {
                                // connect to virtual bottom
                                comp.union(bottom, this.xyTo1D(i, j));
                        }
                }
        }
}

public void open(int i, int j) {
  checkIndexes(i, j);
}

public boolean isOpen(int i, int j) {
    checkIndexes(i, j);
        return this.get(i, j) == 1;
}

public boolean isFull(int i, int j) {
    checkIndexes(i , j);
        return comp.connected(top, this.xyTo1D(i, j));
}

public boolean percolates() {
        return comp.connected(top, bottom);
}

private int size() {
        return this.n;
}

private int xyTo1D(int x, int y) {
        return x*(this.n+1) + y;
}

private int get(int i, int j) {
        checkIndexes(i, j);
        return grid[i][j];
}

// for testing
private void set(int i, int j, int x) {
        checkIndexes(i, j);
        grid[i][j] = x;
}

// for showing grid
private void showGrid() {
        for (int i = 1; i <= this.size(); i++) {
                for (int j = 1; j <= this.size(); j++) {
                        System.out.print(this.get(i, j));
                }
                System.out.println();
        }
}

private void checkIndexes(int i, int j) {
        if (i <= 0 || i > n) {
                throw new IndexOutOfBoundsException("row index i out of bounds");
        }
        if (j <= 0 || j > n) {
                throw new IndexOutOfBoundsException("col index j out of bounds");
        }
}

public static void main(String[] args) {
        System.out.println("Compiled!!!");
        int N = 5;

        // test the constructor
        System.out.println("Constructor test");
        Percolation perc = new Percolation(N);
        perc.showGrid();
        System.out.println();

        // test isOpen method
        System.out.println("isOpen test");
        int gx = 1;
        int gy = 2;
        int el = 1;
        perc.set(gx, gy, el);
        boolean openCheck = perc.isOpen(gx, gy);
        System.out.println("Site " + gx + ", " + gy + " is open: " + openCheck);
        perc.showGrid();
        System.out.println();

        // test xyTo1D method
        System.out.println("xyTo1D test");
        Percolation test2D = new Percolation(5);
        int curr = 0;
        for (int i = 1; i <= 5; i++) {
                for (int j = 1; j <= 5; j++) {
                        test2D.set(i, j, curr);
                        curr++;
                }
        }

        test2D.showGrid();

        int[] test1D = new int[6*6];
        int coord;
        for (int i = 1; i <= 5; i++) {
                for (int j = 1; j <= 5; j++) {
                        coord = test2D.xyTo1D(i, j);
                        System.out.println(i + ", " + j + " = " + coord);
                        test1D[coord] = test2D.get(i, j);
                }
        }
        System.out.println();
}
}

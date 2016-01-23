import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {
private int[][] grid;
private int n;
private int top;
private int bottom;

public Percolation(int N) {
        if (N <= 0) {
                throw new IllegalArgumentException("N must be greater than 0");
        }
        n = N;
        grid = new int[N+1][N+1];
        for (int i = 1; i <= N; i++) {
                for (int j = 1; j <= N; j++) {
                        grid[i][j] = 0;
                }
        }
}

public void open(int i, int j) {

}

public boolean isOpen(int i, int j) {
        return this.get(i, j) == 1;
}

public boolean isFull(int i, int j) {
        return true;
}

public boolean percolates() {
        return true;
}

private int size() {
        return n;
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
        Percolation perc = new Percolation(N);
        perc.showGrid();

        // test isOpen method
        int gx = 1;
        int gy = 2;
        int el = 1;
        perc.set(gx, gy, el);
        boolean openCheck = perc.isOpen(gx, gy);
        System.out.println("Site " + gx + ", " + gy + " is open: " + openCheck);
        perc.showGrid();
}
}

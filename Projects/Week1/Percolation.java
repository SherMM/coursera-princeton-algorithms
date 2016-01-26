/******************************************************************************
*  Compilation:  javac Percolation.java
*  Execution:    java Percolation
*  Dependencies: WeightedQuickUnionUF.java
*
*  A mutable grid system representing a system we want to test for
*  percolation. A system percolation if any site on the top is connected
*  to any site on the bottom of the grid through adjacent grid squares.
*
*
******************************************************************************/

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Percolation class is a mutable grid structure
 * representing blocked and open sites. Contained a WQUF
 * structure to manage and track connections among grid squares,
 * specifically those of the virtual top and virtual bottom
 *
 * @author Ian Laurain
 *
 */
public class Percolation {

private WeightedQuickUnionUF comp;
private int[][] grid; // grid to store percolation state
private int n; // size of grid (row or column)
private int top; // virtual top
private int bottom; // virtual bottom

public Percolation(int N) {
        if (N <= 0) {
                throw new IllegalArgumentException("N must be greater than 0");
        }

        n = N;
        /*
         * Because indices needed for grid are 1..N, we allow an
         * extra column and row in the grid. The first row and column
         * will therefore be ignored. WQUF structure has as many spaces
         * as the 2D grid, plus an extra. 2D grid spaces will be translated
         * to corresponding location in WQUF. Some spaces in WQUF will not
         * be used, so top and bottom don't start at front and end of WQUF
         */
        top = n+1;
        bottom = (n+1)*(n+1);
        grid = new int[n+1][n+1];
        comp = new WeightedQuickUnionUF((n+1)*(n+1) + 1);
        // initialize all grid sites as blocked
        for (int i = 1; i <= N; i++) {
                for (int j = 1; j <= N; j++) {
                        grid[i][j] = 0;
                        if (i == 1) {
                                // connect 1st row to virtual top
                                comp.union(top, this.xyTo1D(i, j));
                        }
                        if (i == n) {
                                // connect last row to virtual bottom
                                comp.union(bottom, this.xyTo1D(i, j));
                        }
                }
        }
}

/**
 * Opens a site and connects to any adjacent, open sites
 */
public void open(int i, int j) {
        checkIndexes(i, j);
        this.set(i, j, 1); // open current grid space

        // open adjaction spaces above and below
        int idx = this.xyTo1D(i, j);
        if (i > 1) {
                if (i < this.size()) {
                        if (this.isOpen(i-1, j)) {
                                comp.union(idx, this.xyTo1D(i-1, j));
                        }
                        if (this.isOpen(i+1, j)) {
                                comp.union(idx, this.xyTo1D(i+1, j));
                        }
                } else if (i == this.size()) {
                        if (this.isOpen(i-1, j)) {
                                comp.union(idx, this.xyTo1D(i-1, j));
                        }
                }
        } else {
                if (i != this.size() && this.isOpen(i+1, j)) {
                        comp.union(idx, this.xyTo1D(i+1, j));
                }
        }

        // open adjacent spaces on the sides
        if (j > 1) {
                if (j < this.size()) {
                        if (this.isOpen(i, j-1)) {
                                comp.union(idx, this.xyTo1D(i, j-1));
                        }
                        if (this.isOpen(i, j+1)) {
                                comp.union(idx, this.xyTo1D(i, j+1));
                        }
                } else if (j == this.size()) {
                        if (this.isOpen(i, j-1)) {
                                comp.union(idx, this.xyTo1D(i, j-1));
                        }
                }
        } else {
                if (j != this.size() && this.isOpen(i, j+1)) {
                        comp.union(idx, this.xyTo1D(i, j+1));
                }
        }
}

/**
 * Returns true if current grid space is open,
 * and false otherwise
 *
 * @param i row index
 * @param j column index
 *
 * @return a boolean indicating open/blocked state
 */
public boolean isOpen(int i, int j) {
        checkIndexes(i, j);
        return this.get(i, j) == 1;
}

/**
 * Returns true if space open and connected to virtual top,
 * and false otherwise
 *
 * @param i row index
 * @param j column index
 *
 * @return a boolean indicating full state
 */
public boolean isFull(int i, int j) {
        checkIndexes(i, j);
        return this.isOpen(i, j) && comp.connected(top, this.xyTo1D(i, j));
}

/**
 * Returns true if system percolates, and false otherwise
 *
 * @return a boolean indicating percolation state
 */
public boolean percolates() {
        // check case where grid only has one space
        if (this.size() == 1 && !this.isOpen(1, 1)) {
                return false;
        }
        return comp.connected(top, bottom);
}

/**
 * Returns size of grid (row or colunn size - N)
 *
 * @return integer indicating grid size
 */
private int size() {
        return this.n;
}

/**
 * Returns integer that is 2D grid index position
 * convert to 1D index value
 *
 * @param x row index
 * @param y column index
 *
 * @return 1D index
 */
private int xyTo1D(int x, int y) {
        return x*(this.n+1) + y;
}

/**
 * Returns the integers at given index position
 *
 * @param i row index
 * @param j column index
 *
 * @return the value at position i,j
 */
private int get(int i, int j) {
        checkIndexes(i, j);
        return grid[i][j];
}

/**
 * Sets a position in the grid to a value
 *
 * @param i row index
 * @param j column index
 * @param x the value to set index space equal to
 */
private void set(int i, int j, int x) {
        checkIndexes(i, j);
        grid[i][j] = x;
}

/**
 * Shows the current grid state so we can see
 * which sites are open or blocked
 */
private void showGrid() {
        for (int i = 1; i <= this.size(); i++) {
                for (int j = 1; j <= this.size(); j++) {
                        System.out.print(this.get(i, j));
                }
                System.out.println();
        }
}

/**
 * Checks whether given indices fall within valid range
 * Throws an exception if not
 */
private void checkIndexes(int i, int j) {
        if (i <= 0 || i > n) {
                throw new IndexOutOfBoundsException("row index i out of bounds");
        }
        if (j <= 0 || j > n) {
                throw new IndexOutOfBoundsException("col index j out of bounds");
        }
}

/**
 * Testing section
 */
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

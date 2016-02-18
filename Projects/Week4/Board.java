import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import java.util.Arrays;

public class Board {
private int[] board;
private int moves;
private int N;
private int size;
public Board(int[][] blocks) {
        moves = 0;
        N = blocks.length;
        size = N*N;
        board = new int[size];
        int idx = 0; // index in board
        for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                        board[idx] = blocks[i][j];
                        idx++;
                }
        }
}

public int dimension() {
        return N;
}

public int hamming() {
        int score = 0;
        for (int i = 0; i < size; i++) {
                int curr = board[i];
                if (curr != i+1 && curr != 0) {
                        score++;
                }
        }
        return score;
}

public int manhattan() {
        int score = 0;
        for (int i = 0; i < size; i++) {
                int curr = board[i];
                if (curr != i+1 && curr != 0) {
                        // need to convert 1d to 2d indexes
                        int x0 = getRowIndex(i, N); // examined 2d indexes
                        int y0 = getColIndex(i, N);
                        int x1 = getRowIndex(curr-1, N); // should be 2d indexes
                        int y1 = getColIndex(curr-1, N);
                        score += Math.abs(x1-x0) + Math.abs(y1-y0);
                }
        }
        return score;
}

private static int convert2Dto1D(int i, int j, int cols) {
        return i * cols + j; // cols is number of columns
}

private static int getRowIndex(int index, int cols) {
        return index % cols;
}

private static int getColIndex(int index, int cols) {
        return index / cols;
}

public boolean isGoal() {
        for (int i = 0; i < size-1; i++) {
                if (board[i] != i+1) {
                        return false;
                }
        }
        return true;
}

public Board twin() {
        // need new array
        int[] copy = Arrays.copyOf(board, board.length);

        // find index of empty block
        int empty = 0;
        for (int i = 0; i < size; i++) {
                if (board[i] == 0) {
                        empty = i;
                }
        }

        int i = empty;
        while (i == empty) {
                i = StdRandom.uniform(size);
        }

        int j = empty;
        while (j == empty) {
                j = StdRandom.uniform(size);
        }

        swap(i, j, copy);
        int[][] tiles = convert1Dto2DArray(copy, N);
        Board twinBoard = new Board(tiles);
        return twinBoard;
}

private static int[][] convert1Dto2DArray(int[] array, int dim) {
        int[][] arr = new int[dim][dim];
        for (int k = 0; k < dim*dim; k++) {
                int i = getRowIndex(k, dim);
                int j = getColIndex(k, dim);
                arr[i][j] = array[k];
        }
        return arr;
}

private static void swap(int i, int j, int[] array) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
}

public boolean equals(Object y) {
        return true;
}

/*
   public Iterable<Board> neighbors() {

   }
 */

public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                        int idx = convert2Dto1D(i, j, N);
                        s.append(String.format("%2d ", board[idx]));
                }
                s.append("\n");
        }
        return s.toString();
}

public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
                for (int j = 0; j < N; j++)
                        blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        StdOut.println("Hamming Score: " + initial.hamming());
        StdOut.println("Manhattan Score: " + initial.manhattan());
        StdOut.println("Is Goal Board: " + initial.isGoal());
        StdOut.println(initial);
        StdOut.println(initial.twin());
        /*
           // solve the puzzle
           Solver solver = new Solver(initial);

           // print solution to standard output
           if (!solver.isSolvable())
            StdOut.println("No solution possible");
           else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
           }
         */
}
}

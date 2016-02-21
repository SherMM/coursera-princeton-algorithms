import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import java.util.Comparator;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

private static class SearchNode implements Comparable<SearchNode> {
private Board board;
private SearchNode prev;
private int moves;

public SearchNode(Board b, SearchNode p) {
        board = b;
        prev = p;
        if (p != null) {
                moves = p.getNumMoves() + 1;
        } else {
                moves = 0;
        }
}

public int getNumMoves() {
        return moves;
}

public int getPriority() {
        return board.manhattan() + moves;
}

public Board getBoard() {
        return board;
}

public SearchNode getPrevNode() {
        return prev;
}

public int compareTo(SearchNode that) {
        if (this.getPriority() < that.getPriority()) {
                return -1;
        } else if (this.getPriority() > that.getPriority()) {
                return +1;
        } else {
                return 0;
        }
}
}

private MinPQ<SearchNode> pq; // PQ for original board
private MinPQ<SearchNode> tq; // PQ for twin board
private int moves; // min number of moves for solvable board
private Comparator<SearchNode> c;
private SearchNode solution; // solution from original board config
private SearchNode tsol; // solution from twin board config
private boolean solvable;

public Solver(Board initial) {
        if (initial == null) {
                throw new NullPointerException("Must Pass Board to Constructor");
        }
        c = new Comparator<SearchNode>() {
                public int compare(SearchNode s1, SearchNode s2) {
                        return s1.compareTo(s2);
                }
        };

        Board twin = initial.twin();

        moves = -1;
        solution = new SearchNode(initial, null);
        tsol = new SearchNode(twin, null);

        // PQ for original board
        pq = new MinPQ<SearchNode>(c);
        pq.insert(solution);

        // PQ for twin board
        tq = new MinPQ<SearchNode>(c);
        tq.insert(tsol);

        while (!pq.isEmpty() && !tq.isEmpty()) {
                SearchNode s = pq.delMin();
                Board searchBoard = s.getBoard();

                SearchNode t = tq.delMin();
                Board twinBoard = t.getBoard();

                if (twinBoard.isGoal()) {
                        solvable = false;
                        break;
                }

                if (searchBoard.isGoal()) {
                        solvable = true;
                        solution = s;
                        moves = s.getNumMoves();
                        break;
                }

                for (Board neighbor : searchBoard.neighbors()) {
                        SearchNode p = s.getPrevNode();
                        if (p == null || !p.getBoard().equals(neighbor)) {
                                SearchNode next = new SearchNode(neighbor, s);
                                pq.insert(next);
                        }
                }

                for (Board neighbor : twinBoard.neighbors()) {
                        SearchNode p = t.getPrevNode();
                        if (p == null || !p.getBoard().equals(neighbor)) {
                                SearchNode next = new SearchNode(neighbor, t);
                                tq.insert(next);
                        }
                }
        }
}

public boolean isSolvable() {
        return solvable;
}

public int moves() {
        return moves;
}

public Iterable<Board> solution() {
        if (!this.isSolvable()) {
                return null;
        }
        SearchNode sol = solution; // need copy to not mutate
        Stack<Board> stack = new Stack<Board>();
        while (sol != null) {
                stack.push(sol.getBoard());
                sol = sol.getPrevNode();
        }
        return stack;
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
}
}

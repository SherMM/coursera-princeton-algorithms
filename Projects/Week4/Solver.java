import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import java.util.Comparator;

public class Solver {

private MinPQ<Board> pq;
private int moves;
private Comparator<Board> c;

public Solver(Board initial) {
        moves = 0;
        c = new Comparator<Board>() {
                public int compare(Board b1, Board b2) {
                        int b1Score = b1.hamming() + moves;
                        int b2Score = b2.hamming() + moves;
                        if (b1Score < b2Score) {
                          return -1;
                        } else if (b1Score > b2Score) {
                          return +1;
                        } else {
                          return 0;
                        }
                }
        };
        pq = new MinPQ<Board>(c);
        moves = 0;
        pq.insert(initial);
}

public boolean isSolvable() {
        return false;
}

public int moves() {
        return moves;
}

public Iterable<Board> solution() {
        Stack<Board> stack = new Stack<Board>();
        return stack;
}

public static void main(String[] args) {

}
}

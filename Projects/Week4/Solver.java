import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {

private MinPQ<Board> pq;
private int moves;

public Solver(Board initial) {
        pq = new MinPQ<Board>();
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

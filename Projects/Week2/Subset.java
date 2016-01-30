/******************************************************************************
*  Compilation:  javac-algs4 Subset.java
*  Execution:    java-algs4 Subset k - the number of items to return from set
*  Dependencies: Iterator, NoSuchElementException, algs4.jar, algs4.StdIn
*
*  Implements a deque as doubly-linked list
*
******************************************************************************/

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Subset {
public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        // initialize RandomizedQueue to store elements
        RandomizedQueue<String> q = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
                String item = StdIn.readString();
                q.enqueue(item);
        }

        int counter = 0;
        while (counter < k) {
                if (!q.isEmpty()) {
                        StdOut.println(q.dequeue());
                }
                counter++;
        }
}
}

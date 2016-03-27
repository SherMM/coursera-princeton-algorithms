/******************************************************************************
*  Compilation:  javac-algs4 RandomizedQueue.java
*  Execution:    java-algs4 RandomizedQueue
*  Dependencies: Iterator, NoSuchElementException, algs4.jar, algs4.StdOut,
*                algs4.StdRandom
*
* Implements a deque as doubly-linked list
*
******************************************************************************/

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import java.util.NoSuchElementException;
import java.util.Iterator;

/**
 * Generic RandomizedQueue class that allows standard enqueue
 * operations as a normal queue would, but for dequeue and iteration and
 * peek operations, allows items to be uniformly chosen at random and
 * handled thereafter (dequeued, returned, etc.)
 *
 * @author Ian Laurain
 *
 */
public class RandomizedQueue<Item> implements Iterable<Item> {

private int size; // the number of elements in the queue
private Item[] queue; // the queue

/**
 * Constructor for RandomizedQueue
 */
public RandomizedQueue() {
        queue = (Item[]) new Object[2]; // have to caste bc of generics
        size = 0;
}

/**
 * Returns true if queue is empty,
 * and false otherwise
 *
 * @return a boolean indicating if queue is empty or not
 */
public boolean isEmpty() {
        return size == 0;
}

/**
 * Returns number of items in queue
 *
 * @return size the number of items in queue
 */
public int size() {
        return size;
}

/**
 * Adds element to back of queue
 *
 * @param item the item to enqueue
 *
 */
public void enqueue(Item item) {
        if (item == null) {
                throw new NullPointerException("Item added cannot be null");
        }
        if (size == queue.length) {
                resize(2*queue.length);
        }
        queue[size++] = item;
}

/**
 * Removes item uniformly at random from queue
 *
 * @return item the item removed
 *
 */
public Item dequeue() {
        if (isEmpty()) {
                throw new NoSuchElementException("The Queue is empty");
        }
        int rand = StdRandom.uniform(size);
        Item item = queue[rand];
        if (rand != size-1) {
                queue[rand] = queue[size-1];
        }
        queue[size-1] = null;
        size--;
        if (size > 0 && size == queue.length/4) {
                resize(queue.length/2);
        }
        return item;
}

/**
 * Resizes queue when full or has too many empty spaces
 *
 * @param capacity the new length of the queue
 *
 */
private void resize(int capacity) {
        assert capacity >= size;
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < size; i++) {
                temp[i] = queue[i];
        }
        queue = temp;
}

/**
 * Returns an item, chosen uniformly at random, from queue, but
 * doesn't remove the item
 *
 * @param item the item chosen
 *
 */
public Item sample() {
        if (isEmpty()) {
                throw new NoSuchElementException("The Queue is empty");
        }
        int rand = StdRandom.uniform(size);
        return queue[rand];
}

/**
 * Prints the items in queue in front to back order after random shuffling
 */
private void print() {
        StringBuilder s = new StringBuilder();
        for (Item item : this) {
                s.append(item + " ");
        }
        String str = s.toString();
        StdOut.println(str);
}

/**
 * Returns an iterator to this queue that iterates through the items in
 * random order
 *
 * @return an iterator to this stack that iterates through the items in random
 * order
 */
public Iterator<Item> iterator() {
        return new QueueIterator();
}

private class QueueIterator implements Iterator<Item> {
private int i;

public QueueIterator() {
        i = size-1;
        if (size-1 > 0) {
                StdRandom.shuffle((Item[]) queue, 0, size-1);
        }
}

public boolean hasNext() {
        return i >= 0;
}

public void remove() {
        throw new UnsupportedOperationException("Can't call remove in iterator");
}

public Item next() {
        if (!hasNext()) {
                throw new NoSuchElementException("The Queue is empty");
        }
        return queue[i--];
}
}

/**
 * For testing purposes
 */
public static void main(String[] args) {
        RandomizedQueue<Integer> queue = new RandomizedQueue<Integer>();
        int n = 50;
        for (int i = 0; i < n; i++) {
                int rand = StdRandom.uniform(101);
                queue.enqueue(rand);
        }

        queue.print();

        for (int i = 0; i < n; i++) {
                int popped = queue.dequeue();
                queue.print();
        }
}
}

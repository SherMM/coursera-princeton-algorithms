/******************************************************************************
*  Compilation:  javac-algs4 Deque.java
*  Execution:    java-algs4 Deque
*  Dependencies: Iterator, NoSuchElementException, algs4.jar, algs4.StdOut
*
* Implements a deque as doubly-linked list
*
******************************************************************************/


import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdOut;

/**
 * Generic Deque class that can be used as stack or queue
 *
 * @author Ian Laurain
 *
 */
public class Deque<Item> implements Iterable<Item> {

private Node<Item> head; // pointer to first element in deque
private Node<Item> tail; // pointer to last element in deque
private int size; // number of elements in deque

/**
 * Node class for maintaining item value
 * and prev and next pointers
 *
 */
private static class Node<Item> {

private Item item;
private Node<Item> next;
private Node<Item> prev;
}

/**
 * Constructor for Deque
 */
public Deque() {
        head = null;
        tail = null;
        size = 0;
}

/**
 * Returns true if deque is empty,
 * and false otherwise
 *
 * @return a boolean indicating if deque is empty or not
 */
public boolean isEmpty() {
        return size == 0;
}

/**
 * Returns number of items in deque
 *
 * @return size the number of items in deque
 */
public int size() {
        return size;
}

/**
 * Adds item to front of deque and changes head item
 * to this new item
 *
 * @param item the item to add
 */
public void addFirst(Item item) {
        if (item == null) {
                throw new NullPointerException("Item added cannot be null");
        }
        Node<Item> oldhead = head;
        head = new Node<Item>();
        head.item = item;
        head.next = oldhead;
        if (!isEmpty()) {
                oldhead.prev = head;
        } else {
                tail = head;
        }
        size++;
}

/**
 * Adds item to back of deque and changes tail item
 * to this new item
 *
 * @param item the item to add
 */
public void addLast(Item item) {
        if (item == null) {
                throw new NullPointerException("Item added cannot be null");
        }
        Node<Item> oldtail = tail;
        tail = new Node<Item>();
        tail.item = item;
        tail.next = null;
        tail.prev = oldtail;
        if (isEmpty()) {
                head = tail;
        } else {
                oldtail.next = tail;
        }
        size++;
}

/**
 * Removes current head item from deque and sets
 * the next item it points to to be the new head item
 * and returns the removed item
 *
 * @return item the item removed
 */
public Item removeFirst() {
        if (isEmpty()) {
                throw new NoSuchElementException("Deque is empty");
        }
        Item item = head.item;
        head.prev = null;
        head = head.next;
        size--;
        if (isEmpty()) {
                tail = null;
        }
        return item;
}

/**
 * Removes current tail item from deque and sets
 * the previous item it points to to be the new tail item
 * and returns the removed item
 *
 * @return item the item removed
 */
public Item removeLast() {
        if (this.isEmpty()) {
                throw new NoSuchElementException("Deque is empty");
        }
        Item item = tail.item;
        tail.next = null;
        tail = tail.prev;
        size--;
        if (isEmpty()) {
                head = null;
        }
        return item;
}

/**
 * Prints out the items in the deque
 *
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
 * Returns an iterator to this deque that iterates through the items in front
 * to back order.
 *
 * @return an iterator to this stack that iterates through the items in front to
 * back order
 */
public Iterator<Item> iterator() {
        return new DequeIterator<Item>(head);
}

private class DequeIterator<Item> implements Iterator<Item> {
private Node<Item> curr;

public DequeIterator(Node<Item> head) {
        curr = head;
}

public boolean hasNext() {
        return curr != null;
}

public void remove() {
        throw new UnsupportedOperationException("Can't call remove from iterator");
}

public Item next() {
        if (!hasNext()) {
                throw new NoSuchElementException("No more elements in deque");
        }
        Item item = curr.item;
        curr = curr.next;
        return item;
}
}

/**
 * For testing
 */
public static void main(String[] args) {
        Deque<Integer> deck = new Deque<Integer>();

        StdOut.print("Deque size: ");
        StdOut.println(deck.size());
        StdOut.println("Deque is empty: " + deck.isEmpty());

        // try to add null item
        try {
          deck.addFirst(null);
        } catch (NullPointerException e) {
          StdOut.println("Null Pointer Exception: " + e.getMessage());
        }


        int n = 5;
        // add some items to front of deque
        for (int i = 0; i < n; i++) {
                deck.addFirst(i);
        }

        // add some items to back of deque
        for (int i = n; i < 2*n+1; i++) {
                deck.addLast(i);
        }

        // print deck
        StdOut.print("Deque: ");
        deck.print();

        while (!deck.isEmpty()) {
          StdOut.println("Removing: " + deck.removeLast());
        }

        StdOut.print("Deque: ");
        deck.print();



}
}

package Part1_Fundamentals;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Author Pierre Schaus
 *
 * We are interested in the implementation of a circular simply linked list,
 * i.e. a list for which the last position of the list refers, as the next position,
 * to the first position of the list.
 *
 * The addition of a new element (enqueue method) is done at the end of the list and
 * the removal (remove method) is done at a particular index of the list.
 *
 * A (single) reference to the end of the list (last) is necessary to perform all operations on this queue.
 *
 * You are therefore asked to implement this circular simply linked list by completing the class see (TODO's)
 * Most important methods are:
 *
 * - the enqueue to add an element;
 * - the remove method [The exception IndexOutOfBoundsException is thrown when the index value is not between 0 and size()-1];
 * - the iterator (ListIterator) used to browse the list in FIFO.
 *
 * @param <Item>
 */
public class CircularLinkedList<Item> implements Iterable<Item> {
    private long modCount = 0; // count the number of operations
    private int n;        // size of the stack
    private Node last;    // trailer of the list

    // helper linked list class
    private class Node {
        private Item item;
        private Node next;
    }

    public CircularLinkedList() {
        // TODO initialize instance variables
        this.n = 0;
    }

    public boolean isEmpty() {
        // TODO
        return n == 0;
    }

    public int size() {
        // TODO
        return n;
    }

    private long modCount() {
        return modCount;
    }

    /**
     * Append an item at the end of the list
     * @param item the item to append
     */
    public void enqueue(Item item) {
        // 0 -> 1 -> 2 -> 3 -> 4 -> 5 -> 6 -> 7 -> 8 -> 9
        // TODO
        modCount++;
        // if 1st elem in list
        if (n == 0) {
            last = new Node();
            last.item = item;
            last.next = last;
        }
        else {
            Node newItem = new Node();
            newItem.item = item;
            // 10.next = 9.next = 0
            // 9.next = 10
            // last = 10
            newItem.next = last.next;
            last.next = newItem;
            last = newItem;
        }
        n++;
    }

    /**
     * Removes the element at the specified position in this list.
     * Shifts any subsequent elements to the left (subtracts one from their indices).
     * Returns the element that was removed from the list.
     */
    public Item remove(int index) {
        // 0 -> 1 -> 2 -> 3 -> 4 -> 5 -> 6 -> 7 -> 8 -> 9
        // TODO: remove using only last and no prev
        // loop through list until find element at index
        // ex: remove(4)
        // 3.next = 5
        // n--
        // modCount++
        // DONE
        modCount++;
        if (n == 0) throw new NoSuchElementException();
        if (index < 0 || index > size()-1) throw new IndexOutOfBoundsException();
        Node prev = last.next;
        // loop through list until find element juste before index
        for (int i = 0; i < index-1; i++) {
            prev = prev.next;
        }
        if (index == 0) {
            Item ret = last.next.item;
            last.next = last.next.next;
            n--;
            return ret;
        }
        Item ret = prev.next.item;
        prev.next = prev.next.next;
        n--;
        return ret;
    }

    /**
     * Returns an iterator that iterates through the items in FIFO order.
     * @return an iterator that iterates through the items in FIFO order.
     */
    public Iterator<Item> iterator() { return new ListIterator(); }

    /**
     * Implementation of an iterator that iterates through the items in FIFO order.
     * The iterator should implement a fail-fast strategy, that is ConcurrentModificationException
     * is thrown whenever the list is modified while iterating on it.
     * This can be achieved by counting the number of operations (nOp) in the list and
     * updating it everytime a method modifying the list is called.
     * Whenever it gets the next value (i.e. using next() method), and if it finds that the
     * nOp has been modified after this iterator has been created, it throws ConcurrentModificationException.
     */
    private class ListIterator implements Iterator<Item> {
        // TODO You probably need a constructor here and some instance variables
        long expectedModCount;
        Node current;
        int remaining;

        private ListIterator() {
            expectedModCount = modCount();
            if (n == 0) current = null;
            else current = last.next;
            remaining = n;
        }

        @Override
        public boolean hasNext() {
            return remaining > 0;
        }

        @Override
        public Item next() {
            if (expectedModCount != modCount()) throw new ConcurrentModificationException();
            if (!hasNext()) throw new NoSuchElementException();
            Item ret = current.item;;
            current = current.next;
            remaining--;
            return ret;
        }

    }

}

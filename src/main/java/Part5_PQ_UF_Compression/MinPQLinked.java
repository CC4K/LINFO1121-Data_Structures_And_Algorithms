package Part5_PQ_UF_Compression;

import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 *  Author: Pierre Schaus
 * <p>
 *  Generic min priority queue implementation with a linked-data-structure
 *  The heap-tree is internally represented with Node's that store the keys
 *  Each node:
 * <p>
 *  - has a pointer to its children and parent:
 *  <p>
 *      - a null pointer indicates the absence of child/parent
 *      <p>
 *  - a key (the values stored in the heap)
 *  <p>
 *  - a size that is equal to the number of descendant nodes
 *  <p>
 *  A heap is an essentially an almost complete tree
 *  that satisfies the heap property:
 *  for any given node the key is less than the ones in the descendant nodes
 *  Here is an example of a heap:
 * <pre>
 *               3
 *           /      \
 *         5         4
 *        /  \      /  \
 *       6    7    8    5
 *      / \  / \  /
 *     7  8 8  9 9
 * </pre>
 *  The insert and min methods are already implemented maintaining the heap property
 *  Your task is to implement the delMin() method.
 *  This method should execute in O(log(n)) where n is the number of elements in the priority queue
 * <p>
 *  You can add any method that you want but leave the instance variable
 *  and public API untouched since it used by the tests
 * <p>
 * Hint: use the unit tests to debug your code, you might get some inspiration from the insert method
 * @param <Key> the generic type of key on this priority queue
 */
public class MinPQLinked<Key> {

    // class used to implement the Nodes in the heap
    public class Node {
        public Key value;
        public Node left;
        public Node right;
        public Node parent;
        public int size;

        public Node(Node parent) {
            this.parent = parent;
            this.size = 1;
        }
    }

    public Node root; // number of items on priority queue
    public Comparator<Key> comparator;  // comparator used to compare the keys


    /**
     * Initializes an empty priority queue using the given comparator.
     * @param  comparator the order in which to compare the keys
     */
    public MinPQLinked(Comparator<Key> comparator) {
        this.comparator = comparator;
    }

    /**
     * Returns true if this priority queue is empty.
     * @return {@code true} if this priority queue is empty;
     *         {@code false} otherwise
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Returns the number of keys on this priority queue.
     * @return the number of keys on this priority queue
     */
    public int size() {
        if (root == null) return 0;
        else return root.size;
    }

    /**
     * Returns a smallest key on this priority queue.
     * @return a smallest key on this priority queue
     * @throws NoSuchElementException if this priority queue is empty
     */
    public Key min() {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        return root.value;
    }

    /**
     * Adds a new key to this priority queue.
     * @param  x the key to add to this priority queue
     */
    public void insert(Key x) {
        if (root == null) {
            root = new Node(null);
            root.value = x;
        }
        else {
            Node n = createNodeInLastLayer(); // create the new node in last layer
            n.value = x; // store x in this node
            swim(n); // restore the heap property from this leaf node to the root
        }
    }
    // maintains the heap invariant
    private void swim(Node n) {
        while (n != root && smallerThan(n, n.parent)) {
            swap(n, n.parent);
            n = n.parent;
        }
    }
    // Creates a new empty node in last layer (ensuring it stay essentially an almost complete tree)
    // and returns the node
    private Node createNodeInLastLayer() {
        Node current = root;
        current.size++;
        while (current.left != null && current.right != null) {
            // both left and right are not null
            if (isPowerOfTwo(current.left.size+1) && current.right.size < current.left.size) {
                // left is complete and there is fewer in right subtree
                current = current.right; // follow right direction
            }
            else {
                current = current.left; // follow left direction
            }
            current.size++; // augment size sisnce this node will have new descendant
        }
        // hook up the new node
        if (current.left == null) {
            current.left = new Node(current);
            return current.left;
        }
        else {
            assert (current.right == null);
            current.right = new Node(current);
            return current.right;
        }
    }

    /**
     * Removes and returns a smallest key on this priority queue.
     * @return a smallest key on this priority queue
     * @throws NoSuchElementException if this priority queue is empty
     */
    public Key delMin() {
        if (isEmpty()) throw new NoSuchElementException();
        Key min = min();
        if (size() == 1) {
            root = null; // back to Constructor state
            return min;
        }
        // TODO: THE WORST EXERCISE ON EARTH, I HATE TREES (no Lorax, not that kind of trees)
        // delete last Node and return its value while decreasing everyone's size
        Key lastValue = deleteLast();
        // set at root
        root.value = lastValue;
        // sink the root
        sink(root);
        return min;
    }
    private void sink(Node n) {
        while (n.left != null) { // last layer check
            // find smallest child
            Node smallest_child = n.left;
            if (n.right != null) {
                if (smallerThan(n.right, n.left)) smallest_child = n.right;
                else if (smallerThan(n.left, n.right)) smallest_child = n.left;
            }
            // need to be n < children
            if (smallerThan(n, smallest_child)) break; // mission accomplished
            else {
                swap(n, smallest_child);
                n = smallest_child;
            }
        }
    }
    private Key deleteLast() {
        Node n = root; // start at the top
        if (size() == 1) {
            root = null;
            return n.value;
        }
        n.size--;
        while (n.left != null) {
            // if left not complete => go left      + if right null => also go left
            if (!isPowerOfTwo(n.left.size+1) || n.right == null) {
                n = n.left;
            }
            // if left complete => check probably right
            else if (isPowerOfTwo(n.left.size+1)) {
                // if right not complete => actually go right
                if (!isPowerOfTwo(n.right.size+1)) {
                    n = n.right;
                }
                // if right complete => compare size to determine direction
                else if (isPowerOfTwo(n.right.size+1)) {
                    // go to the largest part of the tree
                    if (n.left.size > n.right.size) {
                        n = n.left;
                    }
                    else if (n.right.size > n.left.size) {
                        n = n.right;
                    }
                }
            }
            // every step => decrease size
            n.size--;
        }
        // delete yourself by deleting link from parent
        Key lastValue = n.value;
        if (n.parent.left.value == lastValue) n.parent.left = null;
        else if (n.parent.right.value == lastValue) n.parent.right = null;
        return lastValue;
    }


    /***************************************************************************
     * Helper functions for compares and swaps.
     ***************************************************************************/

    // check if x = 2^n for some x>0
    private boolean isPowerOfTwo(int x) {
        return (x & (x - 1)) == 0;
    }

    // Check if node i > j
    private boolean greaterThan(Node i, Node j) {
        return comparator.compare(i.value,j.value) > 0;
    }

    // Check if node i > j
    private boolean smallerThan(Node i, Node j) {
        return comparator.compare(i.value,j.value) < 0;
    }

    // exchange the values in two nodes
    private void swap(Node i, Node j) {
        Key swap = i.value;
        i.value = j.value;
        j.value = swap;
    }

}

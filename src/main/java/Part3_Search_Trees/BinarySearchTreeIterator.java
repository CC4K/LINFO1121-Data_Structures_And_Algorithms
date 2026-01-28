package Part3_Search_Trees;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * In this exercise, we are interested in implementing an iterator (BSTIterator) for a Binary Search Tree (BST).
 * The iterator must traverse the tree in-order. This means that for each node, the left sub-tree is visited, then the node and finally the right sub-tree.
 * <p>
 *  For example, consider the following tree
 * <p>
 *                              12
 *                              |
 *                 8 ------------------------ 18
 *                  |                          |
 *           3 ------------ 11       14 -------------- 20
 *                          |        |
 *                     9 ---         --- 15
 * <p>
 *
 * The iterator visits the nodes in this order: 3, 8, 9, 11, 12, 14, 15, 18, 20
 * We ask you to complete the BSTIterator class, which must implement the Iterator interface.
 * <p>
 * The BSTNode are generic over their key (the integers in the example above) and implement the 
 * BinaryNode and KeyNode interface available in the utils package.
 * <p>
 * Hint: You have two strategies to implement this iterator Fail-Fast and Fail-Safe
 * https://www.geeksforgeeks.org/fail-fast-fail-safe-iterators-java/
 * <p>
 * The Fail-Safe will collect all the keys in a collection and return an iterator on this collection.
 * The Fail-Fast will lazily return the elements and throw an exception if the BST is modified while iterating on it.
 * <p>
 * The advantage of Fail-Fast is that the constructor and iteration is Lazy.
 * The total cost of iterating will be Theta(n) but the initialization can be O(h).
 * <p>
 * It is a good exercise to implement both version.
 */
public class BinarySearchTreeIterator<Key extends Comparable<Key>> implements Iterable<Key> {

    private BSTNode<Key> root;

    public BinarySearchTreeIterator() { };

    /**
     * Returns the size of the tree
     */
    public int size() { return this.size(root); }

    /**
     * Returns the size the subtree rooted at node
     *
     * @param node the root of the subtree
     */
    private int size(BSTNode<Key> node) { return (node == null) ? 0 : node.getSize(); }

    /**
     * Adds a value in the tree
     *
     * @param key the value to add
     */
    public void put(Key key) { this.root = put(this.root, key); }

    /**
     * Adds a value in a subtree of the tree
     *
     * @param node the root of the subtree
     * @param key the value to add
     */
    private BSTNode<Key> put(BSTNode<Key> node, Key key) {
        if (node == null)   return new BSTNode<>(key, 1);
        int cmp = key.compareTo(node.getKey());
        if (cmp < 0)        node.setLeft(put(node.getLeft(), key));     // if smaller put on the left
        else if (cmp > 0)   node.setRight(put(node.getRight(), key));   // if greater put on the right
        node.setSize(1 + size(node.getLeft()) + size(node.getRight()));
        return node;
    }

    @Override
    public Iterator<Key> iterator() { return new BSTIterator(); }

    private class BSTIterator implements Iterator<Key> {
        // TODO
        Stack<BSTNode<Key>> leftParents;
        BSTNode<Key> currentNode;
        int size;


        private BSTIterator() {
            size = size();
            leftParents = new Stack<>(); // left parents from top to bottom
            // fill the stack starting from root
            currentNode = root;
            while (currentNode != null) {
                leftParents.push(currentNode);
                currentNode = currentNode.getLeft(); // go left
            }
            System.out.print("[ "); for (BSTNode<Key> bstNode : leftParents) { System.out.print(bstNode.getKey()+" "); } System.out.println("]");
        }

        @Override
        public boolean hasNext() {
            if (size != size()) throw new ConcurrentModificationException();
            return !leftParents.isEmpty();
        }

        /*          12
         *       /       \
         *     8          18
         *   /   \       /   \
         *  3    11    14    20
         *  x    / \   / \    x
         *      9        15
         *      x         x
         */
        @Override
        public Key next() {
            if (!hasNext()) throw new NoSuchElementException();
            if (size != size()) throw new ConcurrentModificationException();
            BSTNode<Key> currentNode = leftParents.pop();
            Key ret = currentNode.getKey();
            if (currentNode.getRight() != null) {   // move right once
                currentNode = currentNode.getRight();
                while (currentNode != null) {       // keep moving left
                    // save all the leftmost nodes to the right of the current node for later
                    leftParents.push(currentNode);
                    currentNode = currentNode.getLeft();
                }
            }
            System.out.print("[ "); for (BSTNode<Key> bstNode : leftParents) { System.out.print(bstNode.getKey()+" "); } System.out.print("] ");
            System.out.println("ret : "+ret);
            return ret;
        }
    }

    static class BSTNode<K extends Comparable<K>> {

        private BSTNode<K> left;
        private BSTNode<K> right;
        private K key;
        private int size;

        public BSTNode(K key) {
            this.left = null;
            this.right = null;
            this.key = key;
            this.size = 0;
        }

        public BSTNode(K key, int size) {
            this.left = null;
            this.right = null;
            this.key = key;
            this.size = size;
        }

        public BSTNode<K> getLeft() { return this.left; }

        @SuppressWarnings("unchecked")
        public void setLeft(BSTNode<K> node) { this.left = node; }

        public BSTNode<K> getRight() { return this.right; }

        @SuppressWarnings("unchecked")
        public void setRight(BSTNode<K> node) { this.right = node; }

        public K getKey() { return this.key; }

        public void setKey(K newKey) { this.key = newKey; }

        public int getSize() { return this.size; }

        public void setSize(int newSize) { this.size = newSize; }

        /**
         * Adds a new value in the subtree rooted at this node
         */
        public void add(K key) {
            if (key.compareTo(this.key) > 0) {  // if positive add to the right
                if (this.right == null) this.right = new BSTNode<>(key);    // if leaf create new node
                else                    this.right.add(key);                // else add key
            }
            else {                              // if negative add to the left
                if (this.left == null)  this.left = new BSTNode<>(key);     // if leaf create new node
                else                    this.left.add(key);                 // else add key
            }
        }
    }
}

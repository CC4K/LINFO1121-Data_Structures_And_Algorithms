package Part3_Search_Trees;

import java.util.TreeSet;

/**
 * You are given a binary search tree (BST) whose nodes implement the BinaryNode and KeyNode interfaces
 * available in the utils package.
 * <p>
 * We ask you to complete the ceil method, which take as argument the root of a BST and an integer `value`.
 * This method finds a node `N` in the BST such that
 *  - Its value is greater or equal than `value`
 *  - No nodes whose value is greater than `value` has a value lower than `N`
 *  and returns its value.
 *  If no such node exists, the method returns null.
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
 * - The ceiled valued of 11 is 11
 * - The ceiled valued of 4 is 8
 * - The ceiled valued of 21 is null
 */
public class BinarySearchTree {

    /**
     * Returns the ceiled value of `value` in the tree rooted at `root`
     *
     * @param root the root of the tree
     * @param value the value we want to ceil
     */
    /*          12
     *       /       \
     *     8          18
     *   /   \       /   \
     *  3    11    14    20
     *  x    / x   x \    x
     *      9        15
     *      x         x
     */
    public static Integer ceil(BSTNode<Integer> root, int value) { return ceilAux(root, value, null); }

    private static Integer ceilAux(BSTNode<Integer> root, int value, Integer oldRootKey) {
        if (root == null) return oldRootKey;      // Leaf reached => return best possible value
        // TODO
        if (value == root.getKey()) return value; // EZ case
        else if (value > root.getKey()) {         // value is somewhere to the right (EZ case)
            return ceilAux(root.getRight(), value, oldRootKey);
        }
        else { // if (value < root.getKey())      // value is somewhere to the left (horrible case nooooo)
            // ceil is either the current root or somewhere under the root
            if (oldRootKey == null || oldRootKey > root.getKey()) { // check null ref FIRST !!!
                return ceilAux(root.getLeft(), value, root.getKey());
            }
            else { // if (oldRootKey < root.getKey())
                return ceilAux(root.getLeft(), value, oldRootKey);
            }
        }
    }

    public static void main(String[] args) {
        TreeSet<Integer> correct = new java.util.TreeSet<>();
        int [] values = new int []{12, 8, 18, 3, 11, 14, 20, 9, 15};
        int [] inputs = new int []{11, 14, 9, 4, 16, 10, 19, 21, 30, 40};

        BinarySearchTree.BSTNode<Integer> root = new BinarySearchTree.BSTNode<>(values[0]);
        correct.add(values[0]);
        for (int i = 0; i < values.length; i++) {
            root.add(values[i]);
            correct.add(values[i]);
        }

        for (int i: inputs) {
            System.out.println("ceil("+i+") = "+correct.ceiling(i)+" | myCeil("+i+") = "+BinarySearchTree.ceil(root, i));
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

        public BSTNode<K> getLeft() {
            return this.left;
        }

        @SuppressWarnings("unchecked")
        public void setLeft(BSTNode<K> node) {
            this.left = node;
        }

        public BSTNode<K> getRight() {
            return this.right;
        }

        @SuppressWarnings("unchecked")
        public void setRight(BSTNode<K> node) {
            this.right = node;
        }

        public K getKey() {
            return this.key;
        }

        public void setKey(K newKey) {
            this.key = newKey;
        }

        public int getSize() {
            return this.size;
        }

        public void setSize(int newSize) {
            this.size = newSize;
        }

        /**
         * Adds a new value in the subtree rooted a this node
         */
        public void add(K key) {
            if (key.compareTo(this.key) > 0) {
                if (this.right == null) {
                    this.right = new BSTNode<>(key);
                } else {
                    this.right.add(key);
                }
            } else {
                if (this.left == null) {
                    this.left = new BSTNode<>(key);
                } else {
                    this.left.add(key);
                }
            }
        }
    }

}

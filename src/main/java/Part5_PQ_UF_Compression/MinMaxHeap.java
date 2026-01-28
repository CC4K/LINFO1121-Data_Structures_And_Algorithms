package Part5_PQ_UF_Compression;

import static java.lang.Math.log;

/**
 * This class represent a MinMaxHeap (generic over a Key type) implement as an array-based array.
 * Such data structure is useful to store elements and retrieve their minimum/maximum in constant time.
 * In order to do so, the invariants that are usually used in minimum or maximum heap are adapted.
 * For example, the `BinaryHeap` class (that you can find in another exercise) implements a minimum heap
 * by keeping the following invariant true: "For any node in the tree, every of its descendant has a value
 * greater or equal than it".
 * A similar invariant can be expressed for maximum heap.
 * <p>
 * In a MinMaxHeap, the tree is decomposed into layers based on the depth of the node:
 * <p>
 *  - Every node in a depth which is even (0, 2, 4, ...) is a *min layer*
 *  <p>
 *  - Every node in a depth which is odd (1, 3, 5, ...) is a *max layer*
 * <p>
 * and the following invariants hold:
 * <p>
 *  - For every node in a min layer, every of its descendant has a value greater than it
 *  <p>
 *  - For every node in a max layer, every of its descendant has a value lower than it
 * <p>
 * In this exercise you need implement three methods:
 * <p>
 *  1) The min() method which return the minimum of the heap
 *  <p>
 *  2) The max() method which return the maximum of the heap
 *  <p>
 *  3) The swim() method which is used after a call to `insert` for ensuring the invariants of the MinMaxHeap are respected
 * <p>
 * To help you for the swim function, a `getNodeDepth` function is provided which returns the depth of a node.
 */
public class MinMaxHeap<Key extends Comparable<Key>> {

    private Key[] content;
    private int size;

    @SuppressWarnings("unchecked")
    public MinMaxHeap(int initialSize) {
        this.content = (Key[]) new Comparable[initialSize];
        this.size = 0;
    }

    @SuppressWarnings("unchecked")
    private void increaseSize() {
        Key[] newContent = (Key[]) new Comparable[content.length*2];
        System.arraycopy(content, 0, newContent, 0, content.length);
        content = newContent;
    }

    /**
     * Returns the size of this heap
     */
    public int size() {
        return size;
    }

    /**
     * Returns the minimum of the heap.
     * Expected time complexity: O(1)
     */
    public Key min() {
        // TODO
        return content[1];
    }

    /**
     * Returns the maximum of the heap.
     * Expected time complexity: O(1)
     */
    public Key max() {
        // TODO
        if (size == 1) return content[1];
        if (size == 2) return content[2];
        return higherThan(content[2], content[3]) ? content[2] : content[3];
    }

    /**
     * Swaps the elements at index i and j in the
     * array representing the tree
     * @param i the first index to swap
     * @param j the second index to swap
     */
    private void swap(int i, int j) {
        Key tmp = content[i];
        content[i] = content[j];
        content[j] = tmp;
    }

    /**
     * Returns true if the first key is less than the second key
     * @param key The base key for comparison
     * @param comparedTo The key compared to
     */
    private boolean lessThan(Key key, Key comparedTo) {
        return key.compareTo(comparedTo) < 0;
    }

    /**
     * Returns true if the first key is greater than the second key
     * @param key The base key for comparison
     * @param comparedTo The key compared to
     */
    private boolean higherThan(Key key, Key comparedTo) {
        return key.compareTo(comparedTo) > 0;
    }

    /**
     * Returns the depth of the node at a given position
     * @param position The index in the `content` array for which the depth must be computed
     */
    private int getNodeDepth(int position) {
        // There is no log2 function in java.lang.Math so we use this little formula to compute the log2 of K
        // (which gives, when rounded to its integer value, the depth of the index)
        return (int) (log(position) / log(2));
    }

    /**
     * Swim a node in the tree
     * @param position The position of the node to swim in the `content` array
     */
    public void swim(int position) {
        if (position == 1) return;
        if (position == 2 || position == 3) {
            if (lessThan(content[position], content[1])) swap(position, 1);
        }
        // TODO
        int depth = getNodeDepth(position);
        boolean isMin = depth % 2 == 0;
        boolean isMax = !isMin;
        int parent_position = (position % 2 == 0) ? (position/2) : ((position-1)/2);
        boolean grandparents_exist = (parent_position/2 != 0 || (parent_position-1)/2 != 0);
        int grandparent_position = (parent_position % 2 == 0) ? (parent_position/2) : ((parent_position-1)/2);
        //System.out.printf("(i = %d) layer %d (%s)\n", position, depth, (isMin) ? "Min" : "Max");
        // compare with parent and grandparents to check the invariants
        if (isMin) {
            // swap if > parent
            if (higherThan(content[position], content[parent_position])) {
                swap(position, parent_position);
                swim(parent_position);
            }
            // swap if < grandparent
            else if (grandparents_exist && lessThan(content[position], content[grandparent_position])) {
                swap(position, grandparent_position);
                swim(grandparent_position);
            }
        }
        else { // isMax
            // swap if < parent
            if (lessThan(content[position], content[parent_position])) {
                swap(position, parent_position);
                swim(parent_position);
            }
            // swap if > grandparent
            else if (grandparents_exist && higherThan(content[position], content[grandparent_position])) {
                swap(position, grandparent_position);
                swim(grandparent_position);
            }
        }
    }

    /**
     * Inserts a new value in the heap
     * @param k the value to insert
     */
    public void insert(Key k) {
        //System.out.println("inserting "+k);
        size += 1;
        if (size >= content.length) {
            increaseSize();
        }
        content[size] = k;
        swim(size);
    }

}

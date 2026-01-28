package Part5_PQ_UF_Compression;

/**
 * In this task, you must implement the `push` operation on a binary heap data structure.
 * As a reminder, a heap is a tree data structure such that the following invariant is respected
 * <p>
 *      For any node in the tree, the value associated with the node is higher (for a maxHeap) or lower
 *      (for a minHeap) than the value of its children.
 * <p>
 * As a consequence, the minimum/maximum value is located at the root and can be retrieved in constant time.
 * In particular, this invariant must be respected after a push (or remove) operation.
 * <p>
 * In this exercise the tree is represented with an array. The parent-child relation is implicitly represented
 * by the indices. A node at index i has its two children at index 2i and 2i+1.
 * For this is it assumed that the root is located at index 1 in the array.
 */
public class BinaryHeap {
    private int[] content;
    private int size;

    public BinaryHeap(int initialSize) {
        content = new int[initialSize];
        size = 0;
    }

    /**
     * Doubles the available size of this binary heap
     */
    private void increaseSize() {
        int [] newContent = new int[content.length*2];
        System.arraycopy(content, 0, newContent, 0, content.length);
        content = newContent;
    }

    /**
     * Add a new value in this heap
     * The expected time complexity is O(log(n)) with n the size of the binary heap
     * @param value the added value
     */
    public void push(int value) {
        // TODO
        // push value at the very end
        content[++size] = value;
        // increase size if needed
        if (content.length-1 <= size) increaseSize(); // -1 bcs index 0 unused
        // swim the value at the very end
        swim(size);
    }

    public void swim(int k) {
        while (content[k/2] > content[k] && k > 1) { // if child < key => swap
            swap(k, k/2);
            k = k/2;
        }
    }

    public void swap(int i, int j) {
        int tmp = content[i];
        content[i] = content[j];
        content[j] = tmp;
    }

    /**
     * Returns the content of this heap
     */
    public int[] getContent() {
        return content;
    }

    /**
     * Returns the size of this heap
     */
    public int size() {
        return size;
    }

    /**
     * Returns the root of the tree representing this heap
     */
    public int getRoot() {
        return content[1];
    }
}

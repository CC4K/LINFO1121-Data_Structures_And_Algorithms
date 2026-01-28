package Part5_PQ_UF_Compression;

/**
 * A max-heap is a hierarchical tree structure with the following invariants:
 *  - The tree is essentially complete, i.e., all levels of the tree are filled except possibly the right most child part of the last one,
 *  - For any node in the tree, the value associated with the node is greater or equal than the values of its children.
 * <p>
 * Consequently, the maximum value is situated at the root and can be accessed in constant time.
 * Notably, this invariant must be maintained after insertions or removals.
 * <p>
 * In this assignment, your task is to implement the insert, size, getMax, and delMax operations for a ternary max heap data structure
 * implemented with an array.
 * In a ternary max heap, each node can have at most three children, and all children have values lower than the parent node.
 * The tree is represented by the array `content`, where the parent-child relationship is implicitly defined by indices.
 * Specifically, a node at index i has three children at indices 3i+1, 3i+2, and 3i+3.
 * It is assumed that the root is at index 0 in the array.
 * <p>
 * For instance, consider a heap with a capacity of 6. After inserting numbers <8,2,3,8,9> in this order,
 * the array content could be as follows:
 * content: [9, 8, 3, 8, 2, 0], the size = 5 and the heap looks like this :
 * <pre>
 *                                         9
 *                                         |
 *                              ----------------------
 *                              |          |         |
 *                              8          3         8
 *                              |
 *                              2
 * </pre>
 * Now after deleting the max, the array content could be :
 * content [8, 2, 3, 8, (9), 0] and the size = 4 and the heap :
 * <pre>
 *                                         8
 *                                         |
 *                              ----------------------
 *                              |          |         |
 *                              2          3         8
 * </pre>
 * Notice that we left the 9 in the array, but it is not part of the heap anymore since the size is 4.
 * <p>
 * To remove the maximum element from the heap while maintaining its structure,
 * the approach involved swapping the root with last element of the last layer (at position size-1) in the content array.
 * Subsequently, re-heapify the structure by allowing the new root to sink using swap with the largest of its children
 * until it is greater than all its children or reaches a leaf.
 * <p>
 * Complete the implementation of the TernaryHeap class.
 * <p>
 * The insert operation should run in O(log_3(n)) time, where n is the number of elements in the heap.
 * <p>
 * The delMax operation should run in O(log_3(n)) time, where n is the number of elements in the heap.
 * <p>
 * The getMax operation should run in O(1) time.
 * <p>
 * Debug your code on the small examples in the test suite.
 */
public class TernaryHeap {
    // Array representing the heap. This is where all the values must be added
    // let this variable protected so that it can be accessed from the test suite
    protected int[] content;
    protected int size;

    /**
     * Initializes an empty max-heap with the given initial capacity.
     * @param capacity : the initial capacity of the heap
     */
    public TernaryHeap(int capacity) {
        // TODO
        this.content = new int[capacity];
        this.size = 0;
    }

    /**
     * @return the number of keys currently in the heap.
     */
    public int size() {
        // TODO
        return size;
    }

    /**
     * Inserts a new key into the heap. After this method is finished, the heap-invariant must be respected.
     * @param val The key to be inserted
     */
    public void insert(int val) {
        // TODO: insert (valKey, val) by exchanging with (parentKey, parentVal)
        int valKey = size;
        if (valKey < content.length) {
            //System.out.print(Arrays.toString(content)+" => ");
            content[valKey] = val;
            // swim
            int parentKey = getParentKey(valKey);
            while (parentKey != -1 && val > content[parentKey]) {
                // swap valKey and parentKey
                swap(valKey, parentKey);
                // update valKey + current_parent
                valKey = parentKey;
                parentKey = getParentKey(valKey);
            }
            //System.out.println(Arrays.toString(content));
            size++;
        }
    }

    /**
     * Removes and returns the largest key on the heap. After this method is finished, the heap-invariant must be respected.
     * @return The largest key on the heap
     */
    public int delMax() {
        int max = getMax();
        // TODO: put last item at root then make it sink
        //System.out.println(Arrays.toString(content));

        size--;
        content[0] = content[size];
        content[size] = 0;

        //System.out.println(Arrays.toString(content));

        // sink
        int key = 0; // start at root
        while (key < size && getLargestChildN(key) != -1) {
            // determine which of the 3 children of position is the largest
            int maxChildKey = getChildNKey(key, getLargestChildN(key));
            //System.out.printf("thickest boy at %d : %s\n", position, largest_child);

            if (content[key] > content[maxChildKey]) break;
            swap(key, maxChildKey);

            //System.out.println(Arrays.toString(content));

            key = maxChildKey;
        }
        //System.out.println(Arrays.toString(content));
        return max;
    }

    /**
     * @return The largest key on the heap without removing it.
     */
    public int getMax() {
        // TODO
        return content[0];
    }

    public int getChildNKey(int k, int x) {
        return 3*k+x;
    }

    public int getLargestChildN(int k) {
        int[] childrens = getAllChildren(k); // [child1, child2, child3]
        if (childrens[0] == -1) return -1;

        int maxKey = 0;
        int prev_child = childrens[0];
        for (int i = 1; i <= 2; i++) {
            if (childrens[i] > prev_child) maxKey = i;
        }
        return maxKey+1; // 1 for child1, 2 for child2, 3 for child3
    }
    private int[] getAllChildren(int k) {
        int child1 = (3*k+1 < size) ? content[3*k+1] : 0;
        int child2 = (3*k+2 < size) ? content[3*k+2] : 0;
        int child3 = (3*k+3 < size) ? content[3*k+3] : 0;
        if (child1 == 0 && child2 == 0 && child3 == 0) return new int[]{-1, -1, -1};
        return new int[]{child1, child2, child3};
    }

    public int getParentKey(int k) {
        if (k == 0) return -1;
        int target = (k % 3 == 0) ? (k/3) - 1 : (k - (k%3))/3;
        return target;
    }

    public void swap(int k1, int k2) {
        int tmp = content[k1];
        content[k1] = content[k2];
        content[k2] = tmp;
    }

    public static void main(String[] args) {
        TernaryHeap heap = new TernaryHeap(10);
        heap.content = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

    }

}

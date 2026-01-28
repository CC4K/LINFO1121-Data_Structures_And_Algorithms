package Part5_PQ_UF_Compression;

import java.util.PriorityQueue;

/**
 * Imagine a data structure that supports :
 * <p>
 * - insertion in logarithmic time
 * <p>
 * - the median retrieval in constant time
 * <p>
 * - median deletion in logarithmic time
 * <p>
 * Hint: You can use two heaps to solve this problem.
 *       There is no need to implement your own heap, you can use the PriorityQueue class from Java.
 *       Think about the class invariant that you need to maintain after each insertion and deletion.
 * <p>
 * When the number of stored element is odd, the median value is the value at rank n+1/2 where n is the number of element
 * For example if the values {0, 1, 2, 3, 5} are stored, the median value is 2
 * When the number of stored element is even, you will arbitrarily consider that the median value is the value at rank n/2 + 1
 * For example if the values {0, 1, 3, 5} are stored, the median value is 3
 */
public class MedianHeap {
    /// Tree of values < median (MaxPQ)
    PriorityQueue<Integer> left_pq;
    /// Tree of values >= median (MinPQ)
    PriorityQueue<Integer> right_pq;

    public MedianHeap(int initialSize) {
        // TODO
        this.left_pq = new PriorityQueue<>(initialSize/2, (x, y) -> y - x);
        this.right_pq = new PriorityQueue<>(initialSize/2);
    }

    /**
     * Add a new value in the structure
     * The expected time complexity is O(log(n)) with n the size of the heap
     * @param value the added value
     */
    public void insertion(int value) {
        System.out.print("insert("+value+") ");

        if (!right_pq.isEmpty() && value < right_pq.peek()) {
            left_pq.add(value);
        }
        else {
            right_pq.add(value);
        }
        balance();
    }

    private void balance() {
        // [1, 2, 3] [4, 5] => [1, 2] [3, 4, 5]
        if (left_pq.size() > right_pq.size()) {
            right_pq.add(left_pq.poll());
        }
        // [1, 2] [3, 4, 5, 6] => [1, 2, 3] [4, 5, 6]
        if ((left_pq.size() + 1) < right_pq.size()) {
            left_pq.add(right_pq.poll());
        }

        System.out.println(left_pq +" "+ right_pq);
    }


    /**
     * Retrieve the median value of the stored elements
     * Complexity O(1)
     * @return the median
     */
    public int findMedian() {
        // TODO: peek right tree (EZ)
        return right_pq.peek();
    }

    /**
     * Delete and return the median value
     * Complexity O(log(n)) with n the number of stored elements
     * @return the median value
     */
    public int deleteMedian() {
        System.out.print("delete("+findMedian()+") ");

        // TODO: poll from right tree then rebalance
        int median = right_pq.poll();
        balance();
        return median;
    }

}

package Part1_Fundamentals;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Proposed at the exam of August 2025
 * You are asked to implement a specialized linked chain data structure
 * optimized for high-performance insertions.
 * <p>
 * This chain consists of n elements numbered from 0 to n-1.
 * Elements 0 and n-1 are always present in the chain and cannot be removed or relocated.
 * No elements may be inserted before 0 or after n-1.
 * All other elements start unlinked and can be inserted exactly once.
 * <p>
 * To ensure constant-time operations, the chain is represented internally using two arrays:
 * 	successor[i] and predecessor[i] represent the immediate next and
 * 	previous elements of element i in the chain.
 * 	If the element i is not yet part of the chain, we have successor[i] = predecessor[i] = i.
 *  By convention, predecessor[0] == 0 and successor[n - 1] == n - 1.
 * <p>
 * When the data structure is initialized with n elements,
 * the chain contains exactly two linked elements: 0 <-> n-1.
 * <p>
 * You must implement the following methods:
 * 	-	insertAfter(a, b): Insert element b immediately after element a in the chain.
 * 	-	insertBefore(a, b): Insert element b immediately before element a in the chain.
 * 	-   iterator() that enables to iterate over the elements of the chain in their current order,
 * 	    from 0 to n-1.
 */
public class LinkedChain implements Iterable<Integer> {

    protected int[] successor;
    protected int[] predecessor;
    protected int n;

    /**
     * Constructs a linked chain with n elements.
     * The chain is initialized with two linked elements: 0 <-> n-1.
     * The other elements (1 to n-2) are not yet in the chain.
     * @param n > 1 the capacity of the chain
     */
    public LinkedChain(int n) {
        // TODO
        this.n = n;
        this.successor = new int[n];
        this.predecessor = new int[n];

        successor[0] = n-1;
        predecessor[n-1] = 0;
        for (int i = 1; i <= n-2; i++) {
            successor[i] = i;
            predecessor[i] = i;
        }
    }

    /**
     * Inserts an element just after another one in the chain
     * @param a the element after which b will be inserted,
     *        "a" must be in the chain and must be different from n-1 (last element)
     * @param b the element to insert, it must not be in the chain
     * @throws IllegalArgumentException if "a" is not in the chain,
     *         or if "b" is already in the chain or if "a" is the last
     *         or if "a" or "b" are not in the range [0, n-1]
     */
    public void insertAfter(int a, int b) {
        // TODO
        if (a == n || isNotInRange(a) || isNotInRange(b) || !isInChain(a) || isInChain(b)) throw new IllegalArgumentException();
        // a = 0, b = 1
        // 2 = succ[0]
        // pred[1] = 0
        // pred[2] = 1
        // succ[0] = 1
        // succ[1] = 2
        int oldSucc = successor[a];
        predecessor[b] = a;
        predecessor[oldSucc] = b;
        successor[a] = b;
        successor[b] = oldSucc;
    }

    /**
     * Inserts element "b" just before element "a".
     * @param a the element before which "b" should be inserted,
     *        "a" must be in the chain and must be different from 0 (first element)
     * @param b the element to insert, it must not be in the chain
     * @throws IllegalArgumentException if "a" is not in the chain,
     *        or if "b" is already in the chain or if "a" is the first
     *        or if "a" or "b" are not in the range [0, n-1]
     */
    public void insertBefore(int a, int b) {
        // TODO
        if (a == 0 || isNotInRange(a) || isNotInRange(b) || !isInChain(a) || isInChain(b)) throw new IllegalArgumentException();
        // a = 2, b = 1
        // 0 = pred[2]
        // pred[1] = 0
        // pred[2] = 1
        // succ[0] = 1
        // succ[1] = 2
        int oldPred = predecessor[a];
        predecessor[b] = oldPred;
        predecessor[a] = b;
        successor[oldPred] = b;
        successor[b] = a;
    }

    public boolean isInChain(int i) {
        return successor[i] != predecessor[i] || predecessor[i] != i || successor[i] != i;
    }

    public boolean isNotInRange(int i) {
        return i < 0 || (n - 1) < i;
    }

    /**
     * Iterator over the chain from 0 to n-1 in the order they are connected.
     * @return an iterator over the chain, in the order they are connected,
     *         starting from the first i.e. 0 and ending with the last i.e. n-1
     */
    @Override
    public Iterator<Integer> iterator() {
        // TODO
        return new LinkedChainIterator();
    }

    private class LinkedChainIterator implements Iterator<Integer> {
        private int index = 0;
        private boolean shouldStop = false;

        @Override
        public boolean hasNext() {
            return !shouldStop;
        }

        @Override
        public Integer next() {
            if (!hasNext()) throw new NoSuchElementException();
            int ret = index; // return 0 as first element always
            if (index == n-1) shouldStop = true; // update if should stop next time
            index = successor[index]; // then get next index to get in successor
            return ret;
        }
    }
}

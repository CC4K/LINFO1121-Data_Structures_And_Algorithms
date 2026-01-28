package Part2_Sorting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Santa’s elves are preparing for Christmas, but there’s a problem in the North Pole kitchen! They’ve
 * accidentally stacked the Christmas pancakes (each with a unique festive design) in the wrong order. Santa, being
 * the perfectionist he is, wants the pancakes sorted perfectly from smallest to largest by size, starting from the
 * top of the stack.
 * <p>
 * If you want Christmas to run like clockwork, you can help the elves by flipping a stack of pancakes from the top
 * to a certain position i (inclusive). However, Santa's patience is limited - the elves and you have decided that
 * the maximum number of times you can flip the stack is 3 times the number of pancakes.
 * <p>
 * To help the elves, you must provide the list of flips that need to be performed to sort the pancakes correctly.
 * <p>
 * Input:
 *     * An array of n strictly positive integers, each representing the size of the pancakes.
 * <p>
 * Output:
 *     * A list of integers representing the position i for each flip that needs to be performed to order the array
 *       correctly. If no flips are required, an empty list is returned.
 * <p>
 * For example:
 *     * Input: [3, 1, 2, 5, 4]
 *     * Output: [3, 4, 3, 1]
 * <p>
 *     * Explanation:
 *         * After the first flip (3), reverse the subarray from index 0 to index 3 (inclusive):
 *             Original: [3, 1, 2, 5, 4] -> After the flip: [5, 2, 1, 3, 4]
 *         * After the second flip (4), reverse the subarray from index 0 to index 4 (inclusive):
 *             Original: [5, 2, 1, 3, 4] -> After the flip: [4, 3, 1, 2, 5]
 *         * After the third flip (3), reverse the subarray from index 0 to index 3 (inclusive):
 *             Original: [4, 3, 1, 2, 5] -> After the flip: [2, 1, 3, 4, 5]
 *         * After the fourth and final flip (1), reverse the subarray from index 0 to index 1 (inclusive):
 *             Original: [2, 1, 3, 4, 5] -> After the flip: [1, 2, 3, 4, 5]
 */
public class PancakeSorting {

    /**
     * Reverses the order of elements in the array from the start up to a specified position to.
     *
     * @param array The array on which the operation is performed.
     * @param to The position up to which the array should be flipped. This position is inclusive, meaning the subarray
     *           from index 0 to index to will be reversed. If to is not a valid position in the array, the function
     *           throws an IndexOutOfBoundsException().
     */
    private static void flip(int[] array, int to) {
        if (to < 0 || to > array.length - 1) {
            throw new IndexOutOfBoundsException("To position is out of bounds");
        }
        for (int i = 0; i < (to + 1) / 2; i++) {
            int t = array[i];
            array[i] = array[to - i];
            array[to - i] = t;
        }
    }

    /**
     * Sorts the array of pancakes using a series of flips. It returns an ordered list of positions where each flip was
     * performed.
     * @param array The series of pancakes that must be sorted.
     * @return An array of integers representing the positions where flips were performed to sort the array. If no
     *         flips are required, an empty list is returned.
     */
    public static int[] sort(int[] array) {
        System.out.println("input :  "+Arrays.toString(array));
        int[] target = new int[array.length];
        System.arraycopy(array, 0, target, 0, array.length);
        Arrays.sort(target);
        System.out.println("target : "+Arrays.toString(target));
        // TODO
        List<Integer> ret = new ArrayList<>();
        // 1. find largest unsorted
        //int index_largest_sorted = getIndexLargestSorted(array, target);
        while (getIndexLargestSorted(array, target) != 0) {
            int index_largest_unsorted = getIndexLargestUnsorted(array, target);
            // 2. if largest is at 0 => flip to before 1st sorted || else flip to index of largest first
            if (index_largest_unsorted == 0) {
                ret.add(getIndexLargestSorted(array, target));
                flip(array, getIndexLargestSorted(array, target));
                System.out.println(Arrays.toString(array));
                System.out.println(ret);
            }
            else {
                ret.add(index_largest_unsorted);
                flip(array, index_largest_unsorted);
                System.out.println(Arrays.toString(array));
                System.out.println(ret);
            }
        }
        int[] ret_table = new int[ret.size()];
        for (int i = 0; i < ret.size(); i++) ret_table[i] = ret.get(i);
        return ret_table;
    }

    private static int getIndexLargestSorted(int[] a, int[] target) {
        for (int i = a.length-1; i >= 0; i--) {
            if (a[i] != target[i]) {
                System.out.println("largest sorted at "+i);
                return i;
            }
        }
        return 0;
    }

    private static int getIndexLargestUnsorted(int[] a, int[] target) {
        int upper = getIndexLargestSorted(a, target);
        int max = Integer.MIN_VALUE;
        int max_i = a.length;
        for (int i = 0; i < upper; i++) {
            if (a[i] > max) {
                max = a[i];
                max_i = i;
            }
        }
        System.out.println("largest unsorted : "+max+" at "+max_i);
        return max_i;
    }

}

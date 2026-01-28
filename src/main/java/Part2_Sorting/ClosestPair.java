package Part2_Sorting;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * Let a be an array of integers.
 * In this exercise we are interested in finding
 * the two entries i and j such that a[i] + a[j] is the closest from a target x.
 * In other words, there are no entries k,l such that |x - (a[i] + a[j])| > |x - (a[k] + a[l])|.
 * Note that we can have i = j.
 * Your program must return the values a[i] and a[j].
 * <p>
 * For example let a = [5, 10, 1, 75, 150, 151, 155, 18, 75, 50, 30]
 * <p>
 * then for x = 20, your program must return the array [10, 10],
 *      for x = 153 you must return [1, 151] and
 *      for x = 170 you must return [18, 151]
 */
public class ClosestPair {

    /**
      * Finds the pair of integers which sums up to the closest value of x
      * @param array the array in which the value are looked for
      * @param target the target value for the sum
      */
    public static int[] closestPair(int[] array, int target) {
        System.out.println("target: "+target);
        int n = array.length;
        Arrays.sort(array);
        //System.out.println(Arrays.toString(a));
        // [1, 5, 10, 18, 30, 50, 75, 75, 150, 151, 155]
        // TODO: BinarySearch
        int best0 = array[0];
        int best1 = array[0];
        int best_diff = Math.abs(target - (best0 + best1));

        int left = 0;
        int right = n-1;
        while (left <= right) {
            int current_sum = array[left] + array[right];
            int current_diff = Math.abs(target - current_sum);
            // update best_diff if better
            if (current_diff < best_diff) {
                best_diff = current_diff;
                best0 = array[left];
                best1 = array[right];
            }
            if (current_sum > target) { // lower right limit
                right--;
            }
            else if (current_sum < target) { // up left limit
                left++;
            }
            else { // BINGO !!! DING DING DING
                return new int[]{array[left], array[right]};
            }
        }
        // if no match was possible return the ones that resulted in the smallest difference
        return new int[]{best0, best1};
    }

}

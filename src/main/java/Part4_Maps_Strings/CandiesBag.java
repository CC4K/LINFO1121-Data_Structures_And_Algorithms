package Part4_Maps_Strings;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Santa’s elves are hard at work preparing gift bags filled with candies for children. Each candy has a specific
 * sweetness level. To ensure the children enjoy their treats while staying healthy, Santa has set a strict rule: every
 * bag must have a total sweetness of exactly k.
 * <p>
 * The elves want to make the most of the candies they have, so they aim to maximize the number of candies in each bag
 * while following Santa’s rule. However, there’s a catch: only a contiguous selection of candies from the factory’s
 * production line can be used to prepare each bag.
 * <p>
 * One of the elves in charge of the candy-packing operation provides you with a list of candies from a production line
 * of the factory, along with their sweetness values. Can you help the elves find the size of the largest possible candy
 * bag they can prepare from this line with a total sweetness of k?
 * <p>
 * Input:
 *     * An array A of n integers, where each integer represents the sweetness value of a candy.
 *     * An integer k, the required total sweetness.
 * <p>
 * Output:
 *     * A single integer representing the maximum number of candies with a total sweetness of k that are contiguous in
 *       the input array.
 * <p>
 * For example:
 *     * Input: array = [1, -1, 5, -2, 3], k = 3
 *     * Output: 4 (the subarray [1, -1, 5, -2] has a sum of 3 and length 4)
 * <p>
 *     * Input: array = [-2, -1, 2, 1], k = 1
 *     * Output: 2 (the subarray [-1, 2] has a sum of 1 and length 2)
 * <p>
 * Expected Time-Complexity: O(N) (N being the size of the array)
 * <p>
 * Hint:
 *     * To find the longest subarray with a given sum efficiently, think about using a prefix sum. As you compute a
 *       running sum while traversing the array, consider how you can quickly check if any prior prefix sum you
 *       encountered would help you find a contiguous subarray with the desired sum k.
 */
public class CandiesBag {

    /**
     * Computes the maximum number of candies with a total sweetness of k that are contiguous in the input array.
     * @param array The array of sweetness values
     * @param k The required total sweetness
     * @return The maximum number of candies with a total sweetness of k that are contiguous in the input array.
     */
    public static int findMaximumSize(int[] array, int k) {
        System.out.println(Arrays.toString(array)+" "+k);
        // TODO
        Map<Integer, Integer> map = new HashMap<>(); // <current sweetness, current length>
        map.put(0, 0);
        int current_sweetness = 0;
        int current_length_section = 0;
        for (int i = 0; i < array.length; i++) {
            current_sweetness += array[i];
            // add <current sweetness, current length> if not already recorded
            if (!map.containsKey(current_sweetness)) {
                map.put(current_sweetness, i+1);
            }
            // look in subarray between i and j <==> at sweetness - target_sweetness
            if (map.containsKey(current_sweetness - k)) {
                System.out.println(map+" "+current_length_section+" "+current_sweetness+" "+i);
                //                                                       current_index - previous_index_exclusive
                current_length_section = Math.max(current_length_section, i - map.get(current_sweetness - k) + 1);
            }
        }
        System.out.println(map.entrySet());
        System.out.println(current_sweetness);
        System.out.println(current_length_section);

        return current_length_section;
    }

}

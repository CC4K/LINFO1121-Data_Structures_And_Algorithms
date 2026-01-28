package Part2_Sorting;

import java.util.Arrays;

/**
 * Radix sort implementation.
 * Complete the code to pass the test on positive numbers.
 * You can assume that all numbers are non-negative as a first step.
 * <p>
 * As a second step, adapt the code to handle negative numbers.
 * You can also add tests for negative numbers.
 * Remind that negative numbers use the two's complement representation.
 * For example, the number -3 is represented in 4 bits as follows:
 *    1. Positive binary representation of 3: 0011
 *    2. Invert the bits: 1100
 *    3. Add 1: 1100 + 0001 = 1101
 * Thus, -3 in 4-bit two's complement is represented as 1101.
 * <p>
 * What is the time complexity of your algorithm?
 * How does it compare in practice to the other sorting algorithms?
 *
 * @author Pierre Schaus and Harold Kiossous
 */
public class RadixSort {

    // Radix Sort method (we assume all numbers are non-negative)
    public static void sort(int[] A) {
        int maxBits = getMaxBits(A);
        System.out.println("largest bitsize : "+maxBits);
        int[] aux = new int[A.length];
        for (int i = 0; i < maxBits; i++) {
            stableSortOnBit(A, i, aux);
        }
    }

    /**
     * Stable Sort the array A based on the i-th bit
     * In order to have the best time complexity, your algorithm should run in O(n)
     * where n is the size of the array A.
     * @param A the array to sort based on the i-th bit
     * @param bitPosition
     * @param aux is an auxiliary array of the same size as A that you can use in your algorithm
     */
    private static void stableSortOnBit(int[] A, int bitPosition, int[] aux) {
        // TODO:
        //     [000 0101, 000 1010, 000 0001, 100 1011, 000 0000, 000 0010]
        //  => [000 0000, 000 0001, 000 0010, 000 0101, 000 1010, 100 1011]
        System.out.println("current: "+Arrays.toString(A)+" at bit: "+bitPosition);
        int N = A.length;
        // start counting all the "1" and "0" at position for all numbers in array (N - count_1 = count_0)
        int count_1 = 0;
        for (int i = 0; i < N; i++) {
            int bit = getBit(A[i], bitPosition);
            System.out.println(A[i]+" at "+bitPosition+" : "+bit);
            if (bit == 1) count_1++;
        }
        int count_0 = N - count_1; // get nb of "0" from nb of "1"
        System.out.println(count_0+" "+count_1); // 0: 3 "0" and 3 "1"

        // if bit 0 => aux[0, count_0 - 1][count_0, N-1]aux <= if bit 1
        count_1 += count_0; // fix offset in aux
        for (int i = N-1; i >= 0; i--) {
            int bit = getBit(A[i], bitPosition);
            if (bit == 1) {
                count_1--;
                aux[count_1] = A[i];
            }
            else if (bit == 0) {
                count_0--;
                aux[count_0] = A[i];
            }
        }
        System.out.println(Arrays.toString(aux));
        System.arraycopy(aux, 0, A, 0, aux.length);
    }

    // Helper method to get the bit at the given position FROM THE RIGHT
    // For example, getBit(5, 0) returns 1
    //              getBit(5, 1) returns 0
    //              getBit(5, 2) returns 1
    //              getBit(5, 3) returns 0
    private static int getBit(int number, int bitPosition) {
        return (number >> bitPosition) & 1;
    }

    // Helper Method to find the maximum number of bits required
    private static int getMaxBits(int[] A) {
        int max = 0;
        for (int num : A) {
            max = Math.max(max, num);
        }
        return 32 - Integer.numberOfLeadingZeros(max);
    }

    public static void main(String[] args) {
        int[] arr = new int[]{5, 10, 1, 75, 0, 2};
        sort(arr);
        System.out.println("final sorted: "+Arrays.toString(arr)); // [0, 1, 2, 5, 10, 75]
    }

}

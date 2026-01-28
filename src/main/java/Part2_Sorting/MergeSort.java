package Part2_Sorting;

import java.util.Arrays;

/**
 * Author Pierre Schaus
 *
 * Complete the Merge Sort (top-down) algorithm below making use
 * of the provided merge method.
 * You are not allowed to use imports or other external classes of Java.
 * Hint: Merger Sort this is a divide and conquer algorithm.
 *       It is easier to implement it recursively.
 *       As an alternative exercise, you can try to implement it
 *       non recursively, using a loop instead.
 */
public class MergeSort {
    /**
     * Pre-conditions: a[lo..mid] and a[mid+1..hi] are sorted
     * Post-conditions: a[lo..hi] is sorted
     */
    private static void merge(Comparable[] array, Comparable[] aux, int lo, int mid, int hi) {
        // Merge a[lo..mid] with a[mid+1..hi]
        // Copy a[lo..hi] to aux[lo..hi]
        for (int k = lo; k <= hi; k++) {
            aux[k] = array[k];
        }
        int i = lo;
        int j = mid + 1;
        // Merge back to a[lo..hi]
        for (int k = lo; k <= hi; k++) {
            if (i > mid)                            array[k] = aux[j++];
            else if (j > hi)                        array[k] = aux[i++];
            else if (aux[j].compareTo(aux[i]) < 0)  array[k] = aux[j++];
            else                                    array[k] = aux[i++];
        }
    }

    // Mergesort a[lo..hi] using auxiliary array aux[lo..hi]
    private static void mergeSort(Comparable[] array, Comparable[] aux, int lo, int hi) {
        // Sort a[lo..hi]
        if (lo >= hi) return;
        int mid = lo + (hi - lo)/2;
        mergeSort(array, aux, lo, mid);          // Sort left half
        mergeSort(array, aux, mid+1, hi);    // Sort right half
        merge(array, aux, lo, mid, hi);         // Merge results
    }

    /**
     * Rearranges the array in ascending order, using the natural order
     */
    public static void sort(Comparable[] array) {
        Comparable[] aux = new Comparable[array.length];
        mergeSort(array, aux, 0, array.length - 1);
    }

    public static void main(String[] args) {
        Integer[] myarr = new Integer[]{9, 2, 5, 3, 7, 0};
        System.out.println(Arrays.toString(myarr));
        sort(myarr);
        System.out.println(Arrays.toString(myarr));
    }
}


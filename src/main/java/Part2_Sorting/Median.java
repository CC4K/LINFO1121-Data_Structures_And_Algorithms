package Part2_Sorting;

/**
 * Author Pierre Schaus
 *
 * We give you the API of a Vector class allowing to access,
 * modify and exchange two elements in constant time.
 * Your task is to implement a method to calculate the median of a Vector.
 *
 * public interface Vector {
 *     // size of the vector
 *     public int size();
 *     // set the value v to the index i of the vector
 *     public void set(int i, int v);
 *     // returns the value at index i of the vector
 *     public int get(int i);
 *     // exchanges the values at positions i and j
 *     public void swap(int i, int j);
 * }
 * You must implement a method that has the following signature:
 * public static int median(Vector a, int lo, int hi)
 *
 * This method calculates the median of vector "a" between the positions "lo" and "hi" (included).
 * You can consider that the vector "a" has always an odd size.
 * To help you, an IntelliJ project with a minimalist test to check if your code computes the right median value is given.
 * Indeed, it is not advised to debug your code via Inginious.
 * Warning It is not forbidden to modify or swap elements of the vector "a" during the calculation (with the get/set/swap methods).
 * It is forbidden to call other methods of the standard Java library. It is also forbidden to make a "new".
 *
 * The evaluation is based on 10 points:
 *  - good return value: 3 points,
 *  - good return value and complexity O(n log n): 5 points,
 *  - good return value and complexity O(n) expected (average case on a random uniform distribution): 10 points.
 *
 *  All the code you write in the text field will be substituted in the place indicated below.
 *  You are free to implement other methods to help you in this class, but the method "median" given above must at least be included.
 */
public class Median {

    /**
     * Returns the median value of the vector between two indices
     *
     * @param vec the vector
     * @param lo the lowest index from which the median is computed
     * @param hi the highest index from which the median is computed
     */
    public static int median(Vector vec, int lo, int hi) {
        // TODO: IDEALLY SHUFFLE BEFORE
        // step 1 : sort
        quickSort(vec, lo, hi);
        for (int i = 0; i < vec.size(); i++) {
            System.out.print(vec.get(i)+", ");
        }
        System.out.println();
        // step 2 : return median
        return vec.get(vec.size() / 2);
    }

    private static void quickSort(Vector vec, int lo, int hi) {
        if (lo < hi) {
            int partition_index = partition(vec, lo, hi);
            quickSort(vec, lo, partition_index - 1); // recursion calls for smaller elements
            // TODO: NO NEED SECOND ONE HERE BECAUSE WE NEED ONLY FIRST HALF
            quickSort(vec, partition_index + 1, hi); // recursion calls for greater elements
        }
    }

    private static int partition(Vector vec, int lo, int hi) {
        // left and right scan indices
        int left = lo;
        int right = hi + 1;
        int pivot = vec.get(lo);
        while (true) {
            // Scan right, scan left, check for scan complete, and exchange
            while (vec.get(++left) < pivot) if (left == hi) break;
            while (pivot < vec.get(--right)) if (right == lo) break;
            if (left >= right) break;
            vec.swap(left, right);
        }
        vec.swap(lo, right); // Put pivot = a[j] into position
        return right; // with a[lo..j-1] <= a[j] <= a[j+1..hi].
    }

    // Version GeeksForGeeks
    /*
    private static int partition(Vector vec, int lo, int hi) {
        int pivot = vec.get(hi); // choose the pivot
        int i = lo - 1; // index of smaller element and indicates the right position of pivot found so far
        // Traverse arr[low..high] and move all smaller elements to the left side
        // Elements from low to i are smaller after every iteration
        for (int j = lo; j <= hi - 1; j++) {
            if (vec.get(j) < pivot) {
                i++;
                vec.swap(i, j);
            }
        }
        // Move pivot after smaller elements and return its position/index
        vec.swap(i + 1, hi);
        return i + 1;
    }
    */

    public static void main(String[] args) {
        Vector myVector = new Vector(7);
        myVector.set(0, 8);
        myVector.set(1, 0);
        myVector.set(2, 4);
        myVector.set(3, 7);
        myVector.set(4, 10);
        myVector.set(5, 1);
        myVector.set(6, 3);
        for (int i = 0; i < myVector.size(); i++) {
            System.out.print(myVector.get(i)+", ");
        }
        System.out.println(); // 0, 1, 3, 4, 7, 8, 10
        System.out.println(median(myVector, 0, myVector.size()-1)); // 4
    }

    public static class Vector {

        private int [] array;
        private int nOp = 0;


        public Vector(int n) {
            array = new int[n];
        }

        /**
         * Returns the size of the vector
         */
        public int size() {
            return array.length;
        }

        /**
         * Set the index in the vector to the given value
         *
         * @param i the index of the vector
         * @param v the value to set
         */
        public void set(int i, int v) {
            nOp++;
            array[i] = v;
        }

        /**
         * Returns the value at the given index
         *
         * @param i The index from which to retrieve the value
         */
        public int get(int i) {
            nOp++;
            return array[i];
        }

        /**
         * Exchanges elements in the array
         *
         * @param i the first index to swap
         * @param j the second index to swap
         */
        public void swap(int i, int j) {
            nOp++;
            int tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }

        /**
         * Returns the number of operation that has been made
         */
        public int nOp() {
            return nOp;
        }
    }

}

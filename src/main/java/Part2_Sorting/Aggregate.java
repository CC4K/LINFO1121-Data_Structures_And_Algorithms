package Part2_Sorting;

import java.util.*;

/**
 * Your task involves creating an `aggregate` function.
 * It takes as input a 2D array of integer, partitions the lines depending on the value of a chosen column,
 * and return a new 2D array of integer with one line per partition.
 * <p>
 * When merging multiple rows into a single one,
 * multiple values in each column need to be combined into a single value.
 * Various options can be considered for this merge, such as calculating the mean, median, etc.
 * However, in this particular exercise, we specifically request the implementation of the `mode` function (most frequent value).
 * In this context, for each column, the selected value is the one that appears most frequently within the group.
 * <p>
 * ! In a group, if two different values has the same number of occurrences, the smallest one is chosen.
 * ! The groups must be sorted in ascending order.
 * ! The order of the columns must not change.
 * <p>
 * Consider this example, where an array is aggregated following its first column.
 *                {{1, 4, 6},
 *                 {2, 1, 4},              {{1, 4, 7},
 *                 {2, 2, 4},       ->      {2, 1, 4},
 *                 {1, 4, 7},               {3, 3, 5}}
 *                 {3, 3, 5},
 *                 {1, 5, 7}}
 *                Input array             Aggregated array
 * <p>
 * In this example, there are 3 groups, for the three values in the first column; "1", "2", and "3".
 * The group "1" looks like   {{1, 4, 6},
 *                             {1, 4, 7},
 *                             {1, 5, 7}}
 * <p>
 * For the second column, 4 appears twice and 5 once. Hence, the mode of this column is 4.
 * The mode of the third column is 7. When aggregated, this group will become {1, 4, 7}.
 * <p>
 * For the second column of the group "2", both "1" and "2" are present one time.
 * As 1 < 2, "1" is chosen.
 * <p>
 * The aggregate function should execute in O(n.m + n.log(n)) where n is the number of rows and m the number of columns.
 * The mode function should execute in O(n) where n is the number of rows.
 * <p>
 * Debug your code on the small examples in the test suite.
 */
public class Aggregate {

    /**
     * Compute the element in an array that occurs most frequently within a specified range.
     * If there is a tie, the smallest value is returned.
     * The subset of values to consider in the array is determined by three parameters: the column to consider,
     * and the indexes of the first and last lines to consider.
     * <p>
     * Your method should execute in O(n) where n is the number of lines to consider.
     *
     * @param array a 2D array
     * @param from the first line to consider
     * @param to the last line to consider
     * @param column the column to consider
     * @return the value with the most occurrences
     * <p>
     * Example:
     * Consider a 2D array:
     * {
     *     {1, 5, 3},
     *     {4, 5, 2},
     *     {6, 2, 8}
     *     {9, 2, 2}
     *     {1, 5, 3}
     * }
     * Calling mode(array, 1, 4, 1) will analyze the second column (indexes 1 to 4).
     * The values considered in this column are thus: 5, 2, 2, 5.
     * The method returns 2 since it appears most frequently (2 times) in the specified range.
     * There is a tie between 2 and 5, but 2 is smaller.
     */
    public static int mode(int[][] array, int from, int to, int column) {
        Map<Integer, Integer> occurrences = new HashMap<>(); // <value, count>
        int best_key = -1;
        int best_val_occur = -1;
        for (int i = from; i <= to; i++) {
            int current_key = array[i][column];
            int current_val_occur = occurrences.merge(current_key, 1, (x, y) -> x + y);
            //System.out.println(current_key+" : "+current_val_occur);
            if (current_val_occur > best_val_occur || (current_val_occur == best_val_occur && current_key < best_key)) {
                best_key = current_key;
                best_val_occur = current_val_occur;
            }
        }
        //System.out.println(best_key+" "+best_val_occur); // 5 3
        return best_key;
    }

    /**
     * Aggregates values in a 2D integer array based on the value of a specified column.
     * This method partitions the array into groups, where each group consists of rows that have the same value
     * in the specified column. Within each group, it calculates the mode (most frequent value) for each column.
     * If there is a tie for the most frequent value, the smallest value is chosen. The resulting array contains
     * one row for each group, with the mode value for each column. The groups are sorted in ascending order based
     * on the values in the specified column.
     *
     * @param input The 2D integer array to aggregate.
     * @param column The column of the column based on which the array is partitioned into groups.
     * @return A new 2D integer array where each row represents an aggregated group.
     *
     * Example: See above and see unit tests.
     */
    public static int[][] aggregate(int[][] input, int column) {
        printMatrix(input);
        // TODO 1: sort matrix by the given column
        Arrays.sort(input, (x, y) -> x[column] - y[column]);
        printMatrix(input);
        // TODO 2: for each different value of the column, find mode of every other column
        List<int[]> aggregate = new ArrayList<>(); // list of all [group_id, mode, mode]
        int init_group_id = input[0][column]; // start with group at top of matrix
        int line_top_group = 0;
        for (int i = 1; i < input.length; i++) {
            // go down and when group_id change, aggregate the top ones
            int current_group_id = input[i][column];
            if (init_group_id != current_group_id) {
                //System.out.println("init_group_id: "+init_group_id+" from: "+line_top_group+" to: "+(i-1)+" col: "+column);
                aggregate.add(aggregateAux(input, line_top_group, i-1, column));
                init_group_id = current_group_id;
                line_top_group = i;
            }
        }
        // +1 iteration
        aggregate.add(aggregateAux(input, line_top_group, input.length-1, column));
        return aggregate.toArray(new int[0][]);
    }

    private static int[] aggregateAux(int[][] input, int from, int to, int column) {
        int[] ret = new int[input[0].length];
        for (int i = 0; i < input[0].length; i++) { // iterate on each column
            ret[i] = mode(input, from, to, i);
        }
        System.out.println(Arrays.toString(ret));
        return ret;
    }

    private static void printMatrix(int[][] matrix) {
        for (int[] line : matrix) {
            System.out.println(Arrays.toString(line));
        }
        System.out.println();
    }

}


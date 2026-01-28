package Exam_training;

import java.util.Arrays;

/**
 * Chewbacca is trying to help C-3PO recover from his latest... unfortunate disassembly. After inspecting the piles of
 * parts, Chewbacca realizes that many are beyond repair and that he will need to purchase new components from the droid
 * market.
 * <p>
 * C-3PO, in great distress, explains that only the pieces directly connected to his core are essential, those define
 * what he is. But all other parts can safely be replaced with new ones.
 * <p>
 * Using C-3PO’s blueprint and knowing the typical cost of replacement parts, Chewbacca wants to estimate the total cost
 * for this repair. He asks you to compute the sum of all parts that can be replaced, those that are not connected to the core.
 * <pre>
 * Inputs:
 *     · blueprint : the blueprint of C-3PO represented as an adjacency list.
 *                   blueprint[i] lists all pieces directly dependent on piece i.
 *     · core      : an array of indices of the pieces that are part of C-3PO’s core.
 *     · prices    : an array of prices, where prices[i] is the cost of piece i.
 *
 * Output:
 *     · The total replacement cost: the sum of prices for all pieces not reachable/dependent from any core piece.
 *
 * Example:
 *     · Inputs: - blueprint = {
 *                   { 1 },
 *                   { 2 },
 *                   { 3 },
 *                   { 1 },
 *                   { 2 },
 *                   { 3 }
 *                 }
 *               - core = { 0, 5 }
 *               - prices = { 4, 5, 2, 7, 6, 3 }
 *     · Output: 6
 *     · Explanation:
 *
 *         The blueprint represents the dependency graph:
 *
 *                               4
 *                               |
 *                               v
 *         0 -------> 1 -------> 2
 *                    ^          |
 *                    |          |
 *         5 -------> 3 <--------|
 *
 *         Pieces 0 and 5 are core pieces, and all pieces reachable from them (1, 2, 3) must be preserved.
 *         Only piece 4 is not reachable from any core piece and can be safely replaced. Its cost is 6.
 * </pre>
 * Expected Time Complexity: O(N), where N is the number of pieces of C-3PO.
 * <p>
 * Hint: Try to think about which parts C-3PO definitely needs, starting from the core, before deciding which ones can be replaced.
 */
public class UnfortunateDisassembly {

    /**
     * Computes the total cost of all pieces that can be safely replaced.
     * @param blueprint adjacency list representing dependencies between pieces
     * @param core      indices of core pieces that must be preserved
     * @param prices    array of prices for each piece
     * @return sum of prices of all pieces not reachable from any core piece
     */
    public static int repairCost(int[][] blueprint, int[] core, int[] prices) {
        int N = prices.length;
        // TODO
        boolean[] marked = new boolean[N];
        for (int part : core) {
            dfs(blueprint, part, marked);
            System.out.println(Arrays.toString(marked));
        }
        int repairCost = 0;
        for (int i = 0; i < N; i++) {
            if (!marked[i]) repairCost += prices[i];
        }
        System.out.println(repairCost);
        return repairCost;
    }
    private static void dfs(int[][] blueprint, int id, boolean[] marked) {
        marked[id] = true;
        // check neighbours
        for (int neighbour : blueprint[id]) {
            if (!marked[neighbour]) {
                marked[neighbour] = true;
                dfs(blueprint, neighbour, marked);
            }
        }
    }
}

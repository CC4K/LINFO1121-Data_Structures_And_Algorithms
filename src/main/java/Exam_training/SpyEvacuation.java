package Exam_training;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * A Rebel spy stationed on an Imperial desert moon has just sent a distress signal: their cover is blown, and an
 * extraction must be planned immediately. Cassian Andor is coordinating the rescue, but before he can send a ship,
 * he needs to assess the threat level around the spy’s location.
 * <p>
 * Imperial bases are scattered across the moon and represented as points on a 2D map. If the extraction turns noisy,
 * the closest Imperial outposts will be the first to dispatch reinforcements. To avoid walking into an ambush,
 * Cassian needs to know exactly which Imperial installations lie nearest to the spy.
 * <p>
 * Your task is to help him by computing the K nearest bases to the spy’s position using Euclidean distance.
 * <pre>
 * Inputs:
 *     · target : a 2D point { x, y } giving the spy’s location.
 *     · bases  : an array of 2D points, where bases[i] = { x_i, y_i } is the position of the i-th Imperial base.
 *     · k      : the number of nearest bases Cassian wants to retrieve (k > 1).
 *
 * Output:
 *     · An array of k indices corresponding to the K bases closest to the target point. The order of these indices
 *       does not matter.
 *
 * Example:
 *     · Inputs: - target = { 0.0, 0.0 }
 *               - bases  = {
 *                     {  1.0,  1.0 },   // distance = 1.414...
 *                     { -1.0,  0.0 },   // distance = 1.0
 *                     {  3.0,  4.0 },   // distance = 5.0
 *                     {  0.5, -0.5 }    // distance = 0.707...
 *                 }
 *               - k = 2
 *     · Output: [3, 1]   // order not important
 * </pre>
 * Expected Time Complexity: O(N * log(k)), where N is the number of bases and k is k.
 * <p>
 * Hint: While solving the problem, you don’t need to keep all distances.
 *       Try keeping only the best k you’ve seen so far.
 */
public class SpyEvacuation {

    /**
     * Returns the indices of the k bases closest to the given target point.
     * @param target a 2D point {x, y} representing the spy’s location
     * @param bases  an array of 2D points, where bases[i] = {x_i, y_i}
     * @param k      the number of nearest bases to retrieve (k > 0)
     * @return       an array of k indices of the bases with the smallest Euclidean distances to target
     */
    public static int[] kClosestBases(double[] target, double[][] bases, int k) {
        // TODO: keep an ordered PQ of worst distances first to remove the worst ones when full
        PriorityQueue<DistIdx> distances = new PriorityQueue<>(Comparator.comparingDouble(d -> -d.dist));
        for (int baseIdx = 0; baseIdx < bases.length; baseIdx++) {
            double newDist = dist(bases[baseIdx], target);
            if (distances.size() == k) {
                // compare new dist with worst dist
                double worstDist = distances.peek().dist;
                if (worstDist > newDist) {
                    distances.poll(); // remove the awful old dist
                }
                else if (newDist > worstDist) {
                    continue; // new dist is awful and not worth adding
                }
            }
            distances.add(new DistIdx(baseIdx, newDist));
            System.out.println(distances+" "+distances.size());
        }
        //System.out.println(distances); // [(1=1,000000), (3=0,707107)] => only the best distances are left in PQ

        int[] kClosest = new int[k];
        for (int i = 0; i < k; i++) {
            kClosest[i] = distances.poll().idx;
        }
        return kClosest;
    }
    private static double dist(double[] p1, double[] p2) {
        double xDif = (p1[0] - p2[0]);
        double yDif = (p1[1] - p2[1]);
        return Math.sqrt((xDif*xDif) + (yDif*yDif));
    }

    static class DistIdx {
        int idx;
        double dist;
        public DistIdx(int idx, double dist) {
            this.idx = idx;
            this.dist = dist;
        }
        public String toString() {
            return String.format("(%d=%f)", idx, dist);
        }
    }
}

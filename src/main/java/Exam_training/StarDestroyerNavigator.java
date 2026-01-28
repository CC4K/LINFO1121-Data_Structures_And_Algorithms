package Exam_training;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Ahsoka has infiltrated an Imperial Star Destroyer in search of classified plans. Unfortunately, her
 * presence has been detected, and the ship has gone into full lockdown. To escape, Ahsoka must navigate a network of
 * corridors connecting the various compartments of the ship.
 * <p>
 * R2-D2 manages to hack into one of the ship’s security consoles, giving Ahsoka access to a complete map of the
 * Star Destroyer, including the defensive systems installed in each corridor.
 * <p>
 * Every hallway is equipped with a “Shrinking Beam Gun”, a device that reduces the size of any intruder passing
 * through it. Each gun has an intensity between 0.0 and 1.0, representing the fraction of the intruder’s current
 * size that remains after using that corridor.
 * For example, an intensity of 0.8 means Ahsoka keeps 80% of her size when traversing that hallway.
 * <p>
 * Ahsoka starts in a given room and must reach the escape pod. Each corridor she traverses multiplies her current
 * size by the intensity of that corridor. Since she would prefer not to be reduced to action-figure scale, her goal
 * is to follow the path that maximizes her final size.
 * <p>
 * Your task is to write a program that computes the greatest multiplicative size factor Ahsoka can achieve when
 * traveling from a source room to a target room.
 * <p>
 * Inputs:
 *     · The ship’s layout is represented as an adjacency list:
 *         - neighbors[u] is an array of the rooms directly reachable from room u.
 *     · A second array of identical shape stores the shrinking intensities:
 *         - intensities[u][k] is the intensity of the corridor from room u to neighbors[u][k].
 *     · A source room.
 *     · A target room.
 * <p>
 * Output:
 *     · A double value representing the maximum multiplicative factor of Ahsoka’s remaining size when reaching
 *       the escape pod from the source room.
 * <pre>
 * For example:
 *     · Inputs: - neighbours = {
 *                     { 1, 2 },
 *                     { 0, 2 },
 *                     { 0, 1, 3 },
 *                     { 2 }
 *                 }
 *               - intensities = {
 *                     { 0.9, 0.6 },
 *                     { 0.9, 0.8 },
 *                     { 0.6, 0.8, 0.3 },
 *                     { 0.3 }
 *                 }
 *               - source = 0
 *               - target = 3
 *     · Output: 0.216
 *     · Explanation:
 *
 *         neighbours and intensities represent the graph:
 *
 *               1
 *              / \
 *         0.9 /   \ 0.8
 *            /     \
 *           0 ----- 2 ----- 3
 *              0.6     0.3
 *
 *         The path going from 0 to 3 that has the maximum multiplicative factor is the path 0 -> 1 -> 2 -> 3 with a
 *         factor of 0.9 * 0.8 * 0.3 = 0.216
 * </pre>
 * Expected Time Complexity: O(E * log(V)), where E is the number of corridors and V is the number of rooms.
 * <p>
 * Hint: Thing about adapting a well-known algorithm.
 *       In your adaptation, the “best” next room to explore is the one that gives Ahsoka the largest size factor so far, not the smallest one.
 */
public class StarDestroyerNavigator {

    /**
     * Computes the maximum shrinking factor Ahsoka can keep when traveling from the source room to the target room.
     * Each corridor multiplies her current size by an intensity in [0, 1].
     * The goal is to find a path that maximizes the product of these intensities.
     * @param neighbours adjacency list of the ship: neighbors[u] lists rooms reachable from u
     * @param costs intensities[u][k] is the shrinking factor of the corridor u -> neighbors[u][k]
     * @param source starting room
     * @param target destination room
     * @return the maximum product of intensities along any path from source to target, or 0.0 if no such path exists
     */
    public static double navigate(int[][] neighbours, double[][] costs, int source, int target) {
        System.out.println(source+" => "+target);
        // TODO: Dijkstra but best neighbour is the one with the largest cost (to stay large) + for new cost => multiplication
        double[] largest = new double[costs.length];
        PriorityQueue<NodeCost> frontier = new PriorityQueue<>(Comparator.comparingDouble(n -> -n.cost));

        largest[source] = 1.0;
        frontier.add(new NodeCost(source, 1));

        while (!frontier.isEmpty()) {
            NodeCost current = frontier.poll();
            // check destination
            if (current.id == target) break;
            // check neighbours
            for (int j = 0; j < neighbours[current.id].length; j++) {
                int neighbour = neighbours[current.id][j];
                double new_cost = current.cost * costs[current.id][j];
                if (new_cost > largest[neighbour]) {
                    largest[neighbour] = new_cost;
                    frontier.add(new NodeCost(neighbour, new_cost));
                }
            }
        }
        System.out.printf("%.3f%n", largest[target]);

        return largest[target];
    }
    private static class NodeCost {
        int id;
        double cost;
        public NodeCost(int id, double cost) {
            this.id = id;
            this.cost = cost;
        }
        public String toString() {
            return String.format("(%d=%.3f)", id, cost);
        }
    }
}

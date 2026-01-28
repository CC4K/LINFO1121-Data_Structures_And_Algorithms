package Part6_Graphs;

import java.util.*;

/**
 * Given two integers start and end, your task is to determine the minimum number of operations required to transform
 * start into end. You may use the following operations:
 *     - Add 5 to the number
 *     - Subtract 7 to the number
 *     - Multiply the number by 2
 *     - Divide the number by 3 (integer division)
 */
public class IntegerTransformation {

    /**
     * Given two integers start and end, returns the minimum number of operations required to transform start into end.
     * @param start the initial integer.
     * @param target the target integer.
     * @return the minimum number of operations required to transform start into end.
     */
    public static int countSteps(int start, int target) {
        //System.out.println(start+" => "+target);
        // TODO: the stupidest BFS ever where every node has 4 neighbours (1 for each operation)
        //if (target == start) return 0;

        Map<Integer, Integer> distTo = new HashMap<>(); // <number, distance from start>
        Queue<Integer> frontier = new LinkedList<>();

        distTo.put(start, 0);
        frontier.add(start);

        while (!frontier.isEmpty()) {
            int current = frontier.poll();
            System.out.println(current);
            // objective reached
            if (distTo.containsKey(target)) break;
            // check neighbours
            for (int neighbour : new int[]{current + 5, current - 7, current * 2, current / 3}) {
                if (!distTo.containsKey(neighbour)) {
                    distTo.put(neighbour, distTo.get(current)+1);
                    frontier.add(neighbour);
                }
            }
        }

        return distTo.get(target);
    }

}

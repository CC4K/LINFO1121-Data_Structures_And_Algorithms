package Part6_Graphs;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * In case of an emergency at the Olympic games,
 * it’s crucial to ensure that all spectators can be evacuated efficiently.
 * <p>
 * Each venue is represented as a node in an undirected graph.
 * The pathways between them are represented as weighted edges.
 * A set of exits are also represented as nodes in the graph.
 * <p>
 * The goal is to determine the shortest path from each venue to the nearest exit.
 * <p>
 * To enable people to find the shortest path from any venue to the nearest exit,
 * we are going to place one directional arrow at each venue indicating the
 * next venue to follow on the shorted to reach the nearest exit from that node.
 * <p>
 * Hint: You can use Dijkstra's algorithm with minor adaptations (starting from the exits) to solve this problem.
 * <p>
 * The expected time-complexity if O((V + E) log V) where V is the number nodes and E is the number of edges.
 * <p>
 * Look at the test cases for more details on the input and output format as well as some examples.
 */
public class Evacuation {

    /**
     * @param graph the graph representing the venues (and exits), pathways
     *        The graph is represented as an adjacency matrix,
     *              where graph[i][j] is the weight of the edge between i and j.
     *              If there is no edge between i and j, graph[i][j] = 0.
     * @param exits the nodes of the graph representing the exits
     * @return an array of integers, where the i-th element is the index of the next venue to visit
     * to reach the nearest exit from the i-th venue. If the i-th venue is an exit, the value is -1.
     */
    public static int[] findShortestPaths(int[][] graph, int[] exits) {
        //for (int[] line : graph) System.out.println(Arrays.toString(line));
        //System.out.println(Arrays.toString(exits));
        // 3 -- 4
        // | \  |
        // |  \ |   Undirected Graph
        // 1    2
        //  \  /
        //   0
        // if exit = 2
        // {2, 0, -1, 2, 2} OR {2, 3, -1, 2, 2}
        // TODO: This is the fastest and cleanest reverse Dijkstra I have done => good for exam
        //  Note: If using arrays in PQ => Comparator in PQ !!!
        int n = graph.length;

        int[] distTo = new int[n]; Arrays.fill(distTo, Integer.MAX_VALUE);
        int[] edgeTo = new int[n]; // [path_to_closest_exit, path_to_closest_exit, path_to_closest_exit...]
        PriorityQueue<int[]> frontier = new PriorityQueue<>((l1, l2) -> l1[1] - l2[1]); // [id_start, distance]
        // no source => start from exits
        for (int exit : exits) frontier.add(new int[]{exit, 0});

        while (!frontier.isEmpty()) {
            int[] p = frontier.poll();
            int pId = p[0];
            int pCost = p[1];

            // check neighbours
            for (int neighbourId = 0; neighbourId < n; neighbourId++) {
                int neighbourCost = graph[pId][neighbourId];
                int updatedCost = pCost + neighbourCost;
                if ((neighbourCost != 0) && (updatedCost < distTo[neighbourId])) {
                    distTo[neighbourId] = updatedCost;
                    edgeTo[neighbourId] = pId;
                    frontier.add(new int[]{neighbourId, updatedCost});
                }
            }
        }
        for (int exit : exits) edgeTo[exit] = -1;
        //System.out.println(Arrays.toString(distTo));
        //System.out.println(Arrays.toString(pathTo));
        return edgeTo;
    }

}

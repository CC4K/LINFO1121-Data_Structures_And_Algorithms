package Part6_Graphs;

import java.util.*;

/**
 * Santa’s sleigh relies on a magical communication network of relay stations and bidirectional magic pathways. This
 * network ensures seamless coordination between the North Pole and Santa's team during the Christmas gift deliveries.
 * However, the network is fragile: certain pathways are critical, and their removal could disrupt the entire system by
 * splitting the network into disconnected parts.
 * <p>
 * Santa needs your help to identify all these critical pathways so that the elves can reinforce them before the big
 * night.
 * <p>
 * Given the network of relay stations and pathways, determine all the critical pathways whose removal would increase
 * the number of connected components in the network. At the start, the network is composed of a single connected
 * component.
 * <p>
 * Input:
 *     * A graph represented as an adjacency list. The adjacency list is stored as an array of HashSet where each set
 *       contains the ids of the nodes that are adjacent to the node.
 * <p>
 * Output:
 *     * A list of pairs (u,v) representing the critical pathways in the network. Since the graph is undirected, each
 *       edge appears twice in the adjacency list. However, you only need to return each edge once.
 * <p>
 * For example:
 * <pre>
 *     * Input: adj = [
 *                        (1, 2),
 *                        (0, 2, 3),
 *                        (0, 1, 5),
 *                        (1, 4),
 *                        (3),
 *                        (2)
 *                    ]
 *     * Output: [(2, 5), (1, 3), (3, 4)]
 *     * Explanation:
 *         adj represents the graph:
 *
 *                   2 ----- 5
 *                  / \
 *                 /   \
 *                0 --- 1 ----- 3 ----- 4
 *
 *         In this graph if the edge (2, 5) is removed then some nodes (5) are not connected to the remaining of the
 *         graph. It is also the same for the edges (1, 3) and (3, 4).
 * </pre>
 * Expected Time-Complexity: O(N^3) (N being the number of nodes)
 * <p>
 * Hint:
 *     * It would be inefficient to take into account all the edges in the graph. Since a spanning tree must cover all
 *       the nodes in a graph, every critical edge in the original graph will be part of any spanning tree. If we only
 *       consider these edges, deleting them one by one from the original graph could make it possible to determine
 *       which ones are critical.
 */
public class CriticalPathways {

    /**
     * Determines all the critical pathways whose removal would increase the number of components in the network.
     * @param adj A graph represented as an adjacency list. The adjacency list is stored as an array of HashSet where
     *            each set contains the ids of the nodes that are adjacent to the node.
     *
     * @return A list of pairs (u,v) representing the critical pathways in the network. Since the graph is undirected,
     *         each edge appears twice in the adjacency list. However, you only need to return each edge once.
     */
    public static int[][] findCriticalPathways(HashSet<Integer>[] adj) {
        //for (int i = 0; i < adj.length; i++) System.out.println(i+": "+adj[i]);
        // TODO: first create any spanning tree before looping on its edges
        UF uf = new UF(adj.length);
        Queue<int[]> mst = new LinkedList<>();
        for (int n1 = 0; n1 < adj.length; n1++) {
            for (int n2 : adj[n1]) {
                if (!uf.connected(n1, n2)) {
                    uf.union(n1, n2);
                    mst.add(new int[]{n1, n2});
                }
            }
        }
        System.out.print("MST: "); for (int[] e : mst) System.out.print(Arrays.toString(e)+" "); System.out.println(); // [(0,1), (0,2), (1,3), (2,5), (3,4)]
        // TODO: loop on all edge of MST and remove it from graph, check if still connected with a BFS, if so re-add to graph, else to output
        List<int[]> criticalEdges = new ArrayList<>();
        for (int[] edge : mst) {
            System.out.print(Arrays.toString(edge)+" => ");
            // remove edge
            adj[edge[0]].remove(edge[1]);
            adj[edge[1]].remove(edge[0]);
            // check connection
            if (!graphIsConnected(adj)) criticalEdges.add(edge);
            // re-add edge
            adj[edge[0]].add(edge[1]);
            adj[edge[1]].add(edge[0]);
        }
        return criticalEdges.toArray(new int[0][]);
    }

    private static boolean graphIsConnected(HashSet<Integer>[] adj) {
        // go through all nodes and mark them
        // if > 1 not marked => graph is disconnected
        // TODO: BFS
        Queue<Integer> frontier = new LinkedList<>();
        boolean[] marked = new boolean[adj.length];
        int n_marked = 0;

        frontier.add(0);
        marked[0] = true;
        n_marked++;

        while (!frontier.isEmpty()) {
            int node = frontier.poll();
            // check neighbours
            for (int neighbour : adj[node]) {
                if (!marked[neighbour]) {
                    marked[neighbour] = true;
                    n_marked++;
                    frontier.add(neighbour);
                }
            }
        }
        System.out.println(Arrays.toString(marked));
        return n_marked == marked.length;
    }

    static class UF {
        int[] id;
        int[] size;
        public UF(int n) {
            this.id = new int[n];
            this.size = new int[n];
            for (int i = 0; i < n; i++) {
                id[i] = i;
                size[i] = 1;
            }
        }
        public boolean connected(int p, int q) {
            return find(p) == find(q);
        }
        public int find(int p) {
            return (id[p] == p) ? p : find(id[p]);
        }
        public void union(int p, int q) {
            int pId = find(p);
            int qId = find(q);
            if (pId == qId) return; // already in same group
            // attach smallest group under largest group
            if (size[pId] < size[qId]) {
                id[pId] = qId;
                size[qId] += size[pId];
            }
            else { // if (size[qId] <= size[pId])
                id[qId] = pId;
                size[pId] += size[qId];
            }
        }
    }
}

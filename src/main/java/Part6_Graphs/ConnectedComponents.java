package Part6_Graphs;

import java.util.ArrayList;
import java.util.List;

/**
 * You are asked to implement the ConnectedComponent class given a graph.
 * The Graph class available in the code is the one of the Java class API.
 * <pre>
 * {@code
 * public class ConnectedComponents {
 *     public static int numberOfConnectedComponents(Graph g) {
 *         // TODO
 *         return 0;
 *     }
 * }
 * }
 * </pre>
 * Hint: Feel free to add methods or even inner-class (private static class)
 *       to help you but don't change the class name or the signature of the numberOfConnectedComponents method.
 */
public class ConnectedComponents {

    private static boolean[] marked;
    private static int[] groupId;
    private static int currentId;

    public ConnectedComponents() {
    }

    /**
     * @return the number of connected components in g
     */
    public static int numberOfConnectedComponents(Graph g) {
        // TODO
        groupId = new int[g.V()];
        marked = new boolean[g.V()];
        currentId = 0;

        for (int s = 0; s < g.V(); s++) {
            if (!marked[s]) { // different group
                dfs(g, s); // detect entire group
                currentId++; // iterate for next group
            }
        }
        //System.out.println(Arrays.toString(marked));
        //System.out.println(Arrays.toString(groupId));
        return currentId;
    }

    private static void dfs(Graph graph, int node) {
        marked[node] = true;
        groupId[node] = currentId;
        // check neighbours
        for (int neighbour : graph.adj(node)) {
            if (!marked[neighbour]) {
                dfs(graph, neighbour);
            }
        }
    }

    static class Graph {

        private List<Integer>[] edges;

        public Graph(int nbNodes) {
            this.edges = (ArrayList<Integer>[]) new ArrayList[nbNodes];
            for (int i = 0;i < edges.length;i++) edges[i] = new ArrayList<>();
        }

        /**
         * @return the number of vertices
         */
        public int V() {
            return edges.length;
        }

        /**
         * @return the number of edges
         */
        public int E() {
            int count = 0;
            for (List<Integer> bag : edges) count += bag.size();
            return count/2;
        }

        /**
         * Add edge v-w to this graph
         */
        public void addEdge(int v, int w) {
            edges[v].add(w);
            edges[w].add(v);
        }

        /**
         * @return the vertices adjacent to v
         */
        public Iterable<Integer> adj(int v) {
            return edges[v];
        }
    }

}

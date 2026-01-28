package Part6_Graphs;

import java.util.*;

/**
 * Consider this class, BreadthFirstShortestPaths, which computes the shortest path between
 * multiple node sources and any node in an undirected graph using a BFS path.
 * The Graph class is already implemented and here it is:
 * <pre>
 * {@code
 *     public class Graph {
 *         // @return the number of vertices
 *         public int V() { }
 *
 *         // @return the number of edges
 *         public int E() { }
 *
 *         // Add edge v-w to this graph
 *         public void addEdge(int v, int w) { }
 *
 *         // @return the vertices adjacent to v
 *         public Iterable<Integer> adj(int v) { }
 *
 *         // @return a string representation
 *         public String toString() { }
 *     }
 * }
 * </pre>
 * You are asked to implement all the TODOs of the BreadthFirstShortestPaths class.
 */
public class BreadthFirstShortestPaths {

    private static final int INFINITY = Integer.MAX_VALUE;
    private boolean[] marked; // marked[v] = is there an s-v path
    private int[] distTo;     // distTo[v] = number of edges shortest s-v path

    /**
     * Computes the shortest path between any
     * one of the sources and very other vertex
     *
     * @param G       the graph
     * @param sources the source vertices
     */
    public BreadthFirstShortestPaths(Graph G, Iterable<Integer> sources) {
        marked = new boolean[G.V()];
        distTo = new int[G.V()];
        for (int v = 0; v < G.V(); v++) distTo[v] = INFINITY;
        bfs(G, sources);
    }

    // Breadth-first search from multiple sources
    private void bfs(Graph G, Iterable<Integer> sources) {
        // TODO : normal BFS except like Wildfire : multiple src on frontier
        Queue<Integer> frontier = new LinkedList<>();
        for (int src : sources) {
            marked[src] = true;
            distTo[src] = 0;
            frontier.add(src);
        }
        while (!frontier.isEmpty()) {
            int current = frontier.poll();
            for (int neighbour : G.adj(current)) {
                if (!marked[neighbour] && distTo[neighbour] > distTo[current]) {
                    marked[neighbour] = true;
                    distTo[neighbour] = distTo[current] + 1;
                    frontier.add(neighbour);
                }
            }
        }
        //System.out.println(Arrays.toString(distTo));
    }

    /**
     * Is there a path between (at least one of) the sources and vertex v?
     *
     * @param v the vertex
     * @return true if there is a path, and false otherwise
     */
    public boolean hasPathTo(int v) {
        // TODO
        return marked[v];
    }

    /**
     * Returns the number of edges in a shortest path
     * between one of the sources and vertex v?
     *
     * @param v the vertex
     * @return the number of edges in a shortest path
     */
    public int distTo(int v) {
        // TODO
        return distTo[v];
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

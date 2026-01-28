package Part6_Graphs;

import java.util.*;

/**
 * Author: Alexis Englebert
 * Context: You are operating a power plant in the new city of Louvain-La-Neuve, but lack plans for the city's electrical network.
 * Your goal is to minimize the cost of electrical wires ensuring the city is connected with just one wire.
 * <p>
 * The method 'minimumSpanningTreeCost' is designed to find the minimum cost to connect all cities in a given electrical network.
 * The network is represented as a graph where the nodes are the buildings, the edges are the possible connections and their associated cost.
 * <p>
 * Example:
 * Given a network with three buildings (nodes) and the cost of wires (edges) between them:
 * 0---1 (5), 1---2 (10), 0---2 (20)
 * The minimum cost to connect all the buildings is 15 (5 + 10).
 * <p>
 * Note: The method assumes that the input graph is connected and the input is valid.
 */
public class Electricity {

    /**
     * @param n      The number of buildings (nodes) in the network.
     * @param edges  A 2D array where each row represents an edge in the form [building1, building2, cost].
     *               The edges are undirected so (building2, building1, cost) is equivalent to (building1, building2, cost).
     * @return       The minimum cost to connect all cities.
     */
    public static int minimumSpanningCost(int n, int[][] edges) {
        // TODO: Minimum Spanning Tree with Prim's Algorithm
        List<Edge>[] adj = new List[n];
        for (int i = 0; i < n; i++) adj[i] = new ArrayList<>();
        for (int[] edge : edges) {
            adj[edge[0]].add(new Edge(edge[0], edge[1], edge[2]));
            adj[edge[1]].add(new Edge(edge[1], edge[0], edge[2]));
        }
        //for (List<Edge> a : adj) System.out.println(a);

        Queue<Edge> mst = new LinkedList<>(); // MST
        boolean[] marked = new boolean[n];
        PriorityQueue<Edge> frontier = new PriorityQueue<>((n1, n2) -> n1.cost - n2.cost);

        marked[0] = true;
        for (Edge nextEdge : adj[0]) frontier.add(nextEdge);
        int minCost = 0;

        while (!frontier.isEmpty()) {
            Edge edge = frontier.poll();
            // skip loop if both already marked
            if (marked[edge.dest]) continue;
            // add to MSTree
            marked[edge.dest] = true;
            minCost += edge.cost; // update cost
            mst.add(edge);
            // check neighbours
            for (Edge neighbour : adj[edge.dest]) {
                if (!marked[neighbour.dest]) {
                    frontier.add(neighbour);
                }
            }
        }
        System.out.println(mst);
        System.out.println(minCost);
        return minCost;
    }

    public static int minimumSpanningCostKruskal(int n, int[][] edges) {
        // TODO: Minimum Spanning Tree (MST) with Kruskal Union-Find (UF)
        // sort the edges by cost
        Arrays.sort(edges, (a, b) -> Integer.compare(a[2], b[2]));

        Queue<Edge> mst = new LinkedList<>(); // MST
        UF uf = new UF(n);
        int minCost = 0;

        for (int[] edge : edges) {
            int src = edge[0];
            int dest = edge[1];
            int cost = edge[2];
            // add edge if not connected to MST
            if (!uf.connected(src, dest)) {
                uf.union(src, dest);
                minCost += cost;
                mst.add(new Edge(src, dest, cost));
            }
            // stop if MST is complete
            if (mst.size() == n - 1) break;
        }
//        System.out.println(Arrays.toString(uf.id));
//        System.out.println(Arrays.toString(uf.size));
        System.out.println(mst);
        System.out.println(minCost);

        return minCost;
    }

    static class Edge {
        int src;
        int dest;
        int cost;
        public Edge(int from, int to, int cost) {
            this.src = from;
            this.dest = to;
            this.cost = cost;
        }
        public String toString() {
            return String.format("(%d,%d)[%d]", src, dest, cost);
        }
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

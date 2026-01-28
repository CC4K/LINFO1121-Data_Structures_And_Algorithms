package Part6_Graphs;

import java.util.ArrayList;
import java.util.List;

/**
 * Implement the Digraph.java interface in
 * the Digraph.java class using an adjacency list
 * data structure to represent directed graphs.
 */
public class Digraph {

    private List<Integer>[] adj;
    private int V;
    private int E;

    public Digraph(int V) {
        // TODO
        this.adj = new List[V];
        for (int i = 0; i < V; i++) adj[i] = new ArrayList<>();
        this.V = V;
        this.E = 0;
    }

    /**
     * The number of vertices
     */
    public int V() {
        // TODO
        return V;
    }

    /**
     * The number of edges
     */
    public int E() {
        // TODO
        return E;
    }

    /**
     * Add the edge v->w
     */
    public void addEdge(int v, int w) {
        // TODO
        adj[v].add(w);
        E++;
    }

    /**
     * The nodes adjacent to node v
     * that is the nodes w such that there is an edge v->w
     */
    public Iterable<Integer> adj(int v) {
        // TODO
        return adj[v];
    }

    /**
     * A copy of the digraph with all edges reversed
     */
    public Digraph reverse() {
        //System.out.println(Arrays.toString(adj));
        // TODO
        Digraph reverted = new Digraph(V);
        for (int currentNode = 0; currentNode < V; currentNode++) {
            for (int neighbourNode : adj[currentNode]) {
                reverted.addEdge(neighbourNode, currentNode);
            }
        }
        //System.out.println(Arrays.toString(reverted.adj));
        return reverted;
    }

}

package Part6_Graphs;

import java.util.*;

/**
 * The erdos number is a "collaborative distance" metric to Paul Erdos (a prolific mathematician) based on co-authorship of mathematical articles.
 * It is computed as follows:
 * <p>
 * - Erdos has, by definition an erdos-number of 0.
 * <p>
 * - For each other author, we look at all his/her co-authors in each article.
 *   If n is the minimum erdos-number from all his co-authors, then this author has an erdos-number of n+1.
 * <p>
 * For example:
 * <p>
 * Given this set of co-authors relations:
 * <pre>
 * 		{ "Paul Erdös", "Edsger W. Dijkstra" }
 * 		{ "Edsger W. Dijkstra", "Alan M. Turing" }
 * 		{ "Edsger W. Dijkstra", "Donald Knuth" }
 * 		{ "Donald Knuth", "Stephen Cook", "Judea Pearl" }
 * </pre>
 * The erdos number of Paul Erdos is 0, of Edsger W. Dijkstra is 1, of Alan M. Turing is 2, of Donald Knuth is 2, of Stephen Cook is 3.
 * <p>
 * Debug your code on the small examples in the test suite.
 */
public class Erdos {
	public static final String erdos = "Paul Erdös";
    public Map<String, Integer> distTo = new HashMap<>(); // [author, erdos number]

	/**
	 * Constructs an Erdos object and computes the Erdős numbers for each author.
	 * The constructor should run in O(n*m^2) where n is the number of co-author relations, and m the maximum number of co-authors in one article.
	 * @param articlesAuthors An ArrayList of String arrays, where each array represents the list of authors of a single article.
	 */
	public Erdos(ArrayList<String[]> articlesAuthors) {
		// TODO: store adjacencies then BFS
        Map<String, HashSet<String>> adj = new HashMap<>();
        for (String[] authors : articlesAuthors) {
            for (int i = 0; i < authors.length; i++) {
                for (int j = i+1; j < authors.length; j++) {
                    // add 2 by 2
                    if (!adj.containsKey(authors[i])) adj.put(authors[i], new HashSet<>());
                    if (!adj.containsKey(authors[j])) adj.put(authors[j], new HashSet<>());
                    adj.get(authors[i]).add(authors[j]);
                    adj.get(authors[j]).add(authors[i]);
                }
                if (!distTo.containsKey(authors[i])) distTo.put(authors[i], -1);
            }
        }
        //System.out.println(adj.entrySet());
        // Turing=[Dijkstra], Erdös=[Dijkstra], Dijkstra=[Turing, Erdös, Knuth], Pearl=[Cook, Knuth], Cook=[Pearl, Knuth], Knuth=[Dijkstra, Pearl, Cook]

        // TODO: BFS time
        Queue<String> frontier = new LinkedList<>();
        frontier.add(erdos);
        distTo.put(erdos, 0);

        while (!frontier.isEmpty()) {
            String author = frontier.poll();
            // check coauthors / neighbours
            for (String coauthor : adj.get(author)) {
                if (distTo.get(coauthor) == -1) { // not marked yet
                    distTo.put(coauthor, distTo.get(author) + 1); // set to current dist + 1
                    frontier.add(coauthor);
                }
                else if (distTo.get(author)+1 < distTo.get(coauthor)) { // compare with old recorded dist
                    distTo.put(coauthor, distTo.get(author) + 1); // set to current dist + 1
                }
            }
        }
        //System.out.println(distTo);
	}

	/**
	 * Returns the Erdős number of a given author.
	 * This method is expected to run in O(1).
	 * @param author The name of the author whose Erdős number is to be found.
	 * @return The Erdős number of the specified author. If the author is not in the network, returns -1.
	 */
	public int findErdosNumber(String author) {
		// TODO
        return distTo.getOrDefault(author, -1);
	}

}

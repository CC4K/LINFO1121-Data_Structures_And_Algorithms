package Part6_Graphs;

import java.util.*;

/**
 * You are asked to implement the WordTransformationSP class which allows to find the shortest path
 * from a string A to another string B (with the certainty that there is a path from A to B).
 * To do this, we define a rotation(x, y) operation that reverses the order of the letters between the x and y positions (not included).
 * For example, with A=``HAMBURGER``, if we do rotation(A, 4, 8), we get HAMBEGRUR.
 * So you can see that the URGE sub-string has been inverted to EGRU and the rest of the string
 * has remained unchanged: HAMB + ECRU + R = HAMBEGRUR.
 * Let's say that a rotation(x, y) has a cost of y-x.
 * For example going from HAMBURGER to HAMBEGRUR costs 8-4 = 4.
 * The question is what is the minimum cost to reach a string B from A?
 * So you need to implement a public static int minimalCost(String A, String B) function that returns the minimum cost to reach String B
 * from A using the rotation operation.
 */
public class WordTransformationSP {

    /**
     * Rotate the substring between start and end of a given string s
     * e.g. s = HAMBURGER, rotation(s,4,8) = HAMBEGRUR i.e. HAMB + EGRU + R
     * @param s
     * @param start
     * @param end
     * @return rotated string
     */
    public static String rotation(String s, int start, int end) {
        return s.substring(0, start) + new StringBuilder(s.substring(start, end)).reverse().toString() + s.substring(end);
    }

    /**
     * Compute the minimal cost from string "from" to string "to" representing the shortest path
     * @param from
     * @param to
     * @return
     */
    public static int minimalCost(String from, String to) {
        if (from.equals(to)) return 0;
        // TODO : Dijkstra with different version of the starting word as states
        Map<String, Integer> distTo = new HashMap<>(); // <word, cost>
        PriorityQueue<WordState> frontier = new PriorityQueue<>((w1, w2) -> w1.cost - w2.cost);

        distTo.put(from, 0);
        frontier.add(new WordState(from, 0));

        while (!frontier.isEmpty()) {
            WordState state = frontier.poll();
            // check end reached
            if (state.word.equals(to)) break;
            // check neighbours AKA all rotations
            int n = state.word.length();
            for (int i = 0; i < n+1 ; i++) {
                for (int j = i; j < n+1; j++) {
                    String newWord = rotation(state.word, i, j);
                    int newCost = state.cost + (j - i);

                    // if better cost => update
                    if (newCost < distTo.getOrDefault(newWord, Integer.MAX_VALUE)) {
                        distTo.put(newWord, newCost);
                        frontier.add(new WordState(newWord, newCost));
                    }
                }
            }
        }
        System.out.println(distTo.entrySet());
        return distTo.get(to);
    }

    static class WordState {
        String word;
        int cost;
        public WordState(String word, int cost) {
            this.word = word;
            this.cost = cost;
        }
        public String toString() {
            return String.format("(%s [%d])", word, cost);
        }
    }
}

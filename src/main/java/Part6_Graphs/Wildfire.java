package Part6_Graphs;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Let's consider a forest represented as a 2D grid.
 * Each cell of the grid can be in one of three states:
 * <p>
 * 0 representing an empty spot.
 * 1 representing a tree.
 * 2 representing a burning tree (indicating a wildfire).
 * <p>
 * The fire spreads from a burning tree to its four neighboring cells (up, down, left, and right) if there's a tree there.
 * Each minute, the trees in the neighboring cells of burning tree catch on fire.
 * <p>
 * Your task is to calculate how many minutes it would take for the fire to spread throughout the forest i.e. to burn all the trees.
 * <p>
 * If there are trees that cannot be reached by the fire (for example, isolated trees with no adjacent burning trees),
 * we consider that the fire will never reach them and -1 is returned.
 * <p>
 * The time-complexity of your algorithm must be O(n) with n the number of cells in the forest.
 */
public class Wildfire {
    static final int EMPTY = 0;
    static final int TREE = 1;
    static final int BURNING = 2;
    static final int[][] directions = new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    /**
     * This method calculates how many minutes it would take for the fire to spread throughout the forest.
     *
     * @param forest matrix marking the state of the forest (0 = empty, 1 = tree, 2 = burning tree)
     * @return the number of minutes it would take for the fire to spread throughout the forest,
     *         -1 if the forest cannot be completely burned.
     */
    public int burnForest(int[][] forest) {
        int n = forest.length;
        int m = forest[0].length;
        // TODO: BFS
        //boolean[][] marked = new boolean[n][m]; // TODO: Note: Use forest as the marked matrix (set to 0 if marked)
        int[][] distTo = new int[n][m]; // [dist_from_a_src, dist_from_a_src, dist_from_a_src...]
        Queue<int[]> frontier = new LinkedList<>(); // [x, y]

        // find initial fires and init BFS with them as sources
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < m; y++) {
                if (forest[x][y] == BURNING) {
                    forest[x][y] = EMPTY;
                    frontier.add(new int[]{x, y});
                }
            }
        }
        int max_dist = 0;

        while (!frontier.isEmpty()) {
            int[] p = frontier.poll();
            int pX = p[0];
            int pY = p[1];

            // check neighbours
            for (int[] direction : directions) {
                int nX = pX + direction[0];
                int nY = pY + direction[1];
                if (isInBounds(nX, nY, n, m) && forest[nX][nY] != EMPTY) { // is a tree (1 or 2) within the bounds of the forest
                    forest[nX][nY] = EMPTY; // <=> (marked = true)
                    distTo[nX][nY] = distTo[pX][pY] + 1;
                    max_dist = Math.max(max_dist, distTo[nX][nY]);
                    frontier.add(new int[]{nX, nY});
                }
            }
        }

        for (int[] f : forest) System.out.println(Arrays.toString(f));
        System.out.println();
        for (int[] d : distTo) System.out.println(Arrays.toString(d));
        System.out.println(max_dist);

        // check for leftover trees (1 or 2) in the forest
        for (int[] line : forest) {
            if (Arrays.stream(line).filter((loc) -> loc != EMPTY).findAny().isPresent()) return -1;
        }

        return max_dist;
    }

    private boolean isInBounds(int x, int y, int n, int m) {
        return ((0 <= x) && (x < n) && (0 <= y) && (y < m));
    }

}

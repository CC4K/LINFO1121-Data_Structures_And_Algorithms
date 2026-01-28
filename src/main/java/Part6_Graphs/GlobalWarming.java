package Part6_Graphs;

import java.util.Arrays;

/**
 * In this exercise, we revisit the GlobalWarming class from the sorting package.
 * You are still given a matrix of altitude in parameter of the constructor, with a water level.
 * All the entries whose altitude is under, or equal to, the water level are submerged while the other constitute small islands.
 * <p>
 * For example let us assume that the water level is 3 and the altitude matrix is the following
 * <pre>
 *      | 1 | 3 | 3 | 1 | 3 |
 *      | 4 | 2 | 2 | 4 | 5 |
 *      | 4 | 4 | 1 | 4 | 2 |
 *      | 1 | 4 | 2 | 3 | 6 |
 *      | 1 | 1 | 1 | 6 | 3 |
 * </pre>
 * If we replace the submerged entries by _, it gives the following matrix
 * <pre>
 *      | _ | _ | _ | _ | _ |
 *      | 4 | _ | _ | 4 | 5 |
 *      | 4 | 4 | _ | 4 | _ |
 *      | _ | 4 | _ | _ | 6 |
 *      | _ | _ | _ | 6 | _ |
 * </pre>
 * The goal is to implement two methods that can answer the following questions:
 * <p>
 *      1) Are two entries on the same island?
 * <p>
 *      2) How many islands are there
 * <p>
 * Two entries above the water level are connected if they are next to each other on
 * the same row or the same column. They are **not** connected **in diagonal**.
 * Beware that the methods must run in O(1) time complexity, at the cost of a pre-processing in the constructor.
 * To help you, you'll find a `Point` class in the utils package which identified an entry of the grid.
 * Carefully read the expected time complexity of the different methods.
 */
public class GlobalWarming {
    private final int[][] altitude;
    private final int waterLevel;
    private final int height;
    private final int width;
    private final int[][] directions = new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    private final boolean[][] marked;
    private final int[][] groupId;
    private int currentId;

    /**
     * Constructor. The run time of this method is expected to be in
     * O(n x log(n)) with n the number of entry in the altitude matrix.
     *
     * @param altitude the matrix of altitude
     * @param waterLevel the water level under which the entries are submerged
     */
    public GlobalWarming(int[][] altitude, int waterLevel) {
        this.altitude = altitude;
        this.waterLevel = waterLevel;
        this.height = altitude.length;
        this.width = altitude[0].length;

        this.marked = new boolean[height][width];
        this.groupId = new int[height][width];
        this.currentId = 0;

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (marked[row][col]) continue; // already visited => skip
                if (altitude[row][col] <= waterLevel) { // underwater => mark and skip
                    groupId[row][col] = -1;
                    marked[row][col] = true;
                }
                else { // if (altitude[row][col] > waterLevel) // above water => start new DFS
                    dfs(row, col); // detect the entire island
                    if (groupId[row][col] == currentId) currentId++; // component explored since returned from recursion
                }
            }
        }
        for (boolean[] b : marked) System.out.println(Arrays.toString(b));
        for (int[] l : groupId) System.out.println(Arrays.toString(l));
        System.out.println("nb islands : "+currentId);
    }

    /**
     * Use DFS to explore the entire island
     * @param row height coord
     * @param col width coord
     */
    private void dfs(int row, int col) {
        groupId[row][col] = currentId;
        marked[row][col] = true;
        // check neighbours
        for (int[] direction : directions) {
            int newRow = row + direction[0];
            int newCol = col + direction[1];
            if (inMatrix(newRow, newCol) && (altitude[newRow][newCol] > waterLevel) && (!marked[newRow][newCol])) {
                dfs(newRow, newCol);
            }
        }
    }

    private boolean inMatrix(int row, int col) {
        return (0 <= row && row < height) && (0 <= col && col < width);
    }

    /**
     * Returns the number of island
     * <p>
     * Expected time complexity O(1)
     */
    public int nbIslands() {
        return currentId;
    }

    /**
     * Return true if p1 is on the same island as p2, false otherwise
     * <p>
     * Expected time complexity: O(1)
     *
     * @param p1 the first point to compare
     * @param p2 the second point to compare
     */
    public boolean onSameIsland(Point p1, Point p2) {
        int id1 = groupId[p1.x][p1.y];
        int id2 = groupId[p2.x][p2.y];
        return id1 != -1 && id1 == id2;
    }


    /**
     * This class represent a point in a 2-dimension discrete plane. This is used, for instance, to
     * identified cells of a grid
     */
    static class Point {
        private final int x;
        private final int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() { return x; }
        public int getY() { return y; }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Point) {
                Point p = (Point) o;
                return p.x == this.x && p.y == this.y;
            }
            return false;
        }

        public String toString() {
            return "("+x+","+y+")";
        }
    }

}

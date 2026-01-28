package Part5_PQ_UF_Compression;

import java.util.Arrays;

/**
 * In this exercise, we revisit the GlobalWarming
 * class from the sorting package.
 * You are still given a matrix of altitude in
 * parameter of the constructor, with a water level.
 * All the entries whose altitude is under, or equal to,
 * the water level are submerged while the other constitute small islands.
 * <p>
 * For example let us assume that the water
 * level is 3 and the altitude matrix is the following
 * <p>
 *      | 1 | 3 | 3 | 1 | 3 |
 *      | 4 | 2 | 2 | 4 | 5 |
 *      | 4 | 4 | 1 | 4 | 2 |
 *      | 1 | 4 | 2 | 3 | 6 |
 *      | 1 | 1 | 1 | 6 | 3 |
 * <p>
 * If we replace the submerged entries
 * by _, it gives the following matrix
 * <p>
 *      | _ | _ | _ | _ | _ |
 *      | 4 | _ | _ | 4 | 5 |
 *      | 4 | 4 | _ | 4 | _ |
 *      | _ | 4 | _ | _ | 6 |
 *      | _ | _ | _ | 6 | _ |
 * <p>
 * The goal is to implement two methods that
 * can answer the following questions:
 *      1) Are two entries on the same island?
 *      2) How many islands are there
 * <p>
 * Two entries above the water level are
 * connected if they are next to each other on
 * the same row or the same column. They are
 * **not** connected **in diagonal**.
 * Beware that the methods must run in O(1)
 * time complexity, at the cost of a pre-processing in the constructor.
 * To help you, you'll find a `Point` class
 * in the utils package which identified an entry of the grid.
 * Carefully read the expected time complexity of the different methods.
 */
public class GlobalWarming {

    private final int[][] altitude;
    private final int waterLevel;
    private final int width;
    private final int height;

    private int nbIslands;
    private final int[] islands;
    private final boolean[] visited;

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
        this.width = altitude[0].length;
        this.height = altitude.length;
        //System.out.println(Arrays.deepToString(altitude));
        this.nbIslands = 0;
        this.islands = new int[width*height];
        this.visited = new boolean[width*height];
        Arrays.fill(visited, false);
        // use a recursion to go through islands and check neighbours
        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                dfs(row, column);
                if (islands[(width*row)+column] == nbIslands) nbIslands++;
            }
        }
    }

    private void dfs(int rowId, int columnId) {
        //System.out.println(Arrays.toString(islands));
        //System.out.println(Arrays.toString(visited));
        // check valid coordinate (inside matrix...)
        if (rowId >= height || rowId < 0 || columnId >= width || columnId < 0 || visited[(width*rowId)+columnId]) return; // exit
        // if underwater (help me, I am under the water, blub blub blub)
        if (altitude[rowId][columnId] <= waterLevel) {
            islands[(width*rowId)+columnId] = -1;
            visited[(width*rowId)+columnId] = true;
        }
        // if on an island
        else {
            islands[(width*rowId)+columnId] = nbIslands; // mark with curent islandId
            visited[(width*rowId)+columnId] = true;
            // visit other directions
            dfs(rowId-1, columnId);
            dfs(rowId+1, columnId);
            dfs(rowId, columnId-1);
            dfs(rowId, columnId+1);
        }
    }

    /**
     * Returns the number of island
     * <p>
     * Expected time complexity O(1)
     */
    public int nbIslands() {
         return this.nbIslands;
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
        // compare islandId's
        int id1 = islands[(width*p1.x)+p1.y];
        int id2 = islands[(width*p2.x)+p2.y];
        return id1 == id2 && id1 != -1 && id2 != -1;
    }

    /**
     * This class represent a point in a 2-dimension discrete plane. This is used, for instance, to
     * identified cells of a grid
     */
    public static class Point {

        private final int x;
        private final int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Point) {
                Point p = (Point) o;
                return p.x == this.x && p.y == this.y;
            }
            return false;
        }
    }
}

package Part6_Graphs;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * You just bought yourself the latest game from the Majong™ studio (recently acquired by Macrosoft™): MineClimb™.
 * In this 3D game, the map is made up of size 1 dimensional cube blocks, aligned on a grid, forming vertical columns of cubes.
 * There are no holes in the columns, so the state of the map can be represented as a matrix M of size n⋅m
 * where the entry M_{i,j} is the height of the cube column located at i,j (0 ≤ i < n, 0 ≤ j < m).
 * You can't delete or add cubes, but you do have climbing gear that allows you to move from one column to another
 * (in the usual four directions (north, south, east, west), but not diagonally).
 * The world of MineClimb™ is round: the position north of (0,j) is (n-1,j), the position west of (i,0) is (i,m-1), and vice versa.
 * <p>
 * The time taken to climb up or down a column depends on the difference in height between the current column and the next one.
 * Precisely, the time taken to go from column (i,j) to column (k,l) is |M_{i,j}-M_{k,l}|
 * <p>
 * Given the map of the mine, an initial position and an end position, what is the minimum time needed
 * to reach the end position from the initial position ?
 */
public class MineClimbing {

    private static final int[][] directions = new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    /**
     * Returns the minimum distance between (startX, startY) and (endX, endY), knowing that
     * you can climb from one mine block (a,b) to the other (c,d) with a cost Math.abs(map[a][b] - map[c][d])
     * <p>
     * Do not forget that the world is round: the position (map.length,i) is the same as (0, i), etc.
     * <p>
     * map is a matrix of size n times m, with n,m > 0.
     * <p>
     * 0 <= startX, endX < n
     * 0 <= startY, endY < m
     */
    public static int best_distance(int[][] map, int startX, int startY, int endX, int endY) {
        int n = map.length;
        int m = map[0].length;
        // TODO: Dijkstra (version using int[X, Y, dist])
        int[][] distTo = new int[n][m]; // record where we came from
        for (int[] l : distTo) Arrays.fill(l, Integer.MAX_VALUE);
        PriorityQueue<int[]> frontier = new PriorityQueue<>((p1, p2) -> p1[2] - p2[2]); // record next most interesting node

        distTo[startX][startY] = 0;
        frontier.add(new int[]{startX, startY, 0});

        while (!frontier.isEmpty()) {
            int[] p = frontier.poll();
            //System.out.println("current point : "+currentNode+" ");
            // end already reached
            if (p[0] == endX && p[1] == endY) break;
            // check neighbours
            for (int[] direction : directions) {
                int newX = p[0] + direction[0];
                int newY = p[1] + direction[1];
                // wrap-around
                if (newX == -1) newX = n-1;
                if (newX == n) newX = 0;
                if (newY == -1) newY = m-1;
                if (newY == m) newY = 0;
                //System.out.println("checking ("+newRow+","+newCol+")");

                int diff = Math.abs(map[p[0]][p[1]] - map[newX][newY]); // |M_{i,j} - M_{k,l}|
                int newDist = distTo[p[0]][p[1]] + diff;
                // if better dist => update
                if (newDist < distTo[newX][newY]) {
                    distTo[newX][newY] = newDist;
                    frontier.add(new int[]{newX, newY, newDist});
                }
            }
        }
        for (int[] d : distTo) System.out.println(Arrays.toString(d));
        return distTo[endX][endY];
    }

    public static int best_distance_point(int[][] map, int startX, int startY, int endX, int endY) {
        int n = map.length;
        int m = map[0].length;
        // TODO: Dijkstra (version using Point(X, Y, dist))
        int[][] distTo = new int[n][m]; // record where we came from
        for (int[] l : distTo) Arrays.fill(l, Integer.MAX_VALUE);
        PriorityQueue<Point> frontier = new PriorityQueue<>((p1, p2) -> p1.dist - p2.dist); // record next most interesting node

        distTo[startX][startY] = 0;
        frontier.add(new Point(startX, startY, 0));

        while (!frontier.isEmpty()) {
            Point p = frontier.poll();
            //System.out.println("current point : "+currentNode+" ");

            // end already reached
            if (p.x == endX && p.y == endY) break;

            // check neighbours
            for (int[] direction : directions) {
                int newX = p.x + direction[0];
                int newY = p.y + direction[1];
                // wrap-around
                if (newX == -1) newX = n-1;
                if (newX == n) newX = 0;
                if (newY == -1) newY = m-1;
                if (newY == m) newY = 0;
                //System.out.println("checking ("+newRow+","+newCol+")");

                int diff = Math.abs(map[p.x][p.y] - map[newX][newY]); // |M_{i,j} - M_{k,l}|
                int newDist = distTo[p.x][p.y] + diff;
                // if better dist => update
                if (newDist < distTo[newX][newY]) {
                    distTo[newX][newY] = newDist;
                    frontier.add(new Point(newX, newY, newDist));
                }
            }
        }
        for (int[] d : distTo) System.out.println(Arrays.toString(d));
        return distTo[endX][endY];
    }

    static class Point {
        public int x;
        public int y;
        public int dist;
        public Point(int x, int y, int dist) {
            this.x = x;
            this.y = y;
            this.dist = dist;
        }
        public String toString() {
            return String.format("(%d,%d)[%d]", x, y, dist);
        }
    }

}

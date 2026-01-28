package Part6_Graphs;

import java.util.*;

/**
 * Author Pierre Schaus
 * <p>
 * Assume the following 5x5 matrix that represent a grid surface:
 * <pre>
 * int [][] tab = new int[][] {{1,3,3,1,3},
 *                             {4,2,2,4,5},
 *                             {4,4,5,4,2},
 *                             {1,4,2,3,6},
 *                             {1,1,1,6,3}};
 * </pre>
 * Given a global water level, all positions in the matrix with a value <= the water level are flooded and therefore unsafe.
 * So, assuming the water level is 3, all safe points are highlighted between parenthesis:
 * <pre>
 *   1 , 3 , 3 , 1 , 3
 *  (4), 2 , 2 ,(4),(5)
 *  (4),(4),(5),(4), 2
 *   1 ,(4), 2 , 3 ,(6)
 *   1 , 1 , 1 ,(6), 3}
 * </pre>
 * The method you need to implement is a method that find a safe-path between two positions (row,column) on the matrix.
 * The path assume you only make horizontal or vertical moves but not diagonal moves.
 * <p>
 * For a water level of 3, the shortest path between (1,0) and (1,3) is
 * <p>
 * (1,0) -> (2,0) -> (2,1) -> (2,2) -> (2,3) -> (1,3)
 * <p>
 * Complete the code below so that the {@code shortestPath} method works as expected
 */
public class GlobalWarmingPaths {
    int waterLevel;
    int [][] altitude;
    int height;
    int width;
    int[][] directions = new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    public GlobalWarmingPaths(int[][] altitude, int waterLevel) {
        this.waterLevel = waterLevel;
        this.altitude = altitude;
        // TODO
        this.height = altitude.length;
        this.width = altitude[0].length;
    }

    /**
     * Computes the shortest path between point p1 and p2
     * @param p1 the starting point
     * @param p2 the ending point
     * @return the list of the points starting
     *         from p1 and ending in p2 that corresponds
     *         the shortest path.
     *         If no such path, an empty list.
     */
    public List<Point> shortestPath(Point p1, Point p2) {
        if (altitude[p1.x][p1.y] <= waterLevel || altitude[p2.x][p2.y] <= waterLevel) return new ArrayList<>();
        if (p1.equals(p2)) {
            List<Point> path = new ArrayList<>();
            path.add(p1);
            return path;
        }
        // TODO: BFS
        Queue<Point> frontier = new LinkedList<>();
        boolean[][] marked = new boolean[height][width];
        Point[][] edgeTo = new Point[height][width];

        marked[p1.x][p1.y] = true;
        frontier.add(p1);

        while (!frontier.isEmpty()) {
            Point currentPoint = frontier.poll();
            // objective reached
            if (currentPoint.equals(p2)) break;
            // check neighbours
            for (int[] direction : directions) {
                int newX = currentPoint.x + direction[0];
                int newY = currentPoint.y + direction[1];
                if ((0 <= newX && newX < height) && (0 <= newY && newY < width) && (!marked[newX][newY]) && (altitude[newX][newY] > waterLevel)) {
                    marked[newX][newY] = true;
                    edgeTo[newX][newY] = currentPoint;
                    frontier.add(new Point(newX, newY));
                }
            }
        }
        //for (boolean[] b : marked) System.out.println(Arrays.toString(b));
        //for (Point[] p : edgeTo) System.out.println(Arrays.toString(p));


        List<Point> path = new LinkedList<>();
        path.add(p2);

        Point head = edgeTo[p2.x][p2.y];
        while (head != p1) {
            path.add(head);
            head = edgeTo[head.x][head.y];
        }
        path.add(p1);

        Collections.reverse(path);
        System.out.println(path);

        // expected time complexity O(n^2)
        return path;
    }

    /**
     * This class represent a point in a 2-dimension discrete plane.
     * This is used to identify the cells of a grid
     * with X = row, Y = column
     */
    static class Point {
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

        public String toString() {
            return "("+x+","+y+")";
        }
    }

}

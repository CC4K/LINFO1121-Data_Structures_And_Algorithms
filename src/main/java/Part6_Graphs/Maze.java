package Part6_Graphs;

import java.util.*;

/**
 * We are interested in solving a maze represented
 * by a matrix of integers 0-1 of size nxm.
 * This matrix is a two-dimensional array.
 * An entry equal to '1' means that there
 * is a wall and therefore this position is not accessible,
 * while '0' means that the position is free.
 * We ask you to write a Java code to discover
 * the shortest path between two coordinates
 * on this matrix from (x1, y1) to (x2, y2).
 * The moves can only be vertical (up/down) or horizontal (left/right)
 * (not diagonal), one step at a time.
 * The result of the path is an Iterable of
 * coordinates from the origin to the destination.
 * These coordinates are represented by integers
 * between 0 and n * m-1, where an integer 'a'
 * represents the position x =a/m and y=a%m.
 * If the start or end position is a wall
 * or if there is no path, an empty Iterable must be returned.
 * The same applies if there is no path
 * between the origin and the destination.
 */
public class Maze {
    public static Iterable<Integer> shortestPath(int[][] maze, int x1, int y1, int x2, int y2) {
        if (maze[x1][y1] == 1 || maze[x2][y2] == 1) return new ArrayList<>();

        int nRows = maze.length;
        int mCols = maze[0].length;
        int[][] directions = new int[][]{{0, -1}, {0, 1}, {-1, 0}, {1, 0}};

        // TODO: BFS
        Queue<Integer> frontier = new LinkedList<>();
        int[][] edgeTo = new int[nRows][mCols];
        boolean[][] marked = new boolean[nRows][mCols];

        int start = ind(x1, y1, mCols);
        int end = ind(x2, y2, mCols);
        if (start == end) {
            List<Integer> path = new ArrayList<>();
            path.add(start);
            return path;
        }
        frontier.add(start);
        marked[x1][y1] = true;

        while (!frontier.isEmpty()) {
            int currentPos = frontier.poll();
            int currentRow = row(currentPos, mCols);
            int currentCol = col(currentPos, mCols);
            // objective reached
            if (currentPos == end) break;
            // check neighbours
            for (int[] direction : directions) {
                int newRow = currentRow + direction[0];
                int newCol = currentCol + direction[1];
                int newPos = ind(newRow, newCol, mCols);
                if ((0 <= newRow && newRow < nRows) && (0 <= newCol && newCol < mCols) && (!marked[newRow][newCol]) && (maze[newRow][newCol] != 1)) { // checks
                    frontier.add(newPos);
                    marked[newRow][newCol] = true;
                    edgeTo[newRow][newCol] = currentPos;
                }
            }
        }

        //for (boolean[] line : marked) System.out.println(Arrays.toString(line));
        //for (int[] line : memory) System.out.println(Arrays.toString(line));

        if (!marked[x2][y2]) return new ArrayList<>(); // end not reached

        List<Integer> path = new ArrayList<>();
        path.add(end);

        int head = edgeTo[x2][y2];
        while (head != start) {
            path.add(head);
            int newRow = row(head, mCols);
            int newCol = col(head, mCols);
            head = edgeTo[newRow][newCol];
        }
        path.add(start);

        Collections.reverse(path);
        //System.out.println(path);
        return path;
    }

    public static int ind(int x, int y, int lg) {
        return x * lg + y;
    }

    public static int row(int pos, int mCols) {
        return pos / mCols;
    }

    public static int col(int pos, int mCols) {
        return pos % mCols;
    }

}

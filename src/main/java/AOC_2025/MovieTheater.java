package AOC_2025;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class MovieTheater {

    public static String[] readLines(String filename) throws IOException {
        FileReader fileReader = new FileReader(filename);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        List<String> lines = new ArrayList<>();
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            lines.add(line);
        }
        bufferedReader.close();
        return lines.toArray(new String[lines.size()]);
    }

    public static void main(String[] args) {
        try {
            Path path = Paths.get(System.getProperty("user.dir"), "data", "AOC_2025.MovieTheater", "inputTest.txt");
            String[] testInput = readLines(path.toString());
            Path path2 = Paths.get(System.getProperty("user.dir"), "data", "AOC_2025.MovieTheater", "input.txt");
            String[] realInput = readLines(path2.toString());
            // ====================================================================================================== //
            //System.out.println("Part 1 Test : "+getMaxArea(testInput)); // 50
            //System.out.println("Part 1 Input : "+getMaxArea(realInput)); // 4777967538
            // ====================================================================================================== //
            int N = realInput.length;
            int[][] points = new int[N][];
            for (int i = 0; i < N; i++) {
                String[] split = realInput[i].split(",");
                points[i] = new int[]{Integer.parseInt(split[0]), Integer.parseInt(split[1])};
            }
            System.out.println(Arrays.deepToString(points));

            long max_area = 0;
            // remember 2 last points
            int[] p_minus_2 = points[N-2];
            int[] p_minus_1 = points[N-1];
            for (int i = 0; i < N; i++) {
                int[] p = points[i];
                // detect angle
                // if angle formed => try to make a rectangle from p-2 to p (check if fully in zone)
                // case ^
                if (p_minus_2[1] > p_minus_1[1]) {
                    // case ^ >
                    //if (p_minus_1[0] < p[0]) System.out.println("(^ >)");
                    // case ^ <
                    //if (p_minus_1[0] > p[0]) System.out.println("(^ <)");
                    int[] corner = new int[]{p[0], p_minus_2[1]};
                    if (pointInValidLine(points, corner) || pointInValidZone(points, corner)) {
                        long area = (long) (Math.abs(corner[0] - p_minus_1[0]) + 1) * (Math.abs(corner[1] - p_minus_1[1]) + 1);
                        System.out.println(Arrays.toString(corner)+" is valid | area : "+area);
                        if (area > max_area) max_area = area;
                    }
                }
                // case v
                if (p_minus_2[1] < p_minus_1[1]) {
                    // case v >
                    //if (p_minus_1[0] < p[0]) System.out.println("(v >)");
                    // case v <
                    //if (p_minus_1[0] > p[0]) System.out.println("(v <)");
                    int[] corner = new int[]{p[0], p_minus_2[1]};
                    if (pointInValidLine(points, corner) || pointInValidZone(points, corner)) {
                        long area = (long) (Math.abs(corner[0] - p_minus_1[0]) + 1) * (Math.abs(corner[1] - p_minus_1[1]) + 1);
                        System.out.println(Arrays.toString(corner)+" is valid | area : "+area);
                        if (area > max_area) max_area = area;
                    }
                }
                // case <
                if (p_minus_2[0] > p_minus_1[0]) {
                    // case < ^
                    //if (p_minus_1[1] > p[1]) System.out.println("(< ^)");
                    // case < v
                    //if (p_minus_1[1] < p[1]) System.out.println("(< v)");
                    int[] corner = new int[]{p_minus_2[0], p[1]};
                    if (pointInValidLine(points, corner) || pointInValidZone(points, corner)) {
                        long area = (long) (Math.abs(corner[0] - p_minus_1[0]) + 1) * (Math.abs(corner[1] - p_minus_1[1]) + 1);
                        System.out.println(Arrays.toString(corner)+" is valid | area : "+area);
                        if (area > max_area) max_area = area;
                    }
                }
                // case >
                if (p_minus_2[0] < p_minus_1[0]) {
                    // case > ^
                    //if (p_minus_1[1] > p[1]) System.out.println("(> ^)");
                    // case > v
                    //if (p_minus_1[1] < p[1]) System.out.println("(> v)");
                    int[] corner = new int[]{p_minus_2[0], p[1]};
                    if (pointInValidLine(points, corner) || pointInValidZone(points, corner)) {
                        long area = (long) (Math.abs(corner[0] - p_minus_1[0]) + 1) * (Math.abs(corner[1] - p_minus_1[1]) + 1);
                        System.out.println(Arrays.toString(corner)+" is valid | area : "+area);
                        if (area > max_area) max_area = area;
                    }
                }
                // update p-2 & p-1
                p_minus_2 = p_minus_1;
                p_minus_1 = p;
            }
            System.out.println(max_area);
        }
        catch (IOException e) { System.out.println(e); }
    }

    // ============================================================================================================== //

    private static boolean pointInValidZone(int[][] points, int[] corner) {
        boolean vertical_ok1 = false;
        boolean vertical_ok2 = false;
        boolean horizontal_ok1 = false;
        boolean horizontal_ok2 = false;
        int px = corner[0];
        int py = corner[1];
        int min_x = Arrays.stream(points).min((a, b) -> a[0] - b[0]).get()[0];
        int max_x = Arrays.stream(points).max((a, b) -> a[0] - b[0]).get()[0];
        int min_y = Arrays.stream(points).min((a, b) -> a[1] - b[1]).get()[1];
        int max_y = Arrays.stream(points).max((a, b) -> a[1] - b[1]).get()[1];
        //System.out.println(min_x+"-"+max_x);
        //System.out.println(min_y+"-"+max_y);
        // keep looking in all directions until a point is valid
        for (int y = min_y; y < max_y; y++) { // vertical check
            if (pointInValidLine(points, new int[]{px, y}) && !vertical_ok1) {
                vertical_ok1 = true;
            }
            else if (pointInValidLine(points, new int[]{px, y}) && vertical_ok1) {
                vertical_ok2 = true;
            }
        }
        for (int x = min_x; x < max_x; x++) { // horizontal check
            if (pointInValidLine(points, new int[]{x, py}) && !horizontal_ok1) {
                horizontal_ok1 = true;
            }
            else if (pointInValidLine(points, new int[]{x, py}) && horizontal_ok1) {
                horizontal_ok2 = true;
            }
        }
        return vertical_ok2 && horizontal_ok2;
    }

    private static boolean pointInValidLine(int[][] points, int[] corner) {
        // check that between two points on horizontal or vertical line
        int px = corner[0];
        int py = corner[1];
        int[] p_minus_1 = points[points.length -1];
        for (int[] point : points) {
            // vertical check
            if (p_minus_1[0] == point[0] && point[0] == px) {
                int a = Math.min(p_minus_1[1], point[1]);
                int b = Math.max(p_minus_1[1], point[1]);
                if (a < py && py < b) {
                    //System.out.println(Arrays.toString(corner) + " is on vertical valid line");
                    return true;
                }
            }
            // horizontal check
            if (p_minus_1[1] == point[1] && point[1] == py) {
                int a = Math.min(p_minus_1[0], point[0]);
                int b = Math.max(p_minus_1[0], point[0]);
                if (a < px && px < b) {
                    //System.out.println(Arrays.toString(corner) + " is on horizontal valid line");
                    return true;
                }
            }
            p_minus_1 = point;
        }
        return false;
    }

    private static long getMaxArea(String[] input) {
        int N = input.length;
        int[][] points = new int[N][];
        for (int i = 0; i < N; i++) {
            String[] split = input[i].split(",");
            points[i] = new int[]{Integer.parseInt(split[0]), Integer.parseInt(split[1])};
        }
        System.out.println(Arrays.deepToString(points));

        long max_area = 0;
        for (int[] p1 : points) {
            for (int[] p2 : points) {
                long area = (long) (p2[0] - p1[0] + 1) * (p2[1] - p1[1] + 1);
                if (area > max_area) max_area = area;
            }
        }
        return max_area;
    }

}

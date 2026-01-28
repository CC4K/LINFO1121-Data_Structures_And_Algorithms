package AOC_2025;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class Playground {

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
            Path path = Paths.get(System.getProperty("user.dir"), "data", "AOC_2025.Playground", "inputTest.txt");
            String[] testInput = readLines(path.toString());
            Path path2 = Paths.get(System.getProperty("user.dir"), "data", "AOC_2025.Playground", "input.txt");
            String[] realInput = readLines(path2.toString());
            // ====================================================================================================== //
            System.out.println("Part 1 Test : "+part1Test(testInput));
            System.out.println("Part 1 Input : "+part1Real(realInput)); // 63920
            // ====================================================================================================== //
            System.out.println("Part 2 Test : "+part2(testInput));
            System.out.println("Part 2 Input : "+part2(realInput)); // 1026594680
        }
        catch (IOException e) { System.out.println(e); }
    }

    // ============================================================================================================== //

    private static long part2(String[] input) {
        // UF init
        WeightedQuickUnionUF uf = new WeightedQuickUnionUF(input.length);
        // prepare points and distances
        List<Point> points = toList(input);
        List<Distance> distances = computeDistances(points);
        long x1 = 0;
        long x2 = 0;
        while (true) {
            // find smallest distance
            Distance smallestDistance = getSmallestDistance(distances);
            if (smallestDistance == null) break;
            if (uf.count == 1) break;
            x1 = smallestDistance.p1.x;
            x2 = smallestDistance.p2.x;
            //System.out.println(smallestDistance);
            // and join both points
            int id1 = points.indexOf(smallestDistance.p1);
            int id2 = points.indexOf(smallestDistance.p2);
            uf.union(id1, id2);
            // nullify their distance for next run
            distances.get(getDistanceId(id1, id2, points.size())).val = 0;
        }
        return x1 * x2;
    }

    private static int part1Test(String[] input) {
        // UF init
        WeightedQuickUnionUF uf = new WeightedQuickUnionUF(input.length);
        // prepare points and distances
        List<Point> points = toList(input);
        List<Distance> distances = computeDistances(points);
        for (int i = 0; i < 10; i++) {
            // find smallest distance
            Distance smallestDistance = getSmallestDistance(distances);
            //System.out.println(smallestDistance);
            // and join both points
            int id1 = points.indexOf(smallestDistance.p1);
            int id2 = points.indexOf(smallestDistance.p2);
            uf.union(id1, id2);
            // nullify their distance for next run
            distances.get(getDistanceId(id1, id2, points.size())).val = 0;
        }
        // get ids 3 largest circuits from uf.groupSizes
        int[] largestSizes = getThreeLargestCircuitsSizes(uf.groupSizes);
        //System.out.println(Arrays.toString(largestSizes));
        return Arrays.stream(largestSizes).reduce(1, (x, y) -> x * y);
    }

    private static int part1Real(String[] input) {
        // UF init
        WeightedQuickUnionUF uf = new WeightedQuickUnionUF(input.length);
        // prepare points and distances
        List<Point> points = toList(input);
        List<Distance> distances = computeDistances(points);
        for (int i = 0; i < 1000; i++) {
            // find smallest distance
            Distance smallestDistance = getSmallestDistance(distances);
            //System.out.println(smallestDistance);
            // and join both points
            int id1 = points.indexOf(smallestDistance.p1);
            int id2 = points.indexOf(smallestDistance.p2);
            uf.union(id1, id2);
            // nullify their distance for next run
            distances.get(getDistanceId(id1, id2, points.size())).val = 0;
        }
        // get ids 3 largest circuits from uf.groupSizes
        int[] largestSizes = getThreeLargestCircuitsSizes(uf.groupSizes);
        //System.out.println(Arrays.toString(largestSizes));
        return Arrays.stream(largestSizes).reduce(1, (x, y) -> x * y);
    }

    private static int[] getThreeLargestCircuitsSizes(int[] groupSizes) {
        Arrays.sort(groupSizes);
        int id1 = groupSizes.length-1;
        int id2 = groupSizes.length-2;
        int id3 = groupSizes.length-3;
        return new int[]{groupSizes[id1], groupSizes[id2], groupSizes[id3]};
    }

    private static int getDistanceId(int id1, int id2, int N) {
        int offset = (id1 * N) - (id1 * (id1 - 1)) / 2;
        return offset + (id2 - id1);
    }

    private static Distance getSmallestDistance(List<Distance> distances) {
        // init
        double minDist = Double.MAX_VALUE;
        Point p1 = null;
        Point p2 = null;
        // start search
        for (Distance distance : distances) {
            if (distance.val != 0 && distance.val < minDist) {
                minDist = distance.val;
                p1 = distance.p1;
                p2 = distance.p2;
            }
        }
        if (p1 == null || p2 == null) return null;
        return new Distance(p1, p2);
    }

    private static List<Distance> computeDistances(List<Point> points) {
        List<Distance> distances = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            Point p1 = points.get(i);
            for (int j = i; j < points.size(); j++) {
                Point p2 = points.get(j);
                Distance dist = new Distance(p1, p2);
                distances.add(dist);
            }
        }
        return distances;
    }

    private static class Distance {
        Point p1;
        Point p2;
        double val;
        public Distance(Point point1, Point point2) {
            this.p1 = point1;
            this.p2 = point2;
            this.val = dist(point1, point2);
        }
        public String toString() { return String.format("d(%s, %s) = %f", p1, p2, val); }
    }

    private static double dist(Point p1, Point p2) {
        double dx = (p1.x - p2.x);
        double dy = (p1.y - p2.y);
        double dz = (p1.z - p2.z);
        return Math.sqrt((dx*dx)+(dy*dy)+(dz*dz));
    }

    private static class Point {
        int x;
        int y;
        int z;
        public Point(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
        public String toString() { return String.format("(%d, %d, %d)", x, y, z); }
    }

    private static List<Point> toList(String[] input) {
        List<Point> coords = new ArrayList<>();
        for (String s : input) {
            String[] split = s.split(",");
            Point newCoord = new Point(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
            coords.add(newCoord);
        }
        return coords;
    }

    public static class WeightedQuickUnionUF {
        private final int[] groupIds; // parent link (site indexed)
        private final int[] groupSizes; // size of component for roots (site indexed)
        private int count; // number of components
        public WeightedQuickUnionUF(int N) {
            count = N;
            groupIds = new int[N];
            for (int i = 0; i < N; i++) groupIds[i] = i;
            groupSizes = new int[N];
            for (int i = 0; i < N; i++) groupSizes[i] = 1;
        }
        public int count() { return count; }
        public boolean connected(int p, int q) { return find(p) == find(q); }
        private int find(int p) { // Follow links to find a root.
            while (p != groupIds[p]) p = groupIds[p];
            return p;
        }
        public void union(int p, int q) {
            int i = find(p);
            int j = find(q);
            if (i == j) return;
            // Make smaller root point to larger one.
            if (groupSizes[i] < groupSizes[j])  { groupIds[i] = j; groupSizes[j] += groupSizes[i]; }
            else                                { groupIds[j] = i; groupSizes[i] += groupSizes[j]; }
            count--;
        }
    }

}

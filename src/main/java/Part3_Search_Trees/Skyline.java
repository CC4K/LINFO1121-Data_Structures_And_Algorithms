package Part3_Search_Trees;

import java.util.*;

/**
 * In this exercise, you must compute the skyline defined by a set of buildings.
 * When viewed from far away, the buildings only appear as if they were on a two-dimensionnal line.
 * Hence, they can be defined by three integers: The start of the building (left side), the height
 * of the building and the end of the building (right side).
 * For example, a building defined by (2, 5, 4) would look like
 * <pre>
 *   xxx
 *   xxx
 *   xxx
 *   xxx
 *   xxx
 * ________
 * </pre>
 * Obviously in practice buildings are not on a line, so they can overlap. If we add a new, smaller,
 * building in front of the previous one, defined by (3, 3, 6), then the view looks like:
 * <pre>
 *   xxx
 *   xxx
 *   xyyyy
 *   xyyyy
 *   xyyyy
 * ________
 * </pre>
 * The skyline is then define as the line that follows the highest building at any given points.
 * Visually, for the above example, it gives:
 * <pre>
 *   sss
 *      
 *      ss
 *        
 *        
 * ________
 * </pre>
 * <pre>
 * Input:
 * int[][] buildings = {{2, 5, 4}, {3, 3, 6}};
 * Output:
 * {{2,5},{5,3},{7,0}};
 * </pre>
 * We ask you to compute, given a set of building, their skyline.
 */
public class Skyline {


    /**
     * The buildings are defined with triplets (left, height, right).
     * <pre>
     * Input:
     * int[][] buildings = {{1, 11, 5}, {2, 6, 7}, {3, 13, 9}, {12, 7, 16}, {14, 3, 25}, {19, 18, 22}, {23, 13, 29}, {24, 4, 28}};
     * Output:
     * {{1,11},{3,13},{10,0},{12,7},{17,3},{19,18},{23,13},{30,0}};
     * </pre>
     * @param buildings
     * @return  the skyline in the form of a list of "key points [x, height]".
     *          A key point is the left endpoint of a horizontal line segment.
     *          The key points are sorted by their x-coordinate in the list.
     */
    public static List<int[]> getSkyline(int[][] buildings) {
        //for (int[] building : buildings) System.out.println(Arrays.toString(building));
        // TODO : Sweep Algorithm with extra steps to keep track of not just the X but the Y/Height too (TREEMAP ARE GREAT)

        // record "vertical events"
        int[][] events = new int[2* buildings.length][];
        for (int i = 0; i < 2* buildings.length; i += 2) {
            events[i] = new int[]{buildings[i/2][0], buildings[i/2][1], 1};
            events[i+1] = new int[]{buildings[i/2][2]+1, buildings[i/2][1], -1};
        }
        Arrays.sort(events, (e1, e2) -> e1[0] - e2[0]);
        Arrays.sort(events, Comparator.comparingInt(e -> e[0]));
        //for (int[] event : vertical) System.out.print(Arrays.toString(event));
        //[1, 11, 1][2, 6, 1][3, 13, 1][6, 11, -1][8, 6, -1][10, 13, -1][12, 7, 1][14, 3, 1][17, 7, -1][19, 18, 1][23, 18, -1][23, 13, 1][24, 4, 1][26, 3, -1][29, 4, -1][30, 13, -1]
        int n = events.length;

        List<int[]> skyline = new ArrayList<>();
        TreeMap<Integer, Integer> heights = new TreeMap<>(); // [height, height counter]
        heights.put(0, 1); // initial ground
        int prev_max_height = 0;

        for (int i = 0; i < n; i++) {
            int x = events[i][0], y = events[i][1], up = events[i][2];

            if (up == 1) { // going up => add 1 to height counter / add new height
                if (heights.containsKey(y)) heights.put(y, heights.get(y)+1);
                else if (!heights.containsKey(y)) heights.put(y, 1);
            }
            else if (up == -1) { // going down => remove 1 to height counter / remove outdated height
                int height_count = heights.get(y);
                if (height_count == 1) heights.remove(y); // doesn't count anymore
                else heights.put(y, height_count-1); // update
            }
            //System.out.printf("%02d %s%s", x, heights, "\t".repeat(10-heights.size()));

            boolean skip = false;
            if (i != n-1 && events[i+1][0] == x) skip = true; // skip when next X is the same to avoid 2 diff heights on same X + avoid skipping last X where there is no next
            //System.out.println(x+" ("+heights.lastKey()+" != "+prev_max_height+") = "+(heights.lastKey() != prev_max_height));
            if ((heights.lastKey() != prev_max_height) && !skip) {
                skyline.add(new int[]{x, heights.lastKey()});
                prev_max_height = heights.lastKey();
            }

            //for (int[] s : skyline) System.out.print(Arrays.toString(s)); System.out.println();
        }

        /*
        01 {0=1, 11=1}								[1, 11]
        02 {0=1, 6=1, 11=1}							[1, 11]
        03 {0=1, 6=1, 11=1, 13=1}					[1, 11][3, 13]
        06 {0=1, 6=1, 13=1}							[1, 11][3, 13]
        08 {0=1, 13=1}								[1, 11][3, 13]
        10 {0=1}									[1, 11][3, 13][10, 0]
        12 {0=1, 7=1}								[1, 11][3, 13][10, 0][12, 7]
        14 {0=1, 3=1, 7=1}							[1, 11][3, 13][10, 0][12, 7]
        17 {0=1, 3=1}								[1, 11][3, 13][10, 0][12, 7][17, 3]
        19 {0=1, 3=1, 18=1}							[1, 11][3, 13][10, 0][12, 7][17, 3][19, 18]
        23 {0=1, 3=1}								[1, 11][3, 13][10, 0][12, 7][17, 3][19, 18]
        23 {0=1, 3=1, 13=1}							[1, 11][3, 13][10, 0][12, 7][17, 3][19, 18][23, 13]
        24 {0=1, 3=1, 4=1, 13=1}					[1, 11][3, 13][10, 0][12, 7][17, 3][19, 18][23, 13]
        26 {0=1, 4=1, 13=1}							[1, 11][3, 13][10, 0][12, 7][17, 3][19, 18][23, 13]
        29 {0=1, 13=1}								[1, 11][3, 13][10, 0][12, 7][17, 3][19, 18][23, 13]
        30 {0=1}									[1, 11][3, 13][10, 0][12, 7][17, 3][19, 18][23, 13][30, 0]
         */
        return skyline;
    }

}

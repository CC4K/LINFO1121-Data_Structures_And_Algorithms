package Part2_Sorting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Author Pierre Schaus
 *
 * Given an array of (closed) intervals, you are asked to implement the union operation.
 * This operation will return the minimal array of sorted intervals covering exactly the union
 * of the points covered by the input intervals.
 * For example, the union of intervals [7,9],[5,8],[2,4] is [2,4],[5,9].
 * The Interval class allowing to store the intervals is provided
 * to you.
 *
 */
public class Union {

    /**
     * A class representing an interval with two integers. Hence the interval is
     * [min, max].
     */
    public static class Interval implements Comparable<Union.Interval> {

        public final int min;
        public final int max;

        public Interval(int min, int max) {
            assert(min <= max);
            this.min = min;
            this.max = max;
        }

        @Override
        public boolean equals(Object obj) {
            return ((Union.Interval) obj).min == min && ((Union.Interval) obj).max == max;
        }

        @Override
        public String toString() {
            return "["+min+","+max+"]";
        }

        @Override
        public int compareTo(Union.Interval o) {
            if (min < o.min) return -1;
            else if (min == o.min) return max - o.max;
            else return 1;
        }
    }

    /**
     * Returns the union of the intervals given in parameters.
     * This is the minimal array of (sorted) intervals covering
     * exactly the same points than the intervals in parameter.
     * 
     * @param intervals the intervals to unite.
     */
    public static Interval[] union(Interval[] intervals) {
        // TODO
        if (intervals.length == 0 || intervals.length == 1) return intervals;
        Arrays.sort(intervals);
        List<Interval> ret = new ArrayList<>();
        int past_min = intervals[0].min;
        int past_max = intervals[0].max;
        for (int i = 1; i < intervals.length; i++) {
            // [2, 4], [5, 8], [7, 9]
            //             ^       ^
            //            max   new_max
            //System.out.println(ret+" "+intervals[i]);
            if (intervals[i].min <= past_max || intervals[i].min == past_min) { // conflict
                if (intervals[i].max > past_max) {
                    past_max = intervals[i].max; // update max to prepare a new [min, max]
                }
            }
            else { // no conflict [1, 2], [3, 4]
                ret.add(new Interval(past_min, past_max));
                past_min = intervals[i].min;
                past_max = intervals[i].max;
            }
        }
        // + 1 run for last
        ret.add(new Interval(past_min, past_max));
        System.out.println(ret);
        return ret.toArray(new Interval[0]);
    }

    public static void main(String[] args) {
//        Interval i0 = new Interval(1, 2);
//        Interval i1 = new Interval(3, 4);
//        Interval[] result = union(new Interval[]{i0, i1});
//        System.out.println(Arrays.toString(result) +" = "+ Arrays.toString(new Interval[]{new Interval(1, 2), new Interval(3, 4)}));

        Interval i1 = new Union.Interval(2, 4);
        Interval i2 = new Union.Interval(3, 4);
        Union.Interval i3 = new Union.Interval(5, 6);
        Union.Interval i5 = new Union.Interval(6, 8);
        Union.Interval i4 = new Union.Interval(6, 9);
        Interval i0 = new Union.Interval(10, 10);
        Interval[] result = Union.union(new Union.Interval[]{i0, i1, i2, i3, i4, i5});
        System.out.println(Arrays.toString(result) +" = "+ Arrays.toString(new Interval[]{new Interval(2, 4), new Interval(5, 9), new Interval(10, 10)}));
    }

}

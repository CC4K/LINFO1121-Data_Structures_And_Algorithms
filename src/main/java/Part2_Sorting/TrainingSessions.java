package Part2_Sorting;

import java.util.Arrays;

/**
 * The Olympic Games organizers need to allocate facilities for the athletes' training sessions.
 * Each team has a schedule of training sessions with a start and end time
 * <p>
 * To organize the athletes' training smoothly, the organizing committee must know
 * the minimum number of facilities they need so that the teams can train without overlap.
 * Each team has given the organizers their training slots,
 * represented by two integers timestamps: the start (included) and end time (not included!) of their session,
 * Given the training sessions' time, write the `minFacilitiesRequired` function,
 * which returns the minimum number of required facilities.
 * <p>
 * Example Input with its visual representation:
 * <pre>
 * int[][] sessions = {
 *     {12, 20},//   --------
 *     {14, 25},//     -----------
 *     {19, 22},//          ---
 *     {25, 30},//                -----
 *     {26, 30},//                 ----
 * };
 * </pre>
 * In this example, the minimum number of facilities required is 3 as at time 19, there are 3 sessions (intervals) overlapping,
 * namely [12, 20), [14, 25), and [19, 22).
 * <p>
 * More formally, the goal is to minimize k such that for all time t, the number of sessions that overlap at time t is at most k
 * <p>
 * The computation must be performed in O(n.log(n)) time complexity where n is the number of training sessions.
 */
public class TrainingSessions {

    /**
     * Determines the minimum number of facilities required to accommodate all the training sessions without overlap.
     * @param sessions a non-null array of int arrays where each int array represents a session with start time and end time.
     * @return the minimum number of facilities required.
     */
    public int minFacilitiesRequired(int[][] sessions) {
        // TODO
        // {1, 4}, //  ---
        // {2, 5}, //   ---
        // {3, 6}, //    ---
        // {7, 8}, //        -
        // {9, 12},//          ---
        //System.out.println(Arrays.deepToString(sessions));

        Event[] events = new Event[sessions.length*2];
        for (int i = 0; i < sessions.length; i++) {
            events[i] = new Event(sessions[i][0], 1);
            events[sessions.length+i] = new Event(sessions[i][1], -1);
        }
        Arrays.sort(events, (e1, e2) -> (e1.t != e2.t) ? e1.t - e2.t : e1.start - e2.start);
        //System.out.println(Arrays.toString(events));

        int overlapping = 0;
        int max_facilities = 0;

        for (int i = 0; i < events.length; i++) {
            Event event = events[i];
            System.out.println(event);

            if (event.start == 1) { // event start => counter ++
                overlapping++;
            }
            else if (event.start == -1) { // event end => counter --
                overlapping--;
            }

            if (overlapping > max_facilities) max_facilities = overlapping;
            System.out.println(max_facilities);
        }

        return max_facilities;
    }

    static class Event {
        /// Time coordinate
        int t;
        /// Starting or ending of event
        int start;
        public Event(int t, int start) {
            this.t = t;
            this.start = start;
        }
        public String toString() {
            return String.format("%d[%s]", t, (start == 1) ? "start" : "end");
        }
    }

}

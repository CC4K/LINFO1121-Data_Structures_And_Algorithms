package Exam_training;

import java.util.*;

/**
 * Han Solo is planning a dangerously precise smuggling run along the Corellian Trade Spine. Imperial patrol ships
 * constantly drop in and out of hyperspace along the route, making certain moments far more hazardous than others.
 * <p>
 * Fortunately, Han has been keeping meticulous notes. He has recorded every patrol’s activity window across the day:
 * each patrol becomes active at a given start time and disengages at a given end time. A patrol is considered active
 * at time t if and only if start <= t < end.
 * <p>
 * Han also has a short list of preferred launch times. Before committing to launch, he asks you to determine which of
 * these times places the Millennium Falcon in the least danger. Since he wants to minimize his time in
 * this area, if there are several suitable times for the launch, it is best to choose the earliest possible one.
 * <p>
 * Inputs:
 *     · patrols      : an array of intervals [start, end), not necessarily sorted.
 *                      Each interval represents a patrol active for all t such that start <= t < end.
 *     · launchTimes  : an array of distinct launch timestamps, not necessarily sorted.
 * <p>
 * Output:
 *     · The launch time with the fewest active patrols. Ties are broken by choosing the earliest time.
 * <pre>
 * Example:
 *     · Inputs: - patrols = {
 *                   { 1, 4 },
 *                   { 0, 2 }
 *                 }
 *               - launchTimes = { 1, 2, 3 }
 *     · Output: 2
 *     · Explanation:
 *           Timeline:
 *                0    1    2    3    4
 *                     |-------------|   patrol A
 *                |--------|             patrol B
 *                     x    x    x       candidate launch times
 *
 *           At time 1: 2 active patrols
 *           At time 2: 1 active patrol
 *           At time 3: 1 active patrol
 *
 *           The minimal danger level is 1, and among {2, 3}, time 2 is earlier.
 * </pre>
 * Expected Time Complexity: O((N + T) * log(N + T)), where:
 *                               · N = number of patrol intervals
 *                               · T = number of candidate launch times
 * <p>
 * Hint: Try to imagine all patrol start times, end times, and launch times placed together on a single timeline.
 *       What happens to the number of active patrols as you move from left to right?
 */
public class QuietWindow {

    /**
     * Returns the launch time with the fewest active intervals.
     * An interval [start, end) is active at time t if start <= t < end.
     * If multiple launch times have the same minimum number of active intervals, the earliest (smallest) launch time is returned.
     * @param patrols    array of intervals [start, end), not necessarily sorted
     * @param launchTimes  array of distinct launch timestamps, not necessarily sorted
     * @return the launch time with the fewest active intervals (break ties by earliest time)
     */
    public static int bestLaunchTime(int[][] patrols, int[] launchTimes) {
        /*
         *   0    1    2    3    4
         *        |-------------|   patrol A
         *   |--------|             patrol B
         *        x    x    x       candidate launch times
         */
        // TODO: create events array (patrol starts, patrol ends, launches)
        List<int[]> events = new ArrayList<>();
        //Map<Integer, Integer> events = new TreeMap<>(); // <time, type event>
        for (int[] patrol : patrols) {
            int start = patrol[0];
            int end = patrol[1];
            events.add(new int[]{start, 1});
            events.add(new int[]{end, -1});
        }
        // also add launch times
        for (int launch : launchTimes) {
            events.add(new int[]{launch, 0});
        }
        events.sort((e1, e2) -> e1[0] - e2[0]);
        //for (int[] event : events) System.out.print(Arrays.toString(event)+" ");
        // [0, 1] [1, 1] [1, 0] [2, -1] [2, 0] [3, 0] [4, -1]

        // TODO: Sweep Algorithm (do NOT use structures, integers are more than enough !!!)
        // go through events and save dangerLevel
        int danger = 0;
        int minDanger = Integer.MAX_VALUE;
        int bestLaunchTime = -1;

        for (int[] event : events) {
            int time = event[0];
            int type = event[1];
            if (type == 1) { // patrol START
                danger++;
            }
            else if (type == -1) { // patrol END
                danger--;
            }
            else if (type == 0) { // LAUNCH, recording danger
                if (danger < minDanger) {
                    minDanger = danger;
                    bestLaunchTime = time;
                }
            }
        }
        //System.out.println("least dangerous moment at "+bestLaunchTime+" (danger = "+minDanger+")");
        return bestLaunchTime;
    }
}

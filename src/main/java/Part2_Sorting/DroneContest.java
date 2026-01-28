package Part2_Sorting;

import java.util.*;


/**
 * Context
 * --------
 * You have been contacted by an organizer of a drone artistic figure contest, because he is facing a problem that is a bit too complicated for his Excel sheet.
 * During the contest, several participants are going to make exhibitions of their drone and beautiful figures. Each participant had to reserve a time slot
 * before the contest and indicate the maximum height at which his drone will fly during his performance.
 * As these are powerful drones, they can fly very high, and this can cause problems for civil aviation. The organization in charge of the Belgian airspace,
 * Skeyes, therefore asks you to send them the maximum height used by the drones at any given time (the "profile").
 * You want it to be as accurate as possible, because any reservation costs money...
 * <p>
 * Example
 * -------
 * Let's take an example. Let's have the following reservations:
 * <pre>
 * +-------+-------+------+----------+
 * | Drone | Start | Stop | Altitude |
 * +-------+-------+------+----------+
 * | A     | 1     | 5    | 3        |
 * +-------+-------+------+----------+
 * | B     | 3     | 12   | 5        |
 * +-------+-------+------+----------+
 * | C     | 6     | 14   | 1        |
 * +-------+-------+------+----------+
 * | D     | 7     | 15   | 4        |
 * +-------+-------+------+----------+
 * | E     | 15    | 18   | 5        |
 * +-------+-------+------+----------+
 * | F     | 16    | 20   | 1        |
 * +-------+-------+------+----------+
 * | G     | 17    | 19   | 2        |
 * +-------+-------+------+----------+
 * </pre>
 * With these reservations, the profile is as follows:
 * <pre>
 * t ∈ [0,1[   ->  altitude = 0
 * t ∈ [1,3[   ->  altitude = 3
 * t ∈ [3,12[  ->  altitude = 5
 * t ∈ [12,15[ ->  altitude = 4
 * t ∈ [15,18[ ->  altitude = 5
 * t ∈ [18,19[ ->  altitude = 2
 * t ∈ [19,20[ ->  altitude = 1
 * t ∈ [20,∞[  ->  altitude = 0
 * </pre>
 * Details
 * -------
 * You need to implement a public static LinkedList<HeightChange> findHighest(Drone[] participants) method that takes as input an array of
 * Drone participants in the contest. Each drone is defined by a takeoff time (drone.start), landing time (drone.end) and a flight height (drone.height).
 * The drone is considered to be in flight during [drone.start,drone.end[.
 * You have the following properties:
 * <pre>
 *  1 ≤ drone.start < drone.end;
 *  1 ≤ drone.height;
 *  1 ≤ participants.length ≤100000.
 * </pre>
 *  As output, you need to provide the profile of the drones height as a function of time.
 *  This is a sequence of HeightChange objects that indicate that from the time change.time, the new maximum altitude is change.height.
 *  The HeightChange objects must be sorted by time (from smallest to largest time). The first change must have change.time=0.
 *  The last change must be when the last drone lands. Two successive changes must have different height.
 * <p>
 *  Your profile must be optimal. In other words, given two successive changes a and b,
 *  the maximum altitude of drones flying between a.time (inclusive) and b.time (exclusive) is EXACTLY a.height.
 *  Therefore, there is a drone flying at altitude a.height between these two times.
 */
public class DroneContest {

    /**
     * Given an array of participants (that starts their drones at a time given by drone.start (inclusive),
     * stops it at drone.end (exclusive) and goes at height drone.height),
     * output the heights changes for the use of Skeyes.
     * <p>
     * The first drone takes off strictly somewhere after time 0.
     * <p>
     * The height changes must be as described on INGInious.
     * Equivalently, as follows:
     * - They must be ordered by time
     * - The first height change must be at time 0, and at height 0.
     * - The last height change must be at the time the last drone stops (and thus must be at height 0!)
     * - Given two successive height changes A and B, the maximum height of any active drone between A.time (inclusive)
     * and B.time (exclusive) is EXACTLY A.height (i.e. there exists a drone with this height active between these
     * times). Moreover, A.height != B.height.
     */
    public static LinkedList<HeightChange> findHighest(Drone[] participants) {
        //System.out.println(Arrays.toString(participants));
        // [(1,5)[3], (3,12)[5], (6,14)[1], (7,15)[4], (15,18)[5], (16,20)[1], (17,19)[2]]
        // TODO
        // fist turn input into sorted list of Events (start/end at coordinates)
        Event[] events = new Event[participants.length*2];
        for (int i = 0; i < participants.length; i++) {
            Drone participant = participants[i];
            events[i] = new Event(participant.start, participant.height, 1);
            events[participants.length+i] = new Event(participant.end, participant.height, -1);
        }
        Arrays.sort(events, (e1, e2) -> (e1.t != e2.t) ? (e1.t - e2.t) : (e1.start - e2.start));
        //System.out.println(Arrays.toString(events));
        //[(1,3)[start],(3,5)[start],(5,3)[end],(6,1)[start],(7,4)[start],(12,5)[end],(14,1)[end],(15,4)[end],(15,5)[start],(16,1)[start],(17,2)[start],(18,5)[end],(19,2)[end],(20,1)[end]]


        // TODO : Sweep Algorithm with extra steps to keep track of not just the X but the Y/Height too (TREEMAP ARE GREAT)
        PriorityQueue<Integer> highest = new PriorityQueue<>((x, y) -> y - x);
        Map<Integer, Integer> heights = new HashMap<>(); // <height, height counter>
        LinkedList<HeightChange> ret = new LinkedList<>();

        highest.add(0); // always start at ground level
        heights.put(0, 1);
        int altitude = 0;
        ret.add(new HeightChange(0, 0));

        for (int i = 0; i < events.length; i++) {
            Event event = events[i];
            System.out.print(event+"\t".repeat((event.start == 1) ? 1 : 2));

            if (event.start == 1) { // a drone starts flying
                // increase counter for height
                highest.add(event.height);
                if (heights.containsKey(event.height)) heights.put(event.height, heights.get(event.height)+1);
                else heights.put(event.height, 1);
            }
            else if (event.start == -1) { // a drone stops flying
                // lower counter for height (and remove in PQ)
                heights.put(event.height, heights.get(event.height) - 1);
            }

            // remove inactive heights in PQ
            while (heights.get(highest.peek()) == 0) highest.poll();

            // if repetition of x coordinate => skip next part (but don't skip at the end => out of bounds)
            boolean skip = false;
            if (i < events.length-1 && events[i+1].t == event.t) skip = true;

            // if map has a height larger than max_recorded altitude => update else ignore
            int tallest = highest.peek();
            if (tallest != altitude && !skip) {
                ret.add(new HeightChange(event.t, tallest));
                altitude = tallest;
            }
            System.out.print("altitude: "+altitude+"\ttallest: "+tallest+"\t"+ret+"\n");
        }

        return ret;
    }

}

class HeightChange {
    public int time;
    public int height;
    public HeightChange(int t, int h) {
        time = t; height = h;
    }
    public String toString() {
        return "Time: " + time + ", Height: " + height;
    }
}

class Drone {
    public final int start;
    public final int end;
    public final int height;
    public Drone(int s, int e, int h) {
        start = s; end = e; height = h;
    }
    public String toString() {
        return String.format("(%d,%d)[%d]", start, end, height);
    }
}

class Event {
    public int t;
    public int height;
    public int start;
    public Event(int x, int y, int start) {
        this.t = x;
        this.height = y;
        this.start = start;
    }
    public String toString() {
        return String.format("(%d,%d)[%s]", t, height, (start == 1) ? "start" : "end");
    }
}

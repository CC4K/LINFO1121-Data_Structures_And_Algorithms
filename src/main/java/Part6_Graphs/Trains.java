package Part6_Graphs;

import java.util.*;

/**
 * Considering a list containing the relations between train stations (a train leaves the station `from` at `startTime` and arrives at station `to` at `endTime`)
 * and the positions of those stations, a starting station and a starting time, what is the earliest hour at which you can reach each an accessible station ?
 * <p>
 * You don't have to consider several points :
 * - passengers can leave a station at the exact moment where they reach this station
 * - all liaisons are direct
 * - timetable are not periodic, you don't have to repeat them everyday
 * - (startTime < endTime) and (from != to) are always true in all relations
 * - there is no duplicate entry (i.e. strictly equal relations)
 * <p>
 * As an example, let's consider that your list of relations between train stations is :
 * <pre>
 * {(Bxl-midi, 9:00 am)  : [(Namur, 9:20 am), (Charleroi, 9:30 am), (Ottiginies, 9:20 am)],
 * (Ottignies, 9:30 am)  : [(LLN, 9:40 am), (Charleroi, 9:50) am],
 * (Charleroi, 9:30 am)  : [(Namur, 9:45 am)],
 * (Charleroi, 9:50 am)  : [(Ottignies, 10:00 am)],
 * (Namur, 10:00 am)     : [(Charleroi, 10:30 am)],
 * (Ottignies, 10:00 am) : [(LLN, 10:20 am)]}
 * </pre>
 * In the above dictionary, the keys are the departure stations and times, and the values are a list of stations that you can reach
 * (if you take a train starting from the key) and the time at which you would reach them.
 * <p>
 * The list of reachable stations and the earliest hour you can reach them is :
 * <pre>
 * {Bxl-midi : 9:00 am,
 * Namur : 9:20 am,
 * Charleroi, 9:30 am,
 * Ottignies : 9:20 am,
 * LLN : 9:40 am}
 * </pre>
 * We leave notion of optimal/reasonable complexity unclear on purpose.
 * It is your job, based on your knowledge, to identify, among the appropriate algorithm family, which one is optimal.
 * <p>
 * Some clues :
 * - As you probably guessed it, it is clearly a graph problem but it isn't a usual graph since nodes are particular.
 *   Nodes don't represent only a point in space, but also a point in time (for example (Bruxelles-midi, 8:48 am)).
 * - Don't forget that if I reach Bxl-midi at time i, I can take any train that leaves Bxl-midi at time j >= i.
 * - Consider the function <a href="https://docs.oracle.com/javase/8/docs/api/java/util/TreeMap.html#subMap-K-boolean-K-boolean-">TreeMap.subMap</a> that might be useful ?
 */
public class Trains {

    /** 
     * Considering given starting station and time, compute the earliest hour at which
     * any accessible station can be reached.
     * @param relations a list of relations that connect a pair (station, time) (the key)
     *                  (for example, Bxl-midi, 8:48 am)
     *                  with a list of trains that leave the station at this time,
     *                  represented by a list of StationTime objects that gives for each,
     *                  the destination-station + time those trains arrives.
     *                  Stations are represented by Strings ("Bxl-midi") and
     *                  (absolute) time by positive integers.
     *
     * @param startPoint starting station/time
     * @return a map containing, for each reachable station (key the earliest hour at which it can be reached.
     *         The map must contain the starting station
     */
    public static Map<String, Integer> reachableEarliest(HashMap<StationTime, LinkedList<StationTime>> relations, StationTime startPoint) {
//        TreeMap<StationTime, LinkedList<StationTime>> sortedRelations = new TreeMap<>(
//                (s1, s2) -> (s1.station.compareTo(s2.station) != 0) ? s1.station.compareTo(s2.station) : (s1.time - s2.time)
//        );
        // convert HashMap into TreeMap to get subMap function
        TreeMap<StationTime, LinkedList<StationTime>> sortedRelations = new TreeMap<>();
        for (StationTime st : relations.keySet()) sortedRelations.put(st, relations.get(st));
        // TODO: BFS
        TreeMap<String, Integer> reachableStations = new TreeMap<>();
        Queue<StationTime> frontier = new LinkedList<>();

        reachableStations.put(startPoint.station, startPoint.time);
        frontier.add(startPoint);

        while (!frontier.isEmpty()) {
            StationTime st = frontier.poll();
            System.out.print(st+" => ");

            // TODO: Note: subMap from TreeMaps is MAGICAL
            // search schedules for current station that are still valid at current time
            Map<StationTime, LinkedList<StationTime>> search = sortedRelations.subMap(st, new StationTime(st.station, Integer.MAX_VALUE)); // from (Namur, 12) to (Namur, INF)
            System.out.println(search.entrySet());
            if (search.isEmpty()) continue;

            // for each schedule
            for (LinkedList<StationTime> schedule : search.values()) {
                // search in destinations
                for (StationTime destination : schedule) {
                    if (!reachableStations.containsKey(destination.station)) {
                        reachableStations.put(destination.station, destination.time);
                        frontier.add(destination);
                    }
                    else if (destination.time < reachableStations.get(destination.station)) {
                        reachableStations.put(destination.station, destination.time);
                        frontier.add(destination);
                    }
                }
            }
            System.out.println(reachableStations.entrySet());
        }
        System.out.println("All reachable destinations : "+reachableStations.entrySet());
        return reachableStations;
    }

    public static class StationTime implements Comparable<StationTime> {
        public final String station;
        public final int time;
        public StationTime(String station, int time) {
            this.station = station;
            this.time = time;
        }
        @Override
        public int hashCode() {
            return station.hashCode() ^ Integer.hashCode(~time);
        }
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof StationTime) return ((StationTime) obj).station.equals(station) && ((StationTime) obj).time == time;
            return false;
        }
        @Override
        public int compareTo(StationTime o) {
            //return (time - o.time != 0) ? (time - o.time) : (station.compareTo(o.station));
            return (station.compareTo(o.station) != 0) ? (station.compareTo(o.station)) : (time - o.time);
        }
        public String toString() {
            return String.format("(%s, %dm)", station, time);
        }
    }

}

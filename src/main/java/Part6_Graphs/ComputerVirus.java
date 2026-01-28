package Part6_Graphs;

import java.util.*;

/**
 * Proposed at the exam of August 2025.
 * <p>
 * A computer virus has infected a computer network.
 * Each node represents a server, and edges represent directed connections to other servers.
 * The virus starts at a given server (the “start server”) at time 0.
 * Each server takes a specific incubation time before it becomes infectious to adjacent servers.
 * Once a server becomes infectious, it immediately begins infecting its direct neighbours via outgoing edges.
 * <p>
 * Your task is to compute and return the earliest time each server becomes infectious.
 * <p>
 * The time complexity of your solution should be O(n + m log n)
 */
public class ComputerVirus {

    /**
     * Computes the earliest time each server in the network becomes infectious.
     * @param network a map representing the directed connections between servers
     * @param incubation a map representing the incubation time for each server
     * @param start the server where the virus starts
     * @return a map with the earliest time each server becomes infectious,
     *         if a server is not reachable, it should not be included in the result
     */
    public static Map<String, Integer> computeInfectionTimes(Map<String, List<String>> network, Map<String, Integer> incubation, String start) {
        //System.out.println(network); // {A=[B, C], B=[D], C=[D], D=[]} // start at A
        //System.out.println(incubation); // {A=2, B=3, C=1, D=4}
        // TODO: cleanest BFS with 1 source I have made
        Map<String, Integer> infection = new HashMap<>(); // <server, time of infection>
        Queue<String> frontier = new LinkedList<>();

        infection.put(start, incubation.get(start));
        frontier.add(start);

        while (!frontier.isEmpty()) {
            String server = frontier.poll();
            int infectious = infection.get(server);
            // check neighbours
            for (String neighbour : network.get(server)) {
                int new_infectious = infectious + incubation.get(neighbour);
                if (!infection.containsKey(neighbour)) {
                    infection.put(neighbour, new_infectious);
                    frontier.add(neighbour);
                }
                else {
                    int old_infectious = infection.get(neighbour);
                    if (new_infectious < old_infectious) {
                        infection.put(neighbour, new_infectious);
                    }
                }
            }
        }
        System.out.println(infection);
        return infection;
    }

}

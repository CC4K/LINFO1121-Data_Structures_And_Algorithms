package Part6_Graphs;

import java.util.*;

/**
 * The class TaskScheduler allows
 * to declare a set of tasks with their dependencies.
 * You have to implement the method:
 * <pre>{@code
 *      boolean isValid(List<String> schedule)
 * }</pre>
 * allowing to verify if the given schedule does
 * not violate any dependency constraint.
 * <p>
 * Example:
 * <pre>{@code
 *         TaskScheduler scheduler = new TaskScheduler();
 *         scheduler.addTask("A", Arrays.asList());
 *         scheduler.addTask("B", Arrays.asList("A"));
 *         scheduler.addTask("C", Arrays.asList("A"));
 *         scheduler.addTask("D", Arrays.asList("B", "C"));
 * }</pre>
 *    The dependency graph is represented as follows:
 * <pre>
 *          │─────► B │
 *        A─│         ├─────►D
 *          │─────► C │
 * </pre>
 * <pre>{@code
 *         assertTrue(scheduler.isValid(Arrays.asList("A", "B", "C", "D")));
 *         assertFalse(scheduler.isValid(Arrays.asList("A", "D", "C", "B"))); // D cannot be scheduled before B
 * }</pre>
 * Feel free to use existing java classes.
 */
public class TaskScheduler {
    private Map<String, List<String>> graph;

    public TaskScheduler() {
        this.graph = new HashMap<>();
    }

    /**
     * Adds a task with the given dependencies to the scheduler.
     * The task cannot be scheduled until all of its dependencies have been completed.
     */
    public void addTask(String task, List<String> dependencies) {
        this.graph.put(task, dependencies);
    }

    /**
     * Verify if the given schedule is valid, that is it does not violate the dependencies
     * and every task in the graph occurs exactly once in it.
     * The time complexity of the method should be in O(V+E) where
     * V = number of tasks, and E = number of requirements.
     * @param schedule a list of tasks to be scheduled in the order they will be executed.
     */
    public boolean isValid(List<String> schedule) {
        //System.out.println(graph.entrySet()); // [A=[], B=[A], C=[A], D=[B, C]]
        //System.out.println(schedule); // [A, B, C, D]
        // TODO
        Map<String, Boolean> valid = new HashMap<>();
        for (String task : graph.keySet()) valid.put(task, false);
        for (String task : schedule) {
            if (!graph.containsKey(task)) return false; // task does not exist => impossible
            if (valid.get(task)) return false; // task repeated => impossible
            for (String requirement : graph.get(task)) if (!valid.get(requirement)) return false; // requirement not fulfilled for task => impossible
            valid.put(task, true); // task can be done => mark and continue
        }
        //System.out.println(valid.entrySet()); // [A=true, B=true, C=true, D=true], [A=true, B=true, C=true, D=false]
        return !valid.containsValue(false); // missing task => impossible
    }
}

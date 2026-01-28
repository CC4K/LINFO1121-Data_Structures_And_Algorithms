package AOC_2025;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class Reactor {

    public static String[] readLines(String filename) throws IOException {
        FileReader fileReader = new FileReader(filename);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        List<String> lines = new ArrayList<>();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            lines.add(line);
        }
        bufferedReader.close();
        return lines.toArray(new String[0]);
    }

    public static void main(String[] args) {
        try {
            Path path = Paths.get(System.getProperty("user.dir"), "data", "AOC_2025.Reactor", "inputTest.txt");
            String[] testInput = readLines(path.toString());
            Path path1 = Paths.get(System.getProperty("user.dir"), "data", "AOC_2025.Reactor", "inputTest2.txt");
            String[] testInput2 = readLines(path1.toString());
            Path path2 = Paths.get(System.getProperty("user.dir"), "data", "AOC_2025.Reactor", "input.txt");
            String[] realInput = readLines(path2.toString());
            // ====================================================================================================== //
            System.out.println("Part 1 Test : "+part1(testInput).size());
            System.out.println("Part 1 Input : "+part1(realInput).size());
            // ====================================================================================================== //
            System.out.println("Part 2 Test : "+part2(testInput2));
            System.out.println("Part 2 Input : "+part2(realInput));
        }
        catch (IOException e) { System.out.println("Caught exception "+e); }
    }
    // ============================================================================================================== //
    private static long part2(String[] input) {
        // save graph in Map
        TreeMap<String, List<String>> graph = new TreeMap<>(); // <label, childrens>
        for (String line : input) {
            String[] split = line.split(" ");
            String label = split[0].substring(0, split[0].length()-1);
            List<String> childrens = new ArrayList<>(Arrays.asList(split).subList(1, split.length));
            graph.put(label, childrens);
        }
        //System.out.println(graph.entrySet());

        //dfs("svr", new ArrayList<>(), graph); // find paths using DFS
        // instead count nb of paths : svr -> dac -> fft -> out and svr -> fft -> dac -> out
        // paths svr -> dac -> fft -> out
        long svr_dac = countPaths("svr", "dac", new TreeMap<>(), graph);
        long dac_fft = countPaths("dac", "fft", new TreeMap<>(), graph);
        long fft_out = countPaths("fft", "out", new TreeMap<>(), graph);
        long type_path_1 = svr_dac * dac_fft * fft_out;
        // paths svr -> fft -> dac -> out
        long svr_fft = countPaths("svr", "fft", new TreeMap<>(), graph);
        long fft_dac = countPaths("fft", "dac", new TreeMap<>(), graph);
        long dac_out = countPaths("dac", "out", new TreeMap<>(), graph);
        long type_path_2 = svr_fft * fft_dac * dac_out;

        return type_path_1 + type_path_2;
    }
    private static long countPaths(String current, String target, TreeMap<String, Long> mem, TreeMap<String, List<String>> graph) {
        if (current.equals(target)) return 1;
        if (mem.containsKey(current)) return mem.get(current);
        long count = 0;
        if (graph.containsKey(current)) {
            for (String child : graph.get(current)) {
                count += countPaths(child, target, mem, graph);
            }
        }
        mem.put(current, count);
        return count;
    }
    // ============================================================================================================== //
    static List<List<String>> chemins = new ArrayList<>();
    private static List<List<String>> part1(String[] input) {
        chemins.clear();
        // save graph in Map
        TreeMap<String, List<String>> graph = new TreeMap<>(); // <label, childrens>
        for (String line : input) {
            String[] split = line.split(" ");
            String label = split[0].substring(0, split[0].length()-1);
            List<String> childrens = new ArrayList<>(Arrays.asList(split).subList(1, split.length));
            graph.put(label, childrens);
        }
        //System.out.println(graph.entrySet());
        dfs("you", new ArrayList<>(), graph); // find paths using DFS
        //System.out.println(chemins);
        return chemins;
    }
    private static void dfs(String currentNode, List<String> currentPath, TreeMap<String, List<String>> graph) {
        currentPath.add(currentNode); // add currentNode to currentPath
        if (currentNode.equals("out")) { // recursion case if end "out" is reached
            chemins.add(new ArrayList<>(currentPath)); // save path
            currentPath.remove(currentPath.size()-1); // return to before the currentNode back up
            return; // go back up
        }
        if (graph.containsKey(currentNode)) { // continue down the graph
            for (String child : graph.get(currentNode)) { // every child is the next node
                if (!currentPath.contains(child)) { // currentPath used to know if already visited
                    dfs(child, currentPath, graph);
                }
            }
        }
        currentPath.remove(currentPath.size()-1); // nothing here, return to before the currentNode back up
    }
}

package AOC_2025;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class Factory {

    public static String[] readLines(String filename) throws IOException {
        FileReader fileReader = new FileReader(filename);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        List<String> lines = new ArrayList<>();
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            lines.add(line);
        }
        bufferedReader.close();
        return lines.toArray(new String[0]);
    }

    public static void main(String[] args) {
        try {
            Path path = Paths.get(System.getProperty("user.dir"), "data", "AOC_2025.Factory", "inputTest.txt");
            String[] testInput = readLines(path.toString());
            Path path2 = Paths.get(System.getProperty("user.dir"), "data", "AOC_2025.Factory", "input.txt");
            String[] realInput = readLines(path2.toString());
            // ====================================================================================================== //
            System.out.println("Part 1 Test : "+part1(testInput));
            System.out.println("Part 1 Input : "+part1(realInput));
            // ====================================================================================================== //
            System.out.println("Part 2 Test : "+part2(testInput));
            //System.out.println("Part 2 Input : "+part2(realInput));
        }
        catch (IOException e) { System.out.println("Caught exception "+e); }
    }

    // ============================================================================================================== //

    private static int part1(String[] input) {
        int res = 0;
        for (String line : input) {
            String[] machine = line.split(" ");
            // parse lights
            int[] lights = new int[machine[0].length()-2];
            Arrays.fill(lights, 0);
            int[] target = new int[machine[0].length()-2];
            for (int i = 1; i < machine[0].length()-1; i++) {
                if (machine[0].charAt(i) == '.') target[i-1] = 0;
                else if (machine[0].charAt(i) == '#') target[i-1] = 1;
            }
            // parse buttons
            int[][] buttons = new int[machine.length-2][];
            for (int i = 1; i < machine.length-1; i++) {
                String[] button_split = machine[i].substring(1, machine[i].length()-1).split(",");
                buttons[i-1] = new int[button_split.length];
                for (int j = 0; j < button_split.length; j++) buttons[i-1][j] = Integer.parseInt(button_split[j]);
            }

            // init Dijsktra
            PriorityQueue<LightState> frontier = new PriorityQueue<>();
            Map<int[], Integer> min_costs = new TreeMap<>(Factory::arraysCompare);
            // TODO: NEEDS a TreeMap so it can compare int arrays together (HashMap grows infinitely, compares are broken)

            frontier.add(new LightState(lights, 0)); // 1st state : [0 0 0 0], current_cost : 0
            min_costs.put(lights, 0); // [0 0 0 0], min_cost : 0

            while (!frontier.isEmpty()) {
                LightState current = frontier.poll();
                // check target
                if (arraysEqual(current.lights, target)) {
                    System.out.println("=> "+Arrays.toString(current.lights) +" "+current.cost);
                    res += current.cost;
                    break;
                }
                // for each possible button available
                for (int[] button : buttons) {
                    // update lights
                    int[] new_lights = Arrays.copyOf(current.lights, current.lights.length);
                    for (int light_index : button) flipLightAtIndex(new_lights, light_index);
                    // update cost
                    int new_cost = current.cost + 1; // each button pressed increases the cost by 1
                    // if new state => add | if better state than existing one => update
                    if (!min_costs.containsKey(new_lights) || (min_costs.containsKey(new_lights) && new_cost < min_costs.get(new_lights))) {
                        min_costs.put(new_lights, new_cost);
                        frontier.add(new LightState(new_lights, new_cost));
                    }
                }
            }
        }
        return res;
}

    private static boolean arraysEqual(int[] a, int[] b) {
        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) return false;
        }
        return true;
    }

    private static int arraysCompare(int[] a, int[] b) {
        if (arraysEqual(a, b)) return 0;
        else return Arrays.compare(a, b);
    }

    private static void flipLightAtIndex(int[] lights, int index) {
        lights[index] = (lights[index] == 1) ? 0 : 1; // if 1 => 0 if 0 => 1
    }

    private static class LightState implements Comparable<LightState> {
        int[] lights;
        int cost;
        public LightState(int[] current_lights, int current_cost) {
            this.lights = current_lights;
            this.cost = current_cost;
        }
        @Override
        public int compareTo(LightState o) {
            return cost - o.cost;
        }
    }

    // ============================================================================================================== //

    // by ChatGPT but interesting gaussian solver it *nearly* works
    private static long part2(String[] input) {
        long totalCost = 0;
        for (String line : input) {
            String[] machine = line.split(" ");
            // joltages
            String[] joltage_split = machine[machine.length-1].substring(1, machine[machine.length-1].length()-1).split(",");
            int[] target = new int[joltage_split.length];
            for (int i = 0; i < joltage_split.length; i++) target[i] = Integer.parseInt(joltage_split[i]);

            // buttons
            int[][] buttons = new int[machine.length-2][];
            for (int i = 1; i < machine.length-1; i++) {
                String[] button_split = machine[i].substring(1, machine[i].length()-1).split(",");
                buttons[i-1] = new int[button_split.length];
                for (int j = 0; j < button_split.length; j++) {
                    buttons[i-1][j] = Integer.parseInt(button_split[j]);
                }
            }

            int N = target.length; // rows (equations)
            int M = buttons.length; // cols (variables)
            /*
            [.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
              a b c d e f
            [ 0 0 0 0 1 1 ] [ w ]   [ 3 ]
            [ 0 1 0 0 0 1 ] [ x ]   [ 5 ]
            [ 0 0 1 1 1 0 ] [ y ] = [ 4 ]
            [ 1 1 0 1 0 0 ] [ z ]   [ 7 ]
             */
            double[][] matrix = new double[N][M];
            for(int j=0; j<M; j++) {
                for(int light : buttons[j]) {
                    matrix[light][j] = 1.0;
                }
            }
            double[] b = new double[N];
            for(int i=0; i<N; i++) b[i] = target[i];

            long minPresses = Long.MAX_VALUE;

            // Iterate through all subset sizes k from 1 to min(N, M).
            // This handles singular matrices (by finding a smaller basis) and overdetermined systems.

            for (int k = 1; k <= Math.min(N, M); k++) {
                // 1. Pick k columns (variables to be non-zero)
                List<List<Integer>> colCombos = new ArrayList<>();
                getCombinations(M, k, 0, new ArrayList<>(), colCombos);

                for (List<Integer> cols : colCombos) {
                    // We have selected k columns. Now we have N equations and k variables.
                    // To solve, we need to pick k linearly independent equations (rows).

                    List<List<Integer>> rowCombos = new ArrayList<>();
                    getCombinations(N, k, 0, new ArrayList<>(), rowCombos);

                    for (List<Integer> rows : rowCombos) {
                        // Form k*k matrix
                        double[][] subA = new double[k][k];
                        double[] subB = new double[k];

                        for (int r = 0; r < k; r++) {
                            int originalRow = rows.get(r);
                            subB[r] = b[originalRow];
                            for (int c = 0; c < k; c++) {
                                int originalCol = cols.get(c);
                                subA[r][c] = matrix[originalRow][originalCol];
                            }
                        }

                        double[] sol = solveGaussian(subA, subB);

                        if (sol != null && isValidSolution(sol)) {
                            // Check if this solution satisfies ALL N equations (not just the k we picked)
                            if (checkAllRows(matrix, b, sol, cols)) {
                                long currentCost = 0;
                                for (double val : sol) currentCost += Math.round(val);
                                minPresses = Math.min(minPresses, currentCost);
                                // Optimization: If we found a valid solution for this set of columns,
                                // we don't need to check other row combinations for the same columns.
                                break;
                            }
                        }
                    }
                }
            }
            
            System.out.println(minPresses == Long.MAX_VALUE ? "No solution for "+line : "=> "+minPresses);

            if (minPresses != Long.MAX_VALUE) {
                totalCost += minPresses;
            }
        }
        return totalCost;
    }

    // Helper to generate combinations
    private static void getCombinations(int n, int k, int start, List<Integer> current, List<List<Integer>> result) {
        if (current.size() == k) {
            result.add(new ArrayList<>(current));
            return;
        }
        for (int i = start; i < n; i++) {
            current.add(i);
            getCombinations(n, k, i + 1, current, result);
            current.remove(current.size() - 1);
        }
    }

    // Gaussian elimination solver
    private static double[] solveGaussian(double[][] A, double[] b) {
        int n = A.length;
        double[][] M = new double[n][n + 1];
        for (int i = 0; i < n; i++) {
            System.arraycopy(A[i], 0, M[i], 0, n);
            M[i][n] = b[i];
        }

        for (int i = 0; i < n; i++) {
            int pivot = i;
            for (int j = i + 1; j < n; j++) {
                if (Math.abs(M[j][i]) > Math.abs(M[pivot][i])) pivot = j;
            }
            double[] temp = M[i]; M[i] = M[pivot]; M[pivot] = temp;

            if (Math.abs(M[i][i]) < 1e-9) return null; // Singular

            for (int j = i + 1; j <= n; j++) M[i][j] /= M[i][i];
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    for (int k = i + 1; k <= n; k++) M[j][k] -= M[j][i] * M[i][k];
                }
            }
        }
        double[] res = new double[n];
        for (int i = 0; i < n; i++) res[i] = M[i][n];
        return res;
    }

    private static boolean isValidSolution(double[] sol) {
        for (double v : sol) {
            long round = Math.round(v);
            if (Math.abs(v - round) > 1e-5 || round < 0) return false;
        }
        return true;
    }

    private static boolean checkAllRows(double[][] matrix, double[] b, double[] sol, List<Integer> cols) {
        for(int r=0; r<matrix.length; r++) {
            double sum = 0;
            for(int c=0; c<sol.length; c++) {
                sum += matrix[r][cols.get(c)] * Math.round(sol[c]);
            }
            if (Math.abs(sum - b[r]) > 1e-5) return false;
        }
        return true;
    }

}

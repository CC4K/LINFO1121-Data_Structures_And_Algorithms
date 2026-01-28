package AOC_2025;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class TrashCompactor {

    public static String[] readLines(String filename) throws IOException {
        FileReader fileReader = new FileReader(filename);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        List<String> lines = new ArrayList<>();
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            lines.add(line);
        }
        bufferedReader.close();
        return lines.toArray(new String[lines.size()]);
    }

    public static void main(String[] args) {
        try {
            Path path = Paths.get(System.getProperty("user.dir"), "data", "AOC_2025.TrashCompactor", "inputTest.txt");
            String[] testInput = readLines(path.toString());
            Path path2 = Paths.get(System.getProperty("user.dir"), "data", "AOC_2025.TrashCompactor", "input.txt");
            String[] realInput = readLines(path2.toString());
            // ====================================================================================================== //
            System.out.print("Part 1 Test : ");
            System.out.println(getTotal1(testInput));
            System.out.print("Part 1 Input : ");
            System.out.println(getTotal1(realInput));
            // ====================================================================================================== //
            for (String[] line : toStringList2(testInput)) System.out.println(Arrays.toString(line));
        }
        catch (IOException e) { System.out.println(e); }
    }

    // ============================================================================================================== //

    private static List<String[]> toStringList2(String[] input) {
        List<String[]> horizontalMatrix = new ArrayList<>();
        for (String s : input) {
            String[] line = s.trim().split("\\s+");
            horizontalMatrix.add(line);
        }
        return horizontalMatrix;
    }

    private static long getTotal1(String[] input) {
        List<String[]> horizontalMatrix = toStringList1(input);
        List<List<String>> homework = transpose1(horizontalMatrix);
        List<Long> results = calculateOperation1(homework);
        return results.stream().reduce(0L, (x, y) -> x + y);
    }

    private static List<Long> calculateOperation1(List<List<String>> homework) {
        List<Long> results = new ArrayList<>();
        for (List<String> operation : homework) {
            String op = operation.get(operation.size()-1);
            switch (op) {
                case "+" -> results.add(operation.stream()
                        .limit(operation.size() - 1)
                        .mapToLong(Long::parseLong)
                        .reduce(0, (x, y) -> x + y));
                case "*" -> results.add(operation.stream()
                        .limit(operation.size() - 1)
                        .mapToLong(Long::parseLong)
                        .reduce(1, (x, y) -> x * y));
            }
        }
        return results;
    }

    private static List<List<String>> transpose1(List<String[]> horizontalMatrix) {
        List<List<String>> homework = new ArrayList<>();
        for (int column = 0; column < horizontalMatrix.get(0).length; column++) {
            List<String> verticalOperation = new ArrayList<>();
            for (int line = 0; line < horizontalMatrix.size(); line++) {
                verticalOperation.add(horizontalMatrix.get(line)[column]);
            }
            homework.add(verticalOperation);
        }
        return homework;
    }

    private static List<String[]> toStringList1(String[] input) {
        List<String[]> horizontalMatrix = new ArrayList<>();
        for (String s : input) {
            String[] line = s.trim().split("\\s+");
            horizontalMatrix.add(line);
        }
        return horizontalMatrix;
    }

}

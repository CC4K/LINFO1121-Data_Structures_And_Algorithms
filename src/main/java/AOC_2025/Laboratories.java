package AOC_2025;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class Laboratories {

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
            Path path = Paths.get(System.getProperty("user.dir"), "data", "AOC_2025.Laboratories", "inputTest.txt");
            String[] testInput = readLines(path.toString());
            Path path2 = Paths.get(System.getProperty("user.dir"), "data", "AOC_2025.Laboratories", "input.txt");
            String[] realInput = readLines(path2.toString());
            // ====================================================================================================== //
            System.out.println("Part 1 Test : "+countSplits(testInput, true));
            System.out.println("Part 1 Input : "+countSplits(realInput, false));
            // ====================================================================================================== //
            System.out.println("Part 2 Test : "+countTimelines(testInput, false));
            System.out.println("Part 2 Input : "+countTimelines(realInput, false));
        }
        catch (IOException e) { System.out.println("Caught exception "+e); }
    }

    // ============================================================================================================== //

    private static long countTimelines(String[] input, boolean print) {
        // init
        Map<Integer, Long> currentTimelines = new HashMap<>(); // <Column, NTimelines>
        currentTimelines.put(input[0].indexOf('S'), 1L);
        for (int i = 1; i < input.length; i++) {
            String currentLine = input[i];
            Map<Integer, Long> nextTimelines = new HashMap<>();
            for (Map.Entry<Integer, Long> entry : currentTimelines.entrySet()) {
                int entryColumn = entry.getKey();
                long entryTimelines = entry.getValue();
                if (currentLine.charAt(entryColumn) == '^') {
                    nextTimelines.put(entryColumn-1, entryTimelines + nextTimelines.getOrDefault(entryColumn-1, 0L));
                    nextTimelines.put(entryColumn+1, entryTimelines + nextTimelines.getOrDefault(entryColumn+1, 0L));
                }
                else if (currentLine.charAt(entryColumn) == '.') {
                    nextTimelines.put(entryColumn, entryTimelines + nextTimelines.getOrDefault(entryColumn, 0L));
                }
            }
            currentTimelines = nextTimelines;
            if (print) System.out.println(currentTimelines.entrySet());
        }
        return currentTimelines.values().stream().reduce(0L, (x, y) -> x + y);
    }

    // ============================================================================================================== //

    private static int countSplits(String[] input, boolean print) {
        // init
        List<Integer> currentBeamIndexes = new ArrayList<>();
        currentBeamIndexes.add(input[0].indexOf('S'));
        if (print) System.out.print(input[0]+"\t");
        if (print) System.out.println(currentBeamIndexes);
        int countSplits = 0;
        for (int i = 1; i < input.length; i++) {
            String currentLine = input[i];
            String line = currentLine;
            int countSplitLine = 0;
            List<Integer> nextBeamIndexes = new ArrayList<>();
            for (int beamIndex : currentBeamIndexes) {
                if (line.charAt(beamIndex) == '^') {
                    countSplitLine++;
                    // expand beams indexes
                    nextBeamIndexes.add(beamIndex-1);
                    nextBeamIndexes.add(beamIndex+1);
                    // modify line
                    line = beamPlaceAtIndex(line, beamIndex-1);
                    line = beamPlaceAtIndex(line, beamIndex+1);
                }
                else if (line.charAt(beamIndex) == '.') {
                    nextBeamIndexes.add(beamIndex);
                    // just place a beam
                    line = beamPlaceAtIndex(line, beamIndex);
                }
                currentBeamIndexes = nextBeamIndexes;
            }
            currentLine = line;
            countSplits += countSplitLine;
            if (print) System.out.print(currentLine+"\t");
            if (print) System.out.println(new HashSet<>(currentBeamIndexes));
        }
        return countSplits;
    }
    private static String beamPlaceAtIndex(String line, int beamIndex) {
        StringBuilder newLine = new StringBuilder(line);
        newLine.setCharAt(beamIndex, '|');
        return newLine.toString();
    }

}

package AOC_2025;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class Cafeteria {

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
            Path path = Paths.get(System.getProperty("user.dir"), "data", "AOC_2025.Cafeteria", "inputTest.txt");
            String[] testInput = readLines(path.toString());
            Path path2 = Paths.get(System.getProperty("user.dir"), "data", "AOC_2025.Cafeteria", "input.txt");
            String[] realInput = readLines(path2.toString());
            // ====================================================================================================== //
            System.out.print("Part 1 Test : ");
            System.out.println(countFreshIngredients(testInput)); // 3
            System.out.print("Part 1 Input : ");
            System.out.println(countFreshIngredients(realInput)); // 679
            // ====================================================================================================== //
            System.out.print("Part 2 Test : ");
            System.out.println(countFreshIngredients2(testInput)); // 14
            System.out.print("Part 2 Input : ");
            System.out.println(countFreshIngredients2(realInput)); // 358155203664116
        }
        catch (IOException e) { System.out.println(e); }
    }

    // ============================================================================================================== //

    private static int countFreshIngredients(String[] lines) {
        // store all ranges and sort them first
        List<long[]> ranges = new ArrayList<>();
        int i = 0;
        while (!lines[i].isEmpty()) {
            String[] split = lines[i].split("-");
            long startRange = Long.parseLong(split[0]);
            long endRange = Long.parseLong(split[1]);
            ranges.add(new long[]{startRange, endRange});
            i++;
        }
        ranges.sort(Comparator.comparingLong(x -> x[0]));

        // merge in TreeMap
        TreeMap<Long, Long> mergedRanges = new TreeMap<>();
        long currentStart = ranges.get(0)[0];
        long currentEnd = ranges.get(0)[1];
        for (int j = 1; j < ranges.size(); j++) {
            long newStart = ranges.get(j)[0];
            long newEnd = ranges.get(j)[1];
            // check overlap
            if (newStart <= currentEnd) { // => [currentStart, largest End]
                currentEnd = Math.max(currentEnd, newEnd); // update End
            }
            else { // no overlap => just add to map
                mergedRanges.put(currentStart, currentEnd);
                // update for next iteration
                currentStart = newStart;
                currentEnd = newEnd;
            }
        }
        mergedRanges.put(currentStart, currentEnd); // last iteration (oops)
        //System.out.println(mergedRanges.entrySet());

        i++; // skip the blank line in input

        // check for every id if in ranges
        int count = 0;
        while (i < lines.length) {
            long id = Long.parseLong(lines[i]);
            // find entry RIGHT before
            Map.Entry<Long, Long> smallestEntry = mergedRanges.floorEntry(id); // floor(9) => 3-5 | floor(11) => 10-20
            // check if in range of entry
            if (smallestEntry != null && id <= smallestEntry.getValue()) {
                count++;
            }
            i++;
        }
        return count;
    }

    private static long countFreshIngredients2(String[] lines) {
        // store all ranges and sort them first
        List<long[]> ranges = new ArrayList<>();
        int i = 0;
        while (!lines[i].isEmpty()) {
            String[] split = lines[i].split("-");
            long startRange = Long.parseLong(split[0]);
            long endRange = Long.parseLong(split[1]);
            ranges.add(new long[]{startRange, endRange});
            i++;
        }
        ranges.sort(Comparator.comparingLong(x -> x[0]));

        // merge in TreeMap
        TreeMap<Long, Long> mergedRanges = new TreeMap<>();
        long currentStart = ranges.get(0)[0];
        long currentEnd = ranges.get(0)[1];
        for (int j = 1; j < ranges.size(); j++) {
            long newStart = ranges.get(j)[0];
            long newEnd = ranges.get(j)[1];
            // check overlap
            if (newStart <= currentEnd) { // => [currentStart, largest End]
                currentEnd = Math.max(currentEnd, newEnd); // update End
            }
            else { // no overlap => just add to map
                mergedRanges.put(currentStart, currentEnd);
                // update for next iteration
                currentStart = newStart;
                currentEnd = newEnd;
            }
        }
        mergedRanges.put(currentStart, currentEnd); // last iteration (oops)
        //System.out.println(mergedRanges.entrySet());

        // count fresh ids in all ranges
        long count = 0;
        for (Map.Entry<Long, Long> entry : mergedRanges.entrySet()) {
            Long start = entry.getKey();
            Long end = entry.getValue();
            count += (end - start) + 1;
        }
        return count;
    }

}

package AOC_2025;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class GiftShop {

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
            Path path1 = Paths.get(System.getProperty("user.dir"), "data", "AOC_2025.GiftShop", "inputTest.txt");
            Path path2 = Paths.get(System.getProperty("user.dir"), "data", "AOC_2025.GiftShop", "input.txt");
            String[] testInput = readLines(path1.toString());
            String[] realInput = readLines(path2.toString());
            // ====================================================================================================== //
            System.out.println("Part 1 Test: "+part1(testInput));
            System.out.println("Part 1 Input: "+part1(realInput));
            // ====================================================================================================== //
            System.out.println("Part 2 Test: "+part2(testInput));
            System.out.println("Part 2 Input: "+part2(realInput));
        }
        catch (IOException e) { System.out.println(e); }
    }

    // ============================================================================================================== //

    private static long part2(String[] input) {
        long sum = 0;
        String[] ranges = input[0].split(",");
        for (String range : ranges) {
            String[] split = range.split("-");
            String start = split[0];
            String end = split[1];
            //System.out.println(start+"-"+end);
            for (String invalidId : findInvalidIDs(start, end)) sum += Long.parseLong(invalidId);
        }
        return sum;
    }

    private static List<String> findInvalidIDs(String start, String end) {
        // search entire length of start
        // repetitions of size (divisors of start.length)
        // __ =>   2 blocs of 1 (1-1, 2-2, 3-3...)
        // ___ =>  3 blocs of 1 (1-1-1, 2-2-2...)
        // ____ => 4 blocs of 1 (1-1-1-1, 2-2-2-2, 3-3-3-3...) or 2 blocs of 2 (10-10, 11-11, 12-12...)
        // _____ =>5 blocs of 1 (1-1-1-1-1, 2-2-2-2-2...)
        // ______=>6 blocs of 1 (1-1-1-1-1-1...) or 3 blocs of 2 (10-10-10, 11-11-11...) or 2 blocs of 3 (100-100, 101-101...)
        List<String> invalid = new ArrayList<>();
        String current = start;
        // self-check first
        if (isARepeat(current)) invalid.add(current);
        // keep searching for next one until end
        while (true) {
            String next = getNextInvalid(current, end);
            if (next.equals("nope")) break;
            invalid.add(next);
            current = next;
        }
        //System.out.println(invalid);
        return invalid;
    }

    private static String getNextInvalid(String input, String end) {
        int startLen = input.length();
        int maxLen = end.length();
        // check all lengths
        for (int len = startLen; len <= maxLen; len++) {
            long minCandidate = -1;
            int[] divisors = getDivisorsOfLengthWithoutSelf(len);
            // check all block sizes
            for (int d : divisors) {
                long candidateVal = -1;
                if (len > startLen) {
                    // larger than at beginning
                    // smallest pattern is the smallest divisor number 10...0 (1 followed by d-1 zeros)
                    String chunk = "1"+"0".repeat(Math.max(0, d - 1));
                    candidateVal = Long.parseLong(chunk.repeat(Math.max(0, len / d)));
                }
                else {
                    // same length as beginning : try to form a pattern > currentVal
                    String chunk = input.substring(0, d);
                    long chunkVal = Long.parseLong(chunk);
                    // repeat current chunk
                    StringBuilder pat = new StringBuilder();
                    pat.append(chunk.repeat(Math.max(0, len / d)));
                    long val = Long.parseLong(pat.toString());
                    if (val > Long.parseLong(input)) {
                        candidateVal = val;
                    }
                    else {
                        // if too small, increment chunk
                        long nextChunkVal = chunkVal+1;
                        String nextChunk = String.valueOf(nextChunkVal);

                        // if incrementing increases the nb of digits (99 -> 100, 999 -> 1000) it won't fit in block size 'd' anymore
                        if (nextChunk.length() > d) continue;

                        pat = new StringBuilder();
                        pat.append(nextChunk.repeat(Math.max(0, len / d)));
                        candidateVal = Long.parseLong(pat.toString());
                    }
                }
                if (candidateVal != -1) {
                    if (minCandidate == -1 || candidateVal < minCandidate) {
                        minCandidate = candidateVal;
                    }
                }
            }
            if (minCandidate != -1) {
                if (minCandidate <= Long.parseLong(end)) return String.valueOf(minCandidate);
                return "nope"; // candidate > end
            }
        }
        return "nope";
    }

    private static int[] getDivisorsOfLengthWithoutSelf(int n) {
        List<Integer> divisors = new ArrayList<>();
        for (int i = n-1; i >= 1 ; i--) if (n % i == 0) divisors.add(i);
        divisors.sort((a, b) -> a - b);
        int[] res = new int[divisors.size()];
        for (int i = 0; i < divisors.size(); i++) res[i] = divisors.get(i);
        return res;
    }

    // ============================================================================================================== //

    private static long part1(String[] input) {
        return findRepetitionsIDs(input[0].split(","));
    }

    private static long findRepetitionsIDs(String[] ranges) {
        long sum = 0;
        for (String range : ranges) {
            String[] rangeSplit = range.split("-");
            String start = rangeSplit[0];
            String end = rangeSplit[1];
            //System.out.print(start+"-"+end+" : ");
            // get all possible repeated IDs in the range [start-end]

            if (isARepeat(start)) {
                //System.out.print(start+" ");
                sum += Long.parseLong(start);
            }
            String currentPattern = start;
            while (Long.parseLong(currentPattern) < Long.parseLong(end)) {
                String nextPattern = getNextPattern(currentPattern, Long.parseLong(end));
                if (nextPattern.equals("nope")) break;
                //System.out.print(nextPattern+" ");
                sum += Long.parseLong(nextPattern);
                currentPattern = nextPattern;
            }
            //System.out.println();
        }
        return sum;
    }

    private static String getNextPattern(String input, long end) {
        int len = input.length();
        if (len == 1) return "11";

        // if len is odd, nextPattern is even len : 220 => 1010
        if (len % 2 != 0) {
            String half = String.valueOf((long) Math.pow(10, ((len+1)/2) - 1));
            String next = half+half;
            if (Long.parseLong(next) <= end) {
                return next;
            }
            else return "nope";
        }
        // if len is even, use current half : 9756 => 9999
        else {
            String half = input.substring(0, len/2);
            String next = half+half;
            if (Long.parseLong(next) > Long.parseLong(input)) {
                if (Long.parseLong(next) <= end) return next;
                return "nope";
            }
            else {
                half = String.valueOf(Long.parseLong(half)+1);
                next = half+half;
                if (Long.parseLong(next) <= end) return next;
                return "nope";
            }
        }
    }

    private static boolean isARepeat(String input) {
        int len = input.length();
        if (len < 2) return false;
        // just check if first half == second half
        // check all divisors
        for (int i = 1; i <= len / 2; i++) {
            if (len % i == 0) {
                String bit = input.substring(0, i);
                StringBuilder str = new StringBuilder();
                int repeats = len/i;
                str.append(bit.repeat(repeats));
                if (str.toString().equals(input)) return true; // compare at every divide
            }
        }
        return false;
    }

}

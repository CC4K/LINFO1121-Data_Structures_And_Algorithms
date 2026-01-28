package AOC_2025;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class Lobby {

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
            Path path = Paths.get(System.getProperty("user.dir"), "data", "AOC_2025.Lobby", "inputTest.txt");
            String[] lines = readLines(path.toString());
            Path path2 = Paths.get(System.getProperty("user.dir"), "data", "AOC_2025.Lobby", "input.txt");
            String[] lines2 = readLines(path2.toString());
            // ====================================================================================================== //
            System.out.println("Part 1 Test :");
            System.out.println(lines[0]+" => "+ largestNb1(lines[0])+" =?= "+98);
            System.out.println(lines[1]+" => "+ largestNb1(lines[1])+" =?= "+89);
            System.out.println(lines[2]+" => "+ largestNb1(lines[2])+" =?= "+78);
            System.out.println(lines[3]+" => "+ largestNb1(lines[3])+" =?= "+92);
            int sum = 0;
            for (String line : lines2) sum += largestNb1(line);
            System.out.println("Part 1 Input : "+sum);
            // ====================================================================================================== //
            System.out.println("Part 2 Test :");
            System.out.println(lines[0]+" => "+ largestNb2(lines[0])+" =?= "+987654321111L);
            System.out.println(lines[1]+" => "+ largestNb2(lines[1])+" =?= "+811111111119L);
            System.out.println(lines[2]+" => "+ largestNb2(lines[2])+" =?= "+434234234278L);
            System.out.println(lines[3]+" => "+ largestNb2(lines[3])+" =?= "+888911112111L);
            long sum2 = 0;
            for (String line : lines2) sum2 += largestNb2(line);
            System.out.println("Part 2 Input : "+sum2);
        }
        catch (IOException e) { System.out.println(e); }
    }

    // ============================================================================================================== //

    private static int largestNb1(String bank) {
        for (int i = 99; i >= 11; i--) {
            int digit1 = i / 10; // 1st digit of i
            int digit2 = i % 10; // 2nd digit of i
            //System.out.println(i+" : "+digit1 + digit2);
            int index1 = bank.indexOf(String.valueOf(digit1));
            if (index1 != -1) { // if found digit1
                if (bank.indexOf(String.valueOf(digit2), index1+1) != -1) { // if found digit2 after index of digit1
                    return i;
                }
            }
        }
        return 0;
    }

    private static long largestNb2(String bank) {
        StringBuilder res = new StringBuilder();
        int currentIndex = -1;
        int digitsLeft = 12;

        for (int i = 0; i < 12; i++) {
            // find the largest digit first from 9 to 0
            for (int digit = 9; digit >= 1 ; digit--) {
                int indexLargest = bank.indexOf(String.valueOf(digit), currentIndex+1);
                if (indexLargest != -1 && (bank.length()-1-indexLargest) >= (digitsLeft-1-i)) { // if found larger digit after currentIndex
                    res.append(digit);
                    currentIndex = indexLargest;
                    break; // no need to go further in this loop
                }
            }
        }
        //System.out.println(res);
        return Long.parseLong(res.toString());
    }

}

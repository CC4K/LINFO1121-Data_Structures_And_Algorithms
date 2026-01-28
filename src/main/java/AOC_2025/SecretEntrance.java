package AOC_2025;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class SecretEntrance {

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
            Path path1 = Paths.get(System.getProperty("user.dir"), "data", "AOC_2025.SecretEntrance", "inputTest.txt");
            Path path2 = Paths.get(System.getProperty("user.dir"), "data", "AOC_2025.SecretEntrance", "input.txt");
            String[] testInput = readLines(path1.toString());
            String[] realInput = readLines(path2.toString());
            // ====================================================================================================== //
            System.out.println("Test: ");
            nbZeroStops(testInput);
            System.out.println("Input: ");
            nbZeroStops(realInput);
        }
        catch (IOException e) { System.out.println(e); }
    }

    // ============================================================================================================== //

    private static void nbZeroStops(String[] lines) {
        int zeroCount = 0; // part 1 (right on 0)
        int zeroPasses = 0; // part 2 (passing by 0)
        int current = 50; // starting position

        for (String line : lines) {
            char direction = line.charAt(0);
            int move = Integer.parseInt(line.substring(1));
            //System.out.print(current+" "+((direction == 'R') ? "+ "+move : "- "+move)+" = ");
            move = (direction == 'R') ? move : -move;

            // count revolutions
            if (move >= 0) {
                zeroPasses += (current + move) / 100;
            }
            else {
                zeroPasses -= move / 100;
                move = move % 100;
                if (current != 0 && (current + move <= 0)) zeroPasses++;
            }

            // find new position
            current = (current + move + 100) % 100;

            // count if on 0 (part 1)
            if (current == 0) zeroCount++;

            //System.out.println(current);
        }

        System.out.println("Part 1: "+zeroCount); // 1132
        System.out.println("Part 2: "+zeroPasses); // 6623
        // I HATE MODULOS
    }

}

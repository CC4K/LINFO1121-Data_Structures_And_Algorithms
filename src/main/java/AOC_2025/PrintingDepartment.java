package AOC_2025;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class PrintingDepartment {

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
            Path path = Paths.get(System.getProperty("user.dir"), "data", "AOC_2025.PrintingDepartment", "inputTest.txt");
            String[] lines = readLines(path.toString());
            Path path2 = Paths.get(System.getProperty("user.dir"), "data", "AOC_2025.PrintingDepartment", "input.txt");
            String[] lines2 = readLines(path2.toString());
            // ====================================================================================================== //
            System.out.print("Part 1 Test : ");
            System.out.println(countValid1(toMatrix(lines)));
            System.out.print("Part 1 Input : ");
            System.out.println(countValid1(toMatrix(lines2)));
            // ====================================================================================================== //
            System.out.print("Part 2 Test : ");
            System.out.println(countAllValid(toMatrix(lines)));
            System.out.print("Part 2 Input : ");
            System.out.println(countAllValid(toMatrix(lines2)));
        }
        catch (IOException e) { System.out.println(e); }
    }

    // ============================================================================================================== //

    private static int countAllValid(int[][] matrix) {
        int totalValid = 0;
        int currentCountValid = countValid2(matrix);
        while (currentCountValid > 0) {
            totalValid += currentCountValid;
            currentCountValid = countAllValid(matrix);
        }
        return totalValid;
    }

    private static int countValid2(int[][] matrix) {
        int rows = matrix.length;
        int columns = matrix[0].length;
        int valid = 0;
        for (int rowId = 0; rowId < rows; rowId++) {
            for (int columnId = 0; columnId < columns; columnId++) {
                if (!isValidSpace(matrix, rowId, columnId)) continue;
                int topLeft = (isValidSpace(matrix, rowId-1, columnId-1) ? matrix[rowId-1][columnId-1] : 0);
                int top = (isValidSpace(matrix, rowId-1, columnId) ? matrix[rowId-1][columnId] : 0);
                int topRight = (isValidSpace(matrix, rowId-1, columnId+1) ? matrix[rowId-1][columnId+1] : 0);
                int left = (isValidSpace(matrix, rowId, columnId-1) ? matrix[rowId][columnId-1] : 0);
                int right = (isValidSpace(matrix, rowId, columnId+1) ? matrix[rowId][columnId+1] : 0);
                int bottomLeft = (isValidSpace(matrix, rowId+1, columnId-1) ? matrix[rowId+1][columnId-1] : 0);
                int bottom = (isValidSpace(matrix, rowId+1, columnId) ? matrix[rowId+1][columnId] : 0);
                int bottomRight = (isValidSpace(matrix, rowId+1, columnId+1) ? matrix[rowId+1][columnId+1] : 0);
                int sumNeighbours = topLeft+top+topRight+left+right+bottomLeft+bottom+bottomRight;
                if (sumNeighbours < 4) {
                    valid++;
                    matrix[rowId][columnId] = 0; // remove roll
                }
            }
        }
        return valid;
    }

    private static int countValid1(int[][] matrix) {
        int rows = matrix.length;
        int columns = matrix[0].length;
        int valid = 0;
        for (int rowId = 0; rowId < rows; rowId++) {
            for (int columnId = 0; columnId < columns; columnId++) {
                if (!isValidSpace(matrix, rowId, columnId)) continue;
                int topLeft = (isValidSpace(matrix, rowId-1, columnId-1) ? matrix[rowId-1][columnId-1] : 0);
                int top = (isValidSpace(matrix, rowId-1, columnId) ? matrix[rowId-1][columnId] : 0);
                int topRight = (isValidSpace(matrix, rowId-1, columnId+1) ? matrix[rowId-1][columnId+1] : 0);
                int left = (isValidSpace(matrix, rowId, columnId-1) ? matrix[rowId][columnId-1] : 0);
                int right = (isValidSpace(matrix, rowId, columnId+1) ? matrix[rowId][columnId+1] : 0);
                int bottomLeft = (isValidSpace(matrix, rowId+1, columnId-1) ? matrix[rowId+1][columnId-1] : 0);
                int bottom = (isValidSpace(matrix, rowId+1, columnId) ? matrix[rowId+1][columnId] : 0);
                int bottomRight = (isValidSpace(matrix, rowId+1, columnId+1) ? matrix[rowId+1][columnId+1] : 0);
                int sumNeighbours = topLeft+top+topRight+left+right+bottomLeft+bottom+bottomRight;
                if (sumNeighbours < 4) valid++;

            }
        }
        return valid;
    }

    private static boolean isValidSpace(int[][] matrix, int row, int column) {
        return !(row < 0 || row >= matrix.length || column < 0 || column >= matrix[0].length || matrix[row][column] != 1);
    }

    private static int[][] toMatrix(String[] input) {
        int rows = input.length;
        int columns = input[0].length();
        int[][] matrix = new int[rows][columns];
        for (int rowId = 0; rowId < rows; rowId++) {
            for (int columnId = 0; columnId < columns; columnId++) {
                matrix[rowId][columnId] = (input[rowId].charAt(columnId) == '@') ? 1 : 0;
            }
        }
        return matrix;
    }

}

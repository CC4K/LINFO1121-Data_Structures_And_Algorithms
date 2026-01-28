package AOC_2025;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class ChristmasTreeFarm {

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
            Path path = Paths.get(System.getProperty("user.dir"), "data", "AOC_2025.ChristmasTreeFarm", "inputTest.txt");
            String[] testInput = readLines(path.toString());
            Path path2 = Paths.get(System.getProperty("user.dir"), "data", "AOC_2025.ChristmasTreeFarm", "input.txt");
            String[] realInput = readLines(path2.toString());
            // ====================================================================================================== //
            // storing
            TreeMap<Integer, List<Shape>> boxes = new TreeMap<>();
            List<Region> regions = new ArrayList<>();
            // parsing
            for (int i = 0; i < testInput.length; i++) {
                String line = testInput[i];
                // store box ids
                if (line.contains(":") && !line.contains("x")) {
                    int boxID = Integer.parseInt(line.substring(0, line.length()-1));
                    String[] nextLine1 = testInput[i+1].split("");
                    String[] nextLine2 = testInput[i+2].split("");
                    String[] nextLine3 = testInput[i+3].split("");
                    Shape box = new Shape(new String[][]{nextLine1, nextLine2, nextLine3});
                    List<Shape> shapes = getAllShapes(box);
                    boxes.put(boxID, shapes);
                    i += 4;
                }
                // store regions
                else if (line.contains(":") && line.contains("x")) {
                    String[] split = line.split(" ");
                    String[] dim = split[0].split("x");
                    int[] presents = new int[]{Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]), Integer.parseInt(split[4]), Integer.parseInt(split[5]), Integer.parseInt(split[6])};
                    regions.add(new Region(Integer.parseInt(dim[0]), Integer.parseInt(dim[1].substring(0, dim[1].length()-1)), presents));
                }
            }
            // printing
//            for (Map.Entry<Integer, List<Shape>> entry : boxes.entrySet()) {
//                System.out.println(entry.getKey());
//                for (Shape s : entry.getValue()) System.out.println(s);
//            }
//            System.out.println(regions);
            // TODO: new idea : just check enough space for all '#' in the grid


            // TODO: idea : start with the easiest boxes shapes first to determine faster if possible or not
            Region region1 = regions.get(1);
            int[] presents = regions.get(1).presents;
            String[][] grid = initRegion(region1.col, region1.line);
            printGrid(grid);
            if (solve(grid, region1.presents, boxes)) {
                System.out.println("OK");
                printGrid(grid);
            }
            else {
                System.out.println("NOPE");
            }
        }
        catch (IOException e) { System.out.println("Caught exception "+e); }
    }
    public static class Region {
        int col;
        int line;
        int[] presents;
        public Region(int col, int line, int[] presents) {
            this.col = col;
            this.line = line;
            this.presents = presents;
        }
        public String toString() {
            return String.format("%dx%d %s", col, line, Arrays.toString(presents));
        }
    }
    public static class Shape {
        String[][] shape;
        public Shape(String[][] shape) {
            this.shape = shape;
        }
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (String[] x : shape) sb.append(Arrays.toString(x)).append("\n");
            return sb.toString();
        }
    }
    // ============================================================================================================== //
    private static List<Shape> getAllShapes(Shape initBox) {
        List<Shape> ret = new ArrayList<>();
        int width = initBox.shape[0].length;
        int height = initBox.shape.length;
        String[][] currentShape = initBox.shape;
        ret.add(new Shape(currentShape));
        for (int i = 1; i < 4; i++) {
            String[][] nextShape = new String[height][width];
            for (int line = 0; line < height; line++) {
                for (int col = 0; col < width; col++) {
                    nextShape[col][height-1-line] = currentShape[line][col];
                }
            }
            ret.add(new Shape(nextShape));
            currentShape = nextShape;
        }
        return ret;
    }
    private static String[][] initRegion(int col, int line) {
        String[][] ret = new String[line][col];
        for (int i = 0; i < line; i++) Arrays.fill(ret[i], ".");
        return ret;
    }
    private static void printGrid(String[][] region) {
        for (String[] x : region) System.out.println(Arrays.toString(x));
    }

    private static boolean solve(String[][] grid, int[] presents, TreeMap<Integer, List<Shape>> boxes) {
        // 1. Find the first empty cell (targetR, targetC)
        int targetR = -1, targetC = -1;
        boolean foundEmpty = false;

        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                if (grid[r][c].equals(".")) {
                    targetR = r;
                    targetC = c;
                    foundEmpty = true;
                    break;
                }
            }
            if (foundEmpty) break;
        }
        System.out.println("target = "+targetR+" "+targetC);
        // Base Case: If no empty cells are left, we succeeded!
        if (!foundEmpty) return true;

        // 2. Try to fill this empty cell with an available present
        for (int id = 0; id < presents.length; id++) {
            if (presents[id] > 0) {
                presents[id]--; // Take the present

                // Try all 4 rotations of this present
                List<Shape> shapes = boxes.get(id);
                for (Shape sh : shapes) {
                    String[][] shape = sh.shape;
                    // We must place the shape such that one of its '#' parts covers (targetR, targetC).
                    // Iterate through all '#' cells of the shape to use as the "anchor".
                    for (int sr = 0; sr < shape.length; sr++) {
                        for (int sc = 0; sc < shape[0].length; sc++) {
                            if (shape[sr][sc].equals("#")) {
                                // Calculate where the top-left of the shape would be
                                int r = targetR - sr;
                                int c = targetC - sc;
                                System.out.println(r+" "+c);
                                if (canPlace(grid, shape, r, c)) {
                                    place(grid, shape, r, c, String.valueOf(id));

                                    // Recurse
                                    if (solve(grid, presents, boxes)) return true;

                                    // Backtrack
                                    remove(grid, shape, r, c);
                                }
                            }
                        }
                    }
                }

                presents[id]++; // Put the present back
            }
        }

        return false; // No solution found from this state
    }

    // Checks if a shape fits at grid position (r, c) without going out of bounds or overlapping
    private static boolean canPlace(String[][] grid, String[][] shape, int r, int c) {
        printGrid(grid);
        // Iterate over the shape's local coordinates
        for (int sr = 0; sr < shape.length; sr++) {
            for (int sc = 0; sc < shape[0].length; sc++) {
                // Only check if the shape actually has a block here
                if (shape[sr][sc].equals("#")) {
                    int gr = r + sr;
                    int gc = c + sc;
                    // Check bounds
                    if (gr < 0 || gr >= grid.length || gc < 0 || gc >= grid[0].length) {
                        return false;
                    }
                    // Check collision: grid cell must be empty
                    if (!grid[gr][gc].equals(".")) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    // Places the shape into the grid using the ID
    private static void place(String[][] grid, String[][] shape, int r, int c, String id) {
        for (int sr = 0; sr < shape.length; sr++) {
            for (int sc = 0; sc < shape[0].length; sc++) {
                if (shape[sr][sc].equals("#")) {
                    grid[r + sr][c + sc] = id;
                }
            }
        }
    }

    // Removes the shape from the grid (sets back to '.')
    private static void remove(String[][] grid, String[][] shape, int r, int c) {
        for (int sr = 0; sr < shape.length; sr++) {
            for (int sc = 0; sc < shape[0].length; sc++) {
                if (shape[sr][sc].equals("#")) {
                    grid[r + sr][c + sc] = ".";
                }
            }
        }
    }

}

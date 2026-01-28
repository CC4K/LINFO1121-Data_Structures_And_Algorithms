package Exam_training;

import org.javagrader.Grade;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


@Grade
public class StarDestroyerNavigatorTest {

    private static double delta = 1e-10;

    @Test
    @Grade(value =1)
    public void testSimple() {
        int[][] neighbors = {
                { 1, 2 },
                { 0, 2 },
                { 0, 1, 3 },
                { 2 }
        };

        double[][] intensity = {
                { 0.9, 0.6 },
                { 0.9, 0.8 },
                { 0.6, 0.8, 0.3 },
                { 0.3 }
        };

        double factor = StarDestroyerNavigator.navigate(neighbors, intensity, 0, 3);

        assertEquals(0.216, factor, delta);
    }

}

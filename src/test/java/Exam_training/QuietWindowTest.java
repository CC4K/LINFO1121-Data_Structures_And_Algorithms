package Exam_training;

import org.javagrader.Grade;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


@Grade
public class QuietWindowTest {

    @Test
    @Grade(value=1)
    public void testSimple() {
        int[][] patrols = {
                { 1, 4 },
                { 0, 2 }
        };
        int[] launchTimes = { 1, 2, 3};

        int results = QuietWindow.bestLaunchTime(patrols, launchTimes);

        assertEquals(2, results);
    }

}

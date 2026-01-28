package Exam_training;

import org.javagrader.Grade;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


@Grade
public class UnfortunateDisassemblyTest {

    @Test
    @Grade(value=1)
    public void testSimple() {
        int[][] blueprint = {
                { 1 },
                { 2 },
                { 3 },
                { 1 },
                { 2 },
                { 3 }
        };
        int[] core = { 0, 5 };
        int[] prices = { 4, 5, 2, 7, 6, 3 };

        int cost = UnfortunateDisassembly.repairCost(blueprint, core, prices);

        assertEquals(6, cost);
    }


}

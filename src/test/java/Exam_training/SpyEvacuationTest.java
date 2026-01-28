package Exam_training;

import org.javagrader.Grade;
import org.junit.jupiter.api.Test;

import java.util.*;
import static org.junit.jupiter.api.Assertions.*;


@Grade
public class SpyEvacuationTest {

    @Test
    @Grade(value =1)
    public void testSimple() {

        double[] target = { 0.0, 0.0 };
        double[][] bases = {
                { 1.0, 1.0 },
                { -1.0, 0.0 },
                { 3.0, 4.0 },
                { 0.5, -0.5 }
        };
        int k = 2;

        int[] result = SpyEvacuation.kClosestBases(target, bases, k);

        Arrays.sort(result);
        assertArrayEquals(new int[]{ 1, 3 }, result);
    }

}

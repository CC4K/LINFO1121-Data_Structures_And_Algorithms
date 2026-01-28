package Exam_training;

import org.javagrader.Grade;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


@Grade
public class SecretTransmissionAnalysisTest {

    @Test
    @Grade(value=1)
    public void testSimple() {
        String[] codes = {
                "ATCK",
                "BASE",
                "IMPE"
        };
        String transmission = "ATCKIMPEBASEATCK";

        int[] results = SecretTransmissionAnalysis.analyse(codes, transmission);

        assertArrayEquals(new int[]{ 2, 1, 1 }, results);
    }

}

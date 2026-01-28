package Part3_Search_Trees;

import org.javagrader.ConditionalOrderingExtension;
import org.javagrader.Grade;
import org.javagrader.GradeFeedback;
import org.javagrader.TestResultStatus;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@Grade
@ExtendWith(ConditionalOrderingExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BuggyBSTTest {


    @Test
    @Grade(value = 1)
    @Order(1)
    @GradeFeedback(message = "Your algorithm does not correctly fix the tree on the given example. Debug it locally", on = TestResultStatus.FAIL)
    public void testSimple1() {

        /*
         *  This is a problematic BST
         *
         *                      (49)
         *                     /    \
         *                   (32)*   (55)
         *                  /    \
         *                 (6)   (27)
         *                /        \
         *              (1)        (33)
         *
         */
        Node root = new Node(49);
        root.left = new Node(32);
        root.right = new Node(55);
        root.left.left = new Node(6);
        root.left.right = new Node(27);
        root.left.left.left = new Node(1);
        root.left.right.right = new Node(33);

        BuggyBST.fix(root);
        // The fix it to replacing key node (32) by any key between 6 and 27
        assertTrue(root.left.key < 27);
        assertTrue(root.left.key > 6);

        // Notice that we assume that you only changed one key and
        // let the structure of the tree untouched
        // We do not verify it here for keeping this test simple and readable
    }


    @Test
    @Grade(value = 1)
    @Order(1)
    @GradeFeedback(message = "Your algorithm does not correctly fix the tree on the given example. Debug it locally", on = TestResultStatus.FAIL)
    public void testSimple2() {

        /*
         *  This is a problematic BST
         *
         *                      (49)
         *                     /    \
         *                   (12)   (55)
         *                  /    \
         *                 (6)   (27)
         *                /
         *              (7*)
         *
         */
        Node root = new Node(49);
        root.left = new Node(12);
        root.right = new Node(55);
        root.left.left = new Node(6);
        root.left.right = new Node(27);
        root.left.left.left = new Node(7);

        BuggyBST.fix(root);
        // The fix it either to replace the key 7 by a key less than 6
        // or to replace the key 6 by a key greater than 7 and less than 12
        assertTrue(root.left.left.left.key < 6 || (root.left.left.key > 7 && root.left.left.key < 12));
        // Notice that we assume that you only changed one key and
        // let the structure of the tree untouched
        // We do not verify it here for keeping this test simple and readable
    }


}

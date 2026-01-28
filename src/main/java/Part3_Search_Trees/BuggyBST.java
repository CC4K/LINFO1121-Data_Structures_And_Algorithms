package Part3_Search_Trees;

import java.util.ArrayList;

public class BuggyBST {

    public static void fix(Node root) {
        ArrayList<Node> ordered = new ArrayList<>();
        infix(root, ordered);
        ordered.add(null);
        Node previous;
        Node current = null;
        Node next = ordered.get(0); // start at 1st elem
        for (int i = 1; i < ordered.size(); i++) {
            previous = current;
            current = next;
            next = ordered.get(i);
            if (previous != null && previous.key >= current.key) { // we should be larger than previous not smaller
                current.key = previous.key + 1;
                return;
            }
            if (next != null && current.key >= next.key && (previous == null || next.key > previous.key + 1)) { // we should be smaller than next not larger
                current.key = next.key - 1;
                return;
            }
        }
    }
    private static void infix(Node root, ArrayList<Node> output) {
        if (root == null) return;
        infix(root.left, output); // left
        output.add(root); // now
        infix(root.right, output); // then right
    }
}


/**
 * Node class represents a single node in the binary search tree, with key, value, left and right children,
 * to keep track of the number of nodes in the subtree rooted at this node.
 * Do not change this class.
 */
class Node {
    int key;
    Node left, right;
    public Node(Integer key) {
        this.key = key;
    }
    public String toString() {
        return String.format("(%d)", key);
    }
}

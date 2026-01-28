package Exam_training;


/**
 * Santa needs to calculate the median price of gifts he will deliver this year.
 * The gift prices are stored in a unique data structure known as the 'magical Christmas search tree'.
 * <p>
 * Each node in this tree represents a gift price (as the key) and the quantity of gifts at that price (as the value).
 * The goal is to implement two methods:
 * - put (to add gift prices to the tree) and
 * - median (to find the median price of the gifts).
 * <p>
 * For example, consider the following magical Christmas search tree:
 * <pre>
 *                     [150, 4]
 *                      /     \
 *                     /       \
 *                    /         \
 *                   /           \
 *              [100, 10]       [300, 2]
 *                               /   \
 *                              /     \
 *                             /       \
 *                            /         \
 *                         [200, 8]     [500, 1]
 * </pre>
 * This tree represents a total of 25 gifts. The median price is the 13th price in the sorted list of gift prices.
 * In this example, the sorted list of prices is:
 * 100 (10 times), 150 (4 times), 200 (8 times), 300 (2 times), 500 (once). The 13th price in this list is 150.
 * Thus, the median price of the gifts is 150.
 * <p>
 * Note: It's assumed that the total number of gifts is always an odd number.
 * <p>
 * Hint: you may need to add a size attribute to the Node class to keep track of the total number of gifts in the subtree.
 */

public class SantaInventory {
    private Node root; // root of BST

    private class Node {
        private int toyPrice; // Price of the toy
        private int count; // Number of time a toy with price `toyPrice` has been added in the tree
        private Node left, right; // left and right subtrees
        private int size;
        public String toString() {
            return String.format("[%d,%d]", toyPrice, count);
        }
    }

    /**
     * Inserts a new toy price into the magical Christmas search tree or updates the count of an existing toy price.
     * This method is part of the implementation of the magical Christmas search tree where each node
     * represents a unique toy price and the number of toys available at that price.
     * <p>
     * If the tree already contains the toy price, the method updates the count of toys at that price.
     * If the toy price does not exist in the tree, a new node with the toy price and count is created.
     * @param toyPrice The price of the toy to be added or updated in the tree.
     * @param count    The number of toys added to the magical tree. If the toy price already exists,
     *                 this count is added to the existing count.
     */
    public void put(int toyPrice, int count) {
        // TODO: build a subtree to add
        root = put(root, toyPrice, count);
    }
    private Node put(Node node, int toyPrice, int count) {
        if (node == null) { // place new node here
            node = new Node();
            node.toyPrice = toyPrice;
            node.count = count;
            node.size = count;
            return node;
        }
        if (toyPrice < node.toyPrice) { // go left
            node.size += count;
            node.left = put(node.left, toyPrice, count);
        }
        else if (toyPrice == node.toyPrice) { // update count
            node.count += count;
            node.size += count;
        }
        else if (toyPrice > node.toyPrice) { // go right
            node.size += count;
            node.right = put(node.right, toyPrice, count);
        }
        return node;
    }

    /**
     * Calculates the median price of the toys in the magical Christmas search tree.
     * <p>
     * The median is determined by the size of the tree. If the tree is empty, it throws an IllegalArgumentException.
     * <p>
     * Note: The method assumes that the total number of toys (the sum of counts of all prices) is odd.
     * The median is the price at the middle position when all toy prices are listed in sorted order.
     * @return The median price of the toys.
     * @throws IllegalArgumentException if the tree is empty.
     */
    public int median() {
        if (root == null) throw new IllegalArgumentException("No thoughts. Head empty");
        // TODO: search node with size == (root.size / 2) + 1
        return median(root, (root.size / 2) + 1);
    }
    private int median(Node node, int targetSize) {
        // choose direction based on current skipped amount on your left
        int leftSize = node.left == null ? 0 : node.left.size;
        int skipped = leftSize + node.count;

        if (targetSize < leftSize) { // go left
            return median(node.left, targetSize);
        }
        else if (targetSize > skipped) { // go right
            return median(node.right, targetSize - skipped);
        }
        else { // target reached
            return node.toyPrice;
        }
    }
}

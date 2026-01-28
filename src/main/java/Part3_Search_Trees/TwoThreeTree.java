package Part3_Search_Trees;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// For a detailed description of the tasks, see the video explanations: https://youtu.be/15pRnqHv7F4

public class TwoThreeTree {

    // Node of the 2-3 tree: remember: it can be a 2-node or a 3-node
    static class Node {
        int[] keys = new int[3];     // Up to 3 during split
        Node[] children = new Node[4]; // Up to 4 children during split
        int n; // Number of keys actually in use (1 or 2, or 3 during split)
        int sizeNode = 1; // Number of nodes in the subtree
        int sizeKey = this.n; // Number of keys in the subtree
        boolean isLeaf() {
            return children[0] == null;
        }
    }

    // Helper for propagating splits up the tree
    private static class Split {
        Node left, right;
        int middle;
        Split(Node left, int middle, Node right) {
            this.left = left;
            this.middle = middle;
            this.right = right;
        }
    }

    private Node root; // reference to the root of the tree

    /**
     * Constructs a 2-3 tree from its serialized representation.
     * The time complexity should be O(n) where n is the number of keys in the tree.
     * The tree reconstructed should be identical to the original tree (structure and keys).
     * @param serialized the serialized array of integers representing the tree
     */
    public TwoThreeTree(Integer[] serialized) {
        // TODO 3
        //System.out.println(Arrays.toString(serialized));
        // {1, 2, 3, 4, 5, 6} => [2, 2, 4, 1, 1, 1, 3, 2, 5, 6]
        // la liste des noeauds sérialisés => arbre 2-3
        this.root = deserialize(serialized, 0);
    }

    public TwoThreeTree() {
        this.root = null;
    }

    public Node getRoot() {
        return root;
    }

     private Node deserialize(Integer[] serialized, int index) {
         if (index >= serialized.length) return root; // end of array
         int node_size = serialized[index];
         //System.out.println("size of node : "+node_size);
         if (node_size == 1) {
             // create a 2-node
             int val = serialized[index+1];
             //System.out.println("create 2-node : "+val);
             insert(val);
             index += 2;
         }
         else if (node_size == 2) {
             // create a 3-node
             int val1 = serialized[index+1];
             int val2 = serialized[index+2];
             //System.out.println("create 3-node : "+val1+" "+val2);
             insert(val1);
             insert(val2);
             index += 3;
         }
         return deserialize(serialized, index);
     }

    /**
     * Inserts a key into the 2-3 tree. If the key already exists, it is ignored.
     * @param key the key to insert
     */
    public void insert(int key) {
        if (contains(key)) return; // no duplicates
        if (root == null) {
            root = new Node();
            root.keys[0] = key;
            root.n = 1;
            return;
        }
        Split s = insert(root, key);
        if (s != null) {
            Node newRoot = new Node();
            newRoot.keys[0] = s.middle;
            newRoot.n = 1;
            newRoot.children[0] = s.left;
            newRoot.children[1] = s.right;
            root = newRoot;
        }
    }

    // Recursive insertion that may return a split result
    private Split insert(Node node, int key) {
        int i;
        if (node.isLeaf()) {
            // insert into leaf, keeping keys sorted
            i = node.n - 1;
            while (i >= 0 && key < node.keys[i]) {
                node.keys[i + 1] = node.keys[i];
                i--;
            }
            node.keys[i + 1] = key;
            node.n++;
        } else {
            // find child to descend into
            i = 0;
            while (i < node.n && key > node.keys[i]) i++;
            Split split = insert(node.children[i], key);
            if (split != null) {
                // child was split, insert its middle into this node
                for (int j = node.n; j > i; j--) {
                    node.keys[j] = node.keys[j - 1];
                    node.children[j + 1] = node.children[j];
                }
                node.keys[i] = split.middle;
                node.children[i] = split.left;
                node.children[i + 1] = split.right;
                node.n++;
            }
        }


        // If node temporarily has 3 keys, split it into two 2-nodes
        if (node.n == 3) {
            // create left node and copy first key and children
            Node left = new Node();
            left.keys[0] = node.keys[0];
            left.children[0] = node.children[0];
            left.children[1] = node.children[1];
            left.n = 1;
            left.sizeNode = 1;
            left.sizeKey = left.n;
            if (!left.isLeaf()) {
                left.sizeNode += left.children[0].sizeNode;
                left.sizeNode += left.children[1].sizeNode;
                left.sizeKey += left.children[0].sizeKey;
                left.sizeKey += left.children[1].sizeKey;
            }


            // create right node and copy last key and children
            Node right = new Node();
            right.keys[0] = node.keys[2];
            right.children[0] = node.children[2];
            right.children[1] = node.children[3];
            right.n = 1;
            right.sizeNode = 1;
            right.sizeKey = right.n;
            if (!right.isLeaf()) {
                right.sizeNode += right.children[0].sizeNode;
                right.sizeNode += right.children[1].sizeNode;
                right.sizeKey += right.children[0].sizeKey;
                right.sizeKey += right.children[1].sizeKey;
            }

            node.sizeNode = 1 + left.sizeNode + right.sizeNode;
            node.sizeKey = node.n + left.sizeKey + right.sizeKey;

            return new Split(left, node.keys[1], right);
        }

        node.sizeKey = node.n;
        if (!node.isLeaf()) {
            for (i = 0; i < node.n; i++) {
                node.sizeNode += node.children[i].sizeNode;
                node.sizeKey += node.children[i].sizeKey;
            }
        }
        return null;
    }

    /**
     * Checks if the tree contains a given key.
     * @param key the key to search for
     * @return true if the key is found, false otherwise
     */
    public boolean contains(int key) {
        return search(root, key);
    }

    private boolean search(Node h, int key) {
        if (h == null) return false;
        int i;
        for (i = 0; i < h.n; i++) {
            if (key == h.keys[i]) return true;
            if (key < h.keys[i]) break;
        }
        return search(h.children[i], key);
    }

    public Integer[] toArray() {
        // TODO 1
        // parcours infix
        List<Integer> res = new ArrayList<>();
        toArrayRecurse(root, res);
        return res.toArray(new Integer[0]);
    }

    private void toArrayRecurse(Node node, List<Integer> result) {
        if (node == null) return; // leaf
        // go through all the keys
        for (int i = 0; i < node.n; i++) {
            toArrayRecurse(node.children[i], result); // infix
            result.add(node.keys[i]);
        }
        // not leaf yet => go children gogogogogogo
        toArrayRecurse(node.children[node.n], result);
    }

    public Integer[] serialize() {
        // TODO 2
        // parcours prefix avec taille du noeud en chiffre inséré juste avant le noeud en question
        List<Integer> res = new ArrayList<>();
        serializeRecurse(root, res);
        return res.toArray(new Integer[0]);
    }

    private void serializeRecurse(Node node, List<Integer> result) {
        if (node == null) return; // leaf
        // insere taillle du noeud devant
        System.out.println("taille node : "+node.n);
        System.out.println(Arrays.toString(node.keys));
        result.add(node.n);
        // go through all the keys
        for (int i = 0; i < node.n; i++) {
            result.add(node.keys[i]);
            serializeRecurse(node.children[i], result); // prefix
        }
        // not leaf yet => go children gogogogogogo
        System.out.println("res : "+result);
        serializeRecurse(node.children[node.n], result);
    }

}

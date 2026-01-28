package Part4_Maps_Strings;

import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Queue;

/**
 * We are interested in the implementation of an LRU cache,
 * i.e. a (hash)-map of limited capacity where the addition of
 * a new entry might induce the suppression of the Least Recently Used (LRU)
 * entry if the maximum capacity is exceeded.
 * <p>
 * Your LRU cache implements the same two methods as a MAP :
 * - put(key, elem) inserts the given element in the cache,
 *      this element becomes the most recently used element
 *      and if needed (the cache is full and the key not yet present),
 *      the least recently used element is removed.
 * - get(key) returns the entry with the given key from the cache,
 *      this element becomes the most recently used element
 * <p>
 * These methods need to be implemented with an expected time complexity of O(1).
 * You are free to choose the type of data structure that you consider
 * to best support this cache. You can also use data-structures from Java.
 * <p>
 * Hint for your implementation:
 *       Use a doubly linked list to store the elements from the least
 *       recently used (head) to the most recently used (tail).
 *       If needed the element to suppress is the head of the list.
 * <p>
 *       Use java.util.HashMap with the <key,node> where node is a reference to the node
 *       in the doubly linked list.
 * <p>
 *       Of course, at every put/get the list will need to be updated so that
 *       the "accessed node" is placed at the end of the list.
 * <p>
 *       Feel free to use existing java classes.
 * <p>
 *  Example of usage of an LRU cache with capacity of 3:
 *  <pre>
 *  // step 0:
 *  put(A,7)  // map{(A,7)}  A is the LRU
 *  // step 1:
 *  put(B,10) // map{(A,7),(B,10)}  A is the LRU
 *  // step 2:
 *  put(C,5)  // map{(A,7),(B,10),(C,5)}  A is the LRU
 *  // step 3:
 *  put(D,8)  // map{(B,10),(C,5),(D,8)}  A is suppressed, B is the LRU
 *  // step 4:
 *  get(B)    // C is the LRU
 *  // step 5
 *  put(E,9)  // map{(B,10),(D,8),(E,9)} D is the LRU
 *  // step 6
 *  put(D,3)  // map{(B,10),(D,3),(E,9)} B is the LRU
 *  // step 7
 *  get(B)    // map{(B,10),(D,3),(E,9)} E is the LRU
 *  // step 8
 *  put(A,2)  // map{(B,10),(D,3),(A,2)} D is the LRU
 * </pre>
 */
public class LRUCache<K, V> {
    private final int capacity;
    private final Map<K, Node> cache;
    private final Queue<Node> recency; // Doubly Linked List
//    private final Node CLL;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        cache = new HashMap<>();
        // TODO: Option 1: use a Queue | Option 2: use a custom DoublyLinkedList
//        CLL = new Node(null, null); // dummy Node like in CircularLinkedList
//        CLL.prev = CLL;
//        CLL.next = CLL;
        this.recency = new LinkedList<>();
    }

    public void put(K key, V value) {
        System.out.println("put("+key+","+value+")");

        if (cache.containsKey(key)) {
            Node node = cache.get(key);

            // update value in cache
            node.value = value;
            // remove node from its old place in list
            recency.remove(node);
//            remove(node);
            // make it new MRU
            recency.add(node);
//            addMRU(node);
        }
        else { // if (!cache.containsKey(key))
            if (cache.size() == capacity) {
                // remove current LRU first
//                Node lru = CLL.prev;
                Node lru = recency.poll();
                cache.remove(lru.key); // from cache
//                remove(lru); // from list
            }
            Node node = new Node(key, value);

            // make it new MRU
//            addMRU(node);
            recency.add(node);
            // add to cache
            cache.put(key, node);
        }
//        System.out.println("LRU: "+CLL.prev+"\tMRU: "+CLL.next);
        System.out.println("MRU => "+ recency +" <= LRU");
        System.out.println("Cache: "+cache.values());
        System.out.println();
    }

    public V get(K key) {
        System.out.println("get("+key+")");

        if (!cache.containsKey(key)) return null;

        Node search = cache.get(key);

        // remove from list
        recency.remove(search);
//        remove(search);
        // make it new MRU
        recency.add(search);
//        addMRU(search);

//        System.out.println("LRU: "+CLL.prev+"\tMRU: "+CLL.next);
        System.out.println("MRU => "+ recency +" <= LRU");
        System.out.println("Cache: "+cache.values());
        System.out.println();

        return search.value;
    }

//    private void addMRU(Node node) {
//        node.prev = CLL;
//        node.next = CLL.next;
//        CLL.next.prev = node;
//        CLL.next = node;
//    }
//    private void remove(Node node) {
//        node.next.prev = node.prev;
//        node.prev.next = node.next;
//    }

    private class Node {
        K key;
        V value;
        Node prev;
        Node next;
        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
        public String toString() {
            return String.format("(%s,%s)", key, value);
        }
    }
}

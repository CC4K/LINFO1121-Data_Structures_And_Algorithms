package Exam_training;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;

public class LFUCache {
    int capacity;

    /// <key, Node<key, val> >
    Map<Integer, Node> cache;
    /// <frequency, {nodes with that frequency}>
    Map<Integer, Queue<Node>> frequency;
    /// memory of smallest frequency
    int min_freq;

    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();
        // TODO: for storing Nodes per frequency: Option 1: use a Queue | Option 2: use a custom DoublyLinkedList
        this.frequency = new HashMap<>();
        this.min_freq = 1;
    }

    public int get(int key) {
        /* No match */
        if (!cache.containsKey(key)) return -1;
        /* Match */
        else {
            Node search = cache.get(key);
            // update node frequencies
            interactWithNode(search);
            // go fetch value
            return search.value;
        }
    }

    public void put(int key, int value) {
        /* Match => updating an old Node */
        if (cache.containsKey(key)) {
            Node search = cache.get(key);
            // update node frequencies
            interactWithNode(search);
            // update value
            search.value = value;
        }
        /* No match => adding a new Node */
        else {
            // remove LFU if full
            if (cache.size() == capacity) {
                // first remove node with worst frequency AND oldest
                Node lfu = frequency.get(min_freq).poll();
                cache.remove(lfu.key);
            }
            // create a new Node
            Node new_node = new Node(key, value); // new Node have freq of 1
            // add new node to list of frequencies == 1
            addToFrequencyList(new_node);
            // add to cache
            cache.put(key, new_node);

            min_freq = 1;
        }
    }

    private void interactWithNode(Node search) {
        if (frequency.containsKey(search.freq)) {
            // remove node and put it back to update recency
            frequency.get(search.freq).remove(search);
            if (frequency.get(search.freq).isEmpty()) {
                // update minimum frequency if necessary
                if (search.freq == min_freq) {
                    min_freq = search.freq + 1;
                }
            }
        }
        search.freq += 1;
        // add node to correct list of frequency
        addToFrequencyList(search);
    }

    private void addToFrequencyList(Node node) {
        Queue<Node> list = frequency.getOrDefault(node.freq, new LinkedList<>());
        list.add(node);
        frequency.put(node.freq, list);
    }

    private static class Node {
        int key;
        int value;
        int freq;
        public Node(int key, int value) {
            this.key = key;
            this.value = value;
            this.freq = 1;
        }
        public String toString() {
            return String.format("%d", key);
        }
    }
}

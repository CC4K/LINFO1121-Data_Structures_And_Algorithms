package Part4_Maps_Strings;

import java.util.*;

/**
 * Implement the class WordCounter that counts the number of occurrences
 * of each word in a given piece of text.
 * Feel free to use existing java classes.
 */
public class WordCounter implements Iterable<String> {

    private final Map<String, Integer> wordMap; // <"cat", 3>

    public WordCounter() {
        wordMap = new TreeMap<>();
    }

    /**
     * Add the word so that the counter of the word is increased by 1
     */
    public void addWord(String word) {
        if (wordMap.containsKey(word)) {
            wordMap.put(word, wordMap.get(word)+1);
        }
        else wordMap.put(word, 1);
    }

    /**
     * Return the number of times the word has been added so far
     */
    public int getCount(String word) {
        return wordMap.getOrDefault(word, 0);
    }

    // iterate over the words in ascending lexicographical order
    @Override
    public Iterator<String> iterator() { return wordMap.keySet().iterator(); }

}

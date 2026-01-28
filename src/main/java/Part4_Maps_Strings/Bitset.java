package Part4_Maps_Strings;

/**
 * A Bitset is an efficient alternative to using a Hashset
 * when you need to store a dense set of integers.
 * Instead of using an array of boolean to represent
 * the inclusion or exclusion of the value at the corresponding index,
 * it uses an array of long (called words) to store bits.
 * Each word encode 64 individual bits.
 * <p>
 * Your task is to implement:
 * 1. The initialization of the Bitset,
 *      calculating the required number of long words to store the specified number of bits.
 * 2. The `getNbWords()` method should return the total number of long words used.
 * 3. Helper methods to set, clear, and count bits within the Bitset.
 * <p>
 * Example:
 * Suppose we create a Bitset with a capacity of 8 bits,
 * with each "word" storing up to 4 bits (for simplicity in illustration).
 * The Bitset would initially be represented internally as [0000, 0000]. The following operations would yield:
 *  - getNbWords() : Returns 2 (total number of long words used).
 *  - set(4)       : Updates state to [0000, 0001] (sets the first bit in the second word).
 *  - set(2)       : Updates state to [0100, 0001] (sets the third bit in the first word).   7654 3210
 *  - set(7)       : Updates state to [0100, 1001] (sets the fourth bit in the second word). 1001 0100
 *  - count()      : Returns 3 (total number of bits set).
 *  - contains(2)  : Returns true.
 *  - clear(4)     : Updates state to [0100, 1000] (clears the first bit in the second word).
 */
public class Bitset {

    private final long[] wordList;
    private final int nbWords;
    private int size;

    /**
     * Instantiate a new bitset able to hold n elements
     * @param n the number of bits to store
     */
    public Bitset(int n) {
        // TODO: 64 == 100 0000
        //       63 == 011 1111
        // bitshift 6 times to get the word nb
        this.nbWords = (n + 63) >> 6;
        // an array of 64 bits words / longs
        this.wordList = new long[nbWords];
        this.size = 0;
    }

    /**
     *
     * @return the number of words used to store the n bits
     */
    public int getNbWords() {
        // TODO
        return nbWords;
    }

    /**
     * Set the ith bit
     * @param i the bit to set
     */
    public void set(int i) {
        // TODO
        // bitshift 6 times to get the word nb
        int word = i >> 6;
        //   v
        // 1001
        // 0010 (0001 << i)
        // ____ OR
        // 1011
        int bit = i;
        if ((wordList[word] & (1L << bit)) == 0) { // set only if 0
            wordList[word] = wordList[word] | (1L << bit);
            size++;
        }
    }

    /**
     * Clear the ith bit
     * @param i the bit to clear
     */
    public void clear(int i) {
        // TODO
        // bitshift 6 times to get the word nb
        int word = i >> 6;
        //   v
        // 1011
        // 0010 (0001 << i)
        // ____ NAND
        // 1001
        int bit = i;
        if ((wordList[word] & (1L << bit)) != 0) { // clear only if 1
            wordList[word] = wordList[word] & ~(1L << bit);
            size--;
        }
    }

    /**
     * Check if the ith bit is set in the structure
     * @param i the bit to check
     * @return true if the bit is set and false otherwise.
     */
    public boolean contains(int i) {
        // TODO
        // bitshift 6 times to get the word nb
        int word = i >> 6;
        //   v
        // 1011
        // 0010 (0001 << i)
        // ____ AND
        // 0110
        int bit = i;
        return (wordList[word] & (1L << bit)) != 0;
    }

    /**
     * Returns the number of bits set in the structure
     * @return the total number of set bits
     */
    public int count() {
        // TODO
        return size;
    }

}

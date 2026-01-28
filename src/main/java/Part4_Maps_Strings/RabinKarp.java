package Part4_Maps_Strings;

import java.util.Hashtable;

/**
 * Author Pierre Schaus
 * <p>
 * We are interested in the Rabin-Karp algorithm.
 * We would like to modify it a bit to determine
 * if a word among a list (!!! all words are of the same length !!!)
 * is present in the text.
 * To do this, you need to modify the Rabin-Karp
 * algorithm which is shown below (page 777 of the book).
 * More precisely, you are asked to modify this class
 * so that it has a constructor of the form:
 * public RabinKarp(String[] pat)
 * <p>
 * Moreover the search function must return
 * the index of the beginning of the first
 * word (among the pat array) found in the text or
 * the size of the text if no word appears in the text.
 * <p>
 * Example: If txt = "Here find interesting
 * exercise for Rabin Karp" and pat={"have", "find", "Karp"}
 * the search function must return 5 because
 * the word "find" present in the text and in the list starts at index 5.
 */
public class RabinKarp {
    private final Hashtable<Long, String> map = new Hashtable<>(); // <hash, txt>

    private String pat; // pattern (only needed for Las Vegas)
    private long patHash; // pattern hash value

    private final int M; // pattern length
    private final long Q; // a large prime
    private final int R = 2048; // alphabet size
    private long RM; // R^(M-1) % Q

    public RabinKarp(String[] patterns) {
        // this.pat = pat; // save pattern (only needed for Las Vegas)
        M = patterns[0].length();
        Q = 4463;
        RM = 1;
        for (int i = 1; i < M; i++) { // Compute R^(M-1) % Q for use
            RM = (R * RM) % Q; // in removing leading digit
        }
        // pre-process patterns and store in Map
        for (String pattern : patterns) {
            map.put(hash(pattern, M), pattern);
        }
    }

    public boolean check(int i) // Monte Carlo (See text.)
    { return true; } // For Las Vegas, check pat vs txt(i..i-M+1).

    private long hash(String txt, int M) { // Compute hash for key[0..M-1].
        long hash = 0;
        for (int j = 0; j < M; j++)
            hash = (R*hash + txt.charAt(j)) % Q;
        return hash;
    }

    public int search(String big_txt) { // Search for hash match in text.
        int N = big_txt.length();
        /* SEARCHING THROUGH SLIDING WINDOW */
        // compute hash of first window in transmission hash and compare with codes
        long big_txt_hash = hash(big_txt, M); // hash the first M chars : 0 -> M (ATCK------------)

        // keep sliding window and checking the other chars : M -> N (----IMPEBASEATCK)
        // FROM 0 TO (N-M) !!!
        for (int i = 0; i <= N - M; i++) {
            if (map.containsKey(big_txt_hash)) {
                if (big_txt.substring(i, i + M).equals(map.get(big_txt_hash))) {
                    return i; // match found
                }
            }
            // roll to next window
            if (i < N - M) {
                long leading = (RM * big_txt.charAt(i)) % Q;
                big_txt_hash = (big_txt_hash + Q - leading) % Q;
                big_txt_hash = (R*big_txt_hash + big_txt.charAt(i + M)) % Q;
            }
        }
        return N; // no match found
    }
}

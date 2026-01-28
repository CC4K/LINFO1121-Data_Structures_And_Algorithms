package Exam_training;

import java.util.*;

/**
 * Princess Leia has intercepted a long Imperial transmission. Hidden inside are secret code words that reveal the
 * locations and movements of Imperial troops. Each code word corresponds to a specific type of troop movement: supply
 * convoy, or patrol route.
 * <p>
 * To plan the Rebel response efficiently, Leia needs to monitor all these code words. She has compiled a list of code
 * words that are particularly important. Her goal is to know exactly how often each one appears in the transmission.
 * <p>
 * Your task is to help Leia analyze the transmission and determine the frequency of each code word so the Rebellion can
 * react quickly and strategically.
 * <p>
 * Inputs:
 *     · codes        : an array of k strings, each a secret code word to search for. All codes have the same length.
 *     · transmission : a string representing the intercepted Imperial transmission.
 * <p>
 * Output:
 *     · An array of k integers, where the i-th value is the number of occurrences of codes[i] in the transmission.
 * <pre>
 * Example:
 *     · Inputs: - codes = {
 *                     "ATCK",
 *                     "BASE",
 *                     "IMPE"
 *                 }
 *               - transmission = "ATCKIMPEBASEATCK"
 *     · Output: { 2, 1, 1 }
 *     · Explanation:
 *           - The code "ATCK" is found 2 times
 *           - The code "BASE" is found 1 time
 *           - The code "IMPE" is found 1 time
 * </pre>
 * Expected Time Complexity: O(k*M + N), where:
 *                               · k = number of codes
 *                               · M = length of the codes
 *                               · N = length of the transmission
 * <p>
 * Hint: All code words have the same length.
 *       Think about the Rabin-Karp algorithm (see the textbook).
 */
public class SecretTransmissionAnalysis {
    /// Size of alphabet
    private static final int R = 256;
    /// Large prime number
    private static final long Q = (1L << 61) - 1;

    /**
     * Counts the occurrences of each secret code word in a transmission.
     * @param codes an array of secret code words (all the same length)
     * @param transmission the intercepted Imperial transmission as a string
     * @return an array of integers where the i-th value is the number of times codes[i] appears in the transmission
     */
    public static int[] analyse(String[] codes, String transmission) {
        System.out.println(Arrays.toString(codes)+" "+transmission);
        int[] repeats = new int[codes.length];
        // TODO: compute hash of every code and look up the hash while sliding window over transmission

        int M = codes[0].length(); /// Length of codes (all codes have same length)
        int N = transmission.length(); /// Length of transmission
        if (N < M) return repeats;
        long RM = 1;
        for (int i = 1; i < M; i++) {
            RM = (R * RM) % Q;
        }

        // compute hashes of codes
        Map<Long, Integer> hashIndexes = new HashMap<>(); // store <code hash, code index>
        for (int i = 0; i < codes.length; i++) {
            // hashing start
            String code = codes[i];
            long code_hash = 0;
            for (int j = 0; j < M; j++) {
                code_hash = (R*code_hash + code.charAt(j)) % Q; // hashing
            }

            // store <code hash, code index>
            if (!hashIndexes.containsKey(code_hash)) hashIndexes.put(code_hash, i);
        }
        //System.out.println(hashIndexes); // {1096041291=0, 1111577413=1, 1229803589=2}
        //                                    [      ATCK=0,       BASE=1,       IMPE=2]

        /* SEARCHING THROUGH SLIDING WINDOW */
        // compute hash of first window in transmission hash and compare with codes
        long tansmission_hash = 0;
        for (int i = 0; i < M; i++) { // 0 -> M (ATCK------------)
            tansmission_hash = (R*tansmission_hash + transmission.charAt(i)) % Q; // hashing
        }
        // check
        if (hashIndexes.containsKey(tansmission_hash)) {
            System.out.println(tansmission_hash);
            repeats[hashIndexes.get(tansmission_hash)]++;
        }

        // keep sliding window and checking
        for (int i = M; i < N; i++) { // M -> N (----IMPEBASEATCK)
            long leading = (RM * transmission.charAt(i - M)) % Q;
            tansmission_hash = (tansmission_hash + Q - leading) % Q; // remove leading char
            tansmission_hash = (R*tansmission_hash + transmission.charAt(i)) % Q; // add trailing char AKA re-hashing
            // check
            if (hashIndexes.containsKey(tansmission_hash)) {
                System.out.println(tansmission_hash);
                repeats[hashIndexes.get(tansmission_hash)]++;
            }
        }

        return repeats;
    }
}

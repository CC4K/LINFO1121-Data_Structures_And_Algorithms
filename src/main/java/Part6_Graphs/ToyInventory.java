package Part6_Graphs;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Santa’s workshop is buzzing with activity, but there’s trouble in the toy inventory department! The elves in charge
 * of managing the stock have lost their counts for the total inventory and the crucial list linking each toy to its
 * category.
 *
 * Santa is determined to have an accurate count of his toys, so he’s asked you to help recreate the inventory based on
 * what the elves can provide you.
 *
 * Here’s what the elves remember:
 *     1. The older elves provide you a list of pairs of toys that are in the same category.
 *     2. Each warehouse managers provides a list of the toys they have in stock, along with the number of each toy.
 *
 * Now, Santa has a list of specific toys he wants to check, and for each, you must determine the total number of toys
 * in its category. Can you help the elves restore the inventory and answer Santa’s requests?
 *
 * Input:
 *     * A list of pairs of strings, where each pair (x, y) indicates that toys x and y belong to the same category.
 *     * A list of toys in the warehouses
 *     * A list of the count for each toy in the warehouses
 *     * A list of toys Santa wants to query.
 *
 * Output:
 *     * A list of integers where the i-th value represents the total count of toys in the category of the i-th queried
 *       toy. If a toy belongs to no category or is not in stock, the count should be 0.
 *
 * For example:
 *     * Input: relations = [[piano, flute], [flute, xylophone], [car, helicopter]],
 *              occurrences_name = [piano, flute, piano, xylophone, car, helicopter],
 *              occurrences_count = [3, 2, 1, 5, 2, 3],
 *              requests = [piano, flute, helicopter, teddybear]
 *     * Output: [11, 11, 5, 0] -> total_count({piano, flute, xylophone}) = 3 + 2 + 1 + 5 = 11;
 *                                 total_count({car, helicopter}) = 2 + 3 = 5;
 *                                 total_count({teddy bear}) = 0;
 *
 * Expected Time-Complexity: O(N * log(N)) (N being the number of relations).
 *
 * Hint:
 *     * The problem involves grouping objects that are in the same family and summing their counts efficiently.
 *       A Union-Find data structure allows to identify and merge such groups of object efficiently.
 */
public class ToyInventory {

    /**
     * Computes for each requested toy the total number of toys stored in the warehouses that belong to the same category.
     * @param relations A list of pairs of strings, where each pair (x, y) indicates that toys x and y belong to the same category.
     * @param occurrencesName A list of toys in the warehouses
     * @param occurrencesCount A list of the count for each toy in the warehouses
     * @param requests A list of toys Santa wants to query.
     * @return A list of integers where the i-th value represents the total count of toys in the category of the i-th
     *         requested toy. If a toy belongs to no category or is not in stock, the count should be 0.
     */
    public static int[] answerRequests(String[][] relations, String[] occurrencesName, int[] occurrencesCount, String[] requests) {
        int[] from = new int[relations.length];
        int[] to = new int[relations.length];
        Map<String, Integer> toys_id = new HashMap<>();
        for (int i = 0; i < relations.length; i++) {
            String a = relations[i][0];
            String b = relations[i][1];
            // start populating the map
            toys_id.putIfAbsent(a, toys_id.size());
            toys_id.putIfAbsent(b, toys_id.size());
            // record src & destinations
            from[i] = toys_id.get(a);
            to[i] = toys_id.get(b);
        }
        System.out.println(Arrays.toString(from));
        System.out.println(Arrays.toString(to));
        System.out.println(toys_id);

        int[] occ_id = new int[occurrencesName.length];
        int[] occ_cnt = new int[occurrencesName.length];
        for (int i = 0; i < occurrencesName.length; i++) {
            String name = occurrencesName[i];
            int count = occurrencesCount[i];
            toys_id.putIfAbsent(name, toys_id.size());
            occ_id[i] = toys_id.get(name);
            occ_cnt[i] = count;
        }
        System.out.println(Arrays.toString(occ_id));
        System.out.println(Arrays.toString(occ_cnt));
        System.out.println(toys_id);

        // TODO : use Union-Find to group the toys if they are in the same relation
        UF uf = new UF(toys_id.size());
        // first init the amount[] in UF with occurrences for each toy
        for (int i = 0; i < occ_cnt.length; i++) {
            uf.size[occ_id[i]] += occ_cnt[i];
        }
        // union(from, to)
        for (int i = 0; i < from.length; i++) {
            uf.union(from[i], to[i]);
        }
        System.out.println(Arrays.toString(uf.id));
        System.out.println(Arrays.toString(uf.size));

        int[] answer = new int[requests.length];
        for (int i = 0; i < requests.length; i++) {
            String toy_name = requests[i];
            // check if toy even recorded at all
            if (!toys_id.containsKey(toy_name)) answer[i] = 0; // not found
            else {
                int toy_id = toys_id.get(toy_name);
                answer[i] = uf.size[uf.find(toy_id)];
            }
        }
        System.out.println();
        System.out.println(Arrays.toString(answer)); // [11, 11, 5, 0]
        return answer;
    }

    static class UF {
        int[] id;
        int[] size;
        public UF(int n) {
            this.id = new int[n];
            for (int i = 0; i < n; i++) {
                id[i] = i;
            }
            this.size = new int[n];
        }
        public boolean connected(int p, int q) {
            return find(p) == find(q);
        }
        public int find(int p) {
            return (id[p] == p) ? p : find(id[p]); // while not your own id => follow the trail
        }
        public void union(int p, int q) {
            System.out.println(Arrays.toString(id));
            System.out.println(Arrays.toString(size));
            System.out.println();
            int pId = find(p);
            int qId = find(q);
            if (pId == qId) return; // already in same group
            // smallest sized group points to larger group
            if (size[pId] < size[qId]) {
                id[pId] = qId;
                size[qId] += size[pId];
            }
            else { // if (size[qId] <= size[pId])
                id[qId] = pId;
                size[pId] += size[qId];
            }
        }
    }
}

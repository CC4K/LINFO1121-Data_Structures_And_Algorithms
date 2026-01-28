package Part4_Maps_Strings;

import java.util.*;

/**
 * Given a set of n players, EACH WITH A DIFFERENT score at an arcade game,
 * you want to know what would be the rank of a given score among those.
 * <p>
 * Your first task is to implement correctly the compareTo(Player o)
 * so that the sorting algorithm in the constructor
 * sorts the givens players in decreasing order of their score.
 * <p>
 * Your second task is to implement the getRank(int score) method
 * to find the rank of a given score among the players.
 * <p>
 * !!!! You cannot call the get(int i) more than 2 x log(n) times !!!
 * So that the time complexity is O(log(n)).
 * <p>
 * The highest score is rank 0 and the lowest score is rank n-1.
 * <p>
 * If the queried score is equal to a score that is stored in the players
 * they should have the same rank.
 */
public class ArcadeRanking {

    /**
     *  Stores the players in decreasing order of their score
     *  and allow to retrieve them in their sorted order with a get method.
     *  You don't need to modify this interface nor to implement it.
     *  All you know is that if you have correctly
     *  implemented the compareTo method in Player,
     *  the players are sorted in decreasing order of their score.
     *  It means that get(0) returns the player with the highest score.
     *  and get(size()-1) returns the player with the lowest score.
     */
    interface Players {
        int size();
        Player get(int i);
    }

    /**
     * Return the rank among the given players for the given score.
     * <p>
     * In your algorithm, you cannot call the get(int i) method of players
     * more than 2 x log(n) times so that the time complexity is in O(log(n)).
     * <p>
     * The highest score is rank 0 and the lowest score is rank n-1.
     * If the queried score is equal to a score that is stored in the players
     * they should have the same rank.
     * @param players the set of players, sorted in decreasing order of their score
     *                so that get(0) returns the player with the highest score.
     * @param target_score the score to find the rank of
     */
    public static int getRank(Players players, int target_score) {
        //for (int i = 0; i < players.size(); i++) System.out.print(players.get(i)+" ");
        // (Rapha:842) (Xx_DarkLink_xX:646) (Faker:584) (DiabloX9:34) (G4M3R:15) (FoxhoundFinn33:2)
        // getRank(players,842) = 0    // this score is present
        // getRank(players,650) = 1
        // getRank(players,646) = 1    // this score is present
        // getRank(players,600) = 2
        // getRank(players,584) = 2    // this score is present
        // getRank(players, 34) = 3    // this score is present
        // getRank(players, 15) = 4    // this score is present
        // getRank(players,  3) = 5
        // getRank(players,  2) = 5    // this score is present
        // getRank(players,  0) = 6
        // sorted scores: 842, 646, 584, 34, 15, 2
        // ranks:           0,   1,   2,  3,  4, 5
        // TODO: have to get less than N times => "bête" binarySearch
        int left = 0; int right = players.size() - 1;
        System.out.println(target_score);
        while (left <= right) {
            int mid = left + ((right-left)/2);
            int mid_score = players.get(mid).score;
            System.out.printf("(%d,%d)\n", mid, players.get(mid).score);

            if (target_score == mid_score) return mid; // spot on !
            if (mid_score < target_score) {
                right = mid-1; // look on the left of the array
                System.out.println("look on the left");
            }
            if (target_score < mid_score) {
                left = mid+1; // look on the right of the array
                System.out.println("look on the right");
            }
        }
        System.out.printf("%d %d\n", left, right);
        return left;
    }

}

class Player implements Comparable<Player> {
    public final int score;
    public final String pseudo;

    public Player(int score, String pseudo) {
        this.score = score;
        this.pseudo = pseudo;
    }

    /**
     * Compare two Players objects, so that they can
     * be sorted in decreasing order of their score
     * @param o the other Player to compare to
     */
    @Override
    public int compareTo(Player o) {
        // TODO
        return o.score - score;
    }
    @Override
    public boolean equals(Object obj) {
        return pseudo.equals(((Player) obj).pseudo);
    }
    @Override
    public int hashCode() {
        return pseudo.hashCode();
    }
    @Override
    public String toString() {
        return "("+pseudo+":"+score+")";
    }
}

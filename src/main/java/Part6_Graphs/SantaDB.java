package Part6_Graphs;

import java.util.*;

/**
 * Santa Claus is getting ready for his annual gift distribution.
 * <p>
 * The Christmas elves have worked hard to compile a list of children and the gifts they wish to receive, as mentioned in their letters.
 * However, there's been a slight mix-up: the elves' list of gifts, collected from the children's letters, is missing the children's names.
 * <p>
 * For instance, the lists of children and gifts might look something like this:
 * Children: [{1, "Alice"}, {2, "Bob"}, {3, "Charlie"}, {4, "Britney"}]
 * Gifts: [{1001, 1, "Toy Train"}, {1002, 1, "Doll"}, {1003, 2, "Bicycle"}]
 * <p>
 * Note that not every child may have written a letter, and those children will not receive gifts.
 * Each child has a unique ID, and each gift is also tagged with the child's ID to whom it is intended.
 * <p>
 * Your task is to assist Santa in matching each child with their respective gift(s) and generate a list of gift assignments like:
 * [{1, "Alice", 1001, "Toy Train"}, {1, "Alice", 1002, "Doll"}, {2, "Bob", 1003, "Bicycle"}]
 * This operation is known as an inner join in database terminology.
 * <p>
 * This list will be extremely useful for Santa to prepare name stickers for the gifts.
 * <p>
 * If 'n' is the number of children and 'm' is the number of gifts, your method's time complexity should be O(n log m).
 * You will earn 50% of the grade for a code that produces the correct result, and 100% if your code also meets the time complexity requirement.
 */
public class SantaDB {

    /**
     * Inner join operation on the two arrays (see above).
     * @param children The array of children, the ID of the child in the child list is unique
     * @param gifts The array of gifts, the ID of the gift in this list is unique but the ID of the child in the gift list is not.
     * @return The list of gift assignments
     */
    public static List<GiftAssignment> innerJoin(Child[] children, Gift[] gifts) {
        System.out.println(Arrays.toString(children)); // [(1:Alice), (2:Bob), (3:Charlie)]
        System.out.println(Arrays.toString(gifts)); // [1:[1001,Toy Train], 1:[1002,Doll], 2:[1003,Bicycle]]
        // TODO
        List<GiftAssignment> assignments = new ArrayList<>();

        Map<Integer, String> enfants = new HashMap<>();
        for (Child child : children) enfants.put(child.id, child.name);
        for (Gift gift : gifts) {
            assignments.add(new GiftAssignment(gift.childId, enfants.get(gift.childId), gift.giftId, gift.details));
        }

        // {childId, childName, giftId, giftName}
        // [{1, "Alice", 1001, "Toy Train"}, {1, "Alice", 1002, "Doll"}, {2, "Bob", 1003, "Bicycle"}]
        return assignments;
    }

    public static void main(String[] args) {
        // Children: [{1, "Alice"}, {2, "Bob"}, {3, "Charlie"}]
        Child [] children = new Child []{
            new Child(1, "Alice"),
            new Child(2, "Bob"),
            new Child(3, "Charlie")
        };
        // Gifts: [{1001, 1, "Toy Train"}, {1002, 1, "Doll"}, {1003, 2, "Bicycle"}]
        Gift [] gifts = new Gift []{
            new Gift(1001, 1, "Toy Train"),
            new Gift(1002, 1, "Doll"),
            new Gift(1003, 2, "Bicycle")
        };
        GiftAssignment [] assignments = innerJoin(children, gifts).toArray(new GiftAssignment[0]);
        System.out.println(Arrays.toString(assignments));
    }

}

// You should not modify those three classes Child, Gift and GiftAssignment

class Child {
    int id;
    String name;
    Child(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public String toString() {
        return String.format("(%d:%s)", id, name);
    }
}

class Gift {
    int giftId;
    int childId;
    String details;
    Gift(int giftId, int childId, String details) {
        this.giftId = giftId;
        this.childId = childId;
        this.details = details;
    }
    public String toString() {
        return String.format("%d:[%d,%s]", childId, giftId, details);
    }
}

class GiftAssignment {
    int childId;
    String childName;
    int giftId;
    String giftDetails;
    GiftAssignment(int childId, String childName, int giftId, String giftDetails) {
        this.childId = childId;
        this.childName = childName;
        this.giftId = giftId;
        this.giftDetails = giftDetails;
    }
    @Override
    public String toString() {
        return childName +  " (Child ID: "+ childId +") receives " + giftDetails + " (Gift ID: " + giftId + ")";
    }
}

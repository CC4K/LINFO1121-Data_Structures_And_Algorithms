package Part3_Search_Trees;

import java.util.*;

/**
 * This class represents a map that maps birthdays to persons.
 * Your map should be able to handle multiple people born on the same day.
 * Your map should be able to query efficiently for
 * - people born on a specific date and
 * - people born in a specific year.
 * You can assume that the input is valid for dates (format 'YYYY-MM-DD') and years (format 'YYYY').
 * The years do not start with 0.
 * The time complexity of the operations should be in O(log n + k)
 * where k is the number of people born on the specified date or year
 * and n is the number of different birthdays in the map.
 * <p>
 * Complete the class to make the tests in BirthdayMapTest pass.
 * Do not modify the signature of existing methods.
 * Feel free to add instance variables and new methods.
 * Also feel free to import and use existing java classes.
 */
class BirthdayMap {
    // Hint: feel free to use existing java classes from Java such as java.util.TreeMap

    TreeMap<String, List<Person>> birthdayMap; // <Date, List of persons born on that date>

    BirthdayMap() {
        // TODO
        this.birthdayMap = new TreeMap<>();
    }

    /**
     * Adds a person to the map.
     * The key is the birthday of the person.
     * The time complexity of the method should be in O(log n)
     * where n is the number of different birthdays in the map.
     * @param person
     */
    void addPerson(Person person) {
        // TODO
        String birthday = person.birthday;
        // if date key doesn't exist yet => add date then add person to list
        if (!birthdayMap.containsKey(birthday)) {
            birthdayMap.put(birthday, new ArrayList<>()); // add date
            birthdayMap.get(birthday).add(person); // fill the list
        }
        // if date key already exists => add person to list
        else if (birthdayMap.containsKey(birthday)) {
            birthdayMap.get(birthday).add(person);
        }
    }

    /**
     * The function returns a list of Person objects in the map born on the specified date.
     * @param date a String input representing the date (in 'YYYY-MM-DD' format)
     *             for which we want to retrieve people born on.
     * @return A list of Person objects representing all people born on the specified date.
     *          An empty list is returned if no entries are found for the specified date.
     */
    List<Person> getPeopleBornOnDate(String date) {
        // TODO
        // date recorded then return the list of person on that day
        if (birthdayMap.containsKey(date)) {
            return birthdayMap.get(date);
        }
        return new ArrayList<>();
    }


    /**
     * The function returns a consolidated list of Person objects
     * in the map born in the specified year.
     * @param year A String input representing the year (in 'YYYY' format)
     *             for which we want to retrieve people born in.
     * @return A consolidated list of Person objects representing all people born in the specified year.
     *         If no entries are found for the specified year, the function returns an empty list.
     */
    List<Person> getPeopleBornInYear(String year) {
        // TODO
        // add persons born on every day of the year from 1st Jan to 1st Jan next year in a List
        List<Person> sameYearBuddies = new ArrayList<>();
        int currentYear = Integer.parseInt(year);
        Map<String, List<Person>> yearMap = birthdayMap.subMap(currentYear+"-01-01", (currentYear+1)+"-01-01");
        System.out.println(yearMap);
        for (List<Person> persons : yearMap.values()) {
            for (Person person : persons) {
                sameYearBuddies.add(person);
            }
        }
        return sameYearBuddies;
    }

}

class Person {
    String name;
    String birthday; // format: YYYY-MM-DD
    Person(String name, String birthday) {
        this.name = name;
        this.birthday = birthday;
    }
    @Override
    public String toString() {
        return this.name;
    }
}

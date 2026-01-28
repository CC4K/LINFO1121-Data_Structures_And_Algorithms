package Part4_Maps_Strings;

import org.javagrader.Grade;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


@Grade
public class AutoCompleterTest {

    @Test
    @Grade(value=1)
    public void testEmptyTrie() {
        AutoCompleter ac = new AutoCompleter(new String[]{});
        assertNull(ac.complete(""));
        assertNull(ac.complete("a"));
    }

    @Test
    @Grade(value=1)
    public void testPrefixNotInTrie() {
        AutoCompleter ac = new AutoCompleter(new String[]{ "abruptly",  "babylonite", "cinurous", "drabbing", "exocardiac"});
        assertNull(ac.complete("factive"));
        assertNull(ac.complete("babylonites"));
    }

    @Test
    @Grade(value=1)
    public void testPrefixIsKey() {
        String[] dictionary = new String[]{ "abruptly",  "babylonite", "cinurous", "drabbing", "exocardiac"};
        AutoCompleter ac = new AutoCompleter(dictionary);
        for (String word : dictionary) {
            assertEquals(word, ac.complete(word));
        }
    }

    @Test
    @Grade(value=1)
    public void testPrefixInTrie() {
        String[] dictionary = new String[]{ "abruptly", "abrus", "babylonite", "cinurous", "drabbing", "exocardiac"};
        AutoCompleter ac = new AutoCompleter(dictionary);
        for (String word : dictionary) {
            assertEquals(word, ac.complete(word.substring(0, word.length()-1)));
        }
        assertEquals("abrus", ac.complete("a"));
    }

    @Test
    @Grade(value=1)
    public void testLexicographic() {
         String[] dictionary = {"bifold", "bind","cat", "car", "dot", "dog", "bike", "bill",  };
         AutoCompleter ac = new AutoCompleter(dictionary);
         assertEquals("car", ac.complete("ca"));
         assertEquals("dog", ac.complete("do"));
         assertEquals("bike", ac.complete("bi"));
    }

    @Test
    @Grade(value=1)
    public void testMultipleKeysForPrefix() {
        String[] dictionary = new String[]{ "abruptly", "abrus", "abrui", "babylonite", "cinurous", "drabbing", "exocardiac"};
        AutoCompleter ac = new AutoCompleter(dictionary);
        assertEquals("abrui", ac.complete("a"));
    }


}

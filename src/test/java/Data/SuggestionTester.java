package Data;

import org.junit.Test;

import java.util.Date;

public class SuggestionTester {

    /**
     * Just simple test to make sure that
     * toJson is working
     */
    @Test
    public void test1 () {
        Suggestion simpleSuggestion1 = new Suggestion(Suggestion.SuggestionType.Error, "1", null, 1, 2, "Bad Code", new Date());
        System.out.println(simpleSuggestion1);
        Suggestion simpleSuggestion2 = new Suggestion(Suggestion.SuggestionType.Warning, "2", null, 3, 4, "Not so bad", new Date());
        System.out.println(simpleSuggestion2);
    }
}

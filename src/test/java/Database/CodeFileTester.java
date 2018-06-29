package Database;

import Data.Suggestion;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

public class CodeFileTester {

    @Test
    public void test1 () {
        ArrayList <Suggestion> suggestions = new ArrayList<>();
        Suggestion simpleSuggestion1 = new Suggestion(Suggestion.SuggestionType.Error, null, "1", "1", 1, 2, "Bad Code", new Date());
        System.out.println(simpleSuggestion1);
        Suggestion simpleSuggestion2 = new Suggestion(Suggestion.SuggestionType.Warning, null, "1", "1",  3, 4, "Not so bad", new Date());
        System.out.println(simpleSuggestion2);
        suggestions.add(simpleSuggestion1);
        suggestions.add(simpleSuggestion2);
        CodeFile codeFile = new CodeFile("cpp", "123","int a;", suggestions);
        System.out.println(codeFile);
    }
}

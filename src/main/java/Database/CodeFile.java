package Database;

import Data.Suggestion;
import Models.User;
import com.google.gson.Gson;

import java.util.ArrayList;

public class CodeFile {

    private ArrayList<Suggestion> suggestions;
    private String code;
    private String lang;

    public CodeFile (String lang, String code, ArrayList<Suggestion> suggestions) {
        this.code = code;
        this.lang = lang;
        this.suggestions = suggestions;
    }

    /**
     * Converts this object to JSON string
     * @return JSON representation of the object
     */
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}

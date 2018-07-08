package Models;

import Database.UserStorage;
import Interfaces.UserRetriever;
import com.google.gson.Gson;

import java.util.List;

public class CodeFile implements UserRetriever {

    public enum Lang {
        c,
        cpp,
        java,
        undefined
    }

    private List<Suggestion> suggestions;
    private String code;
    private String fileName;
    private String fileId;
    private Lang lang;
    public CodeFile (String code, String fileId, String fileName, List<Suggestion> suggestions,String lang) {
        this.code = code;
        this.fileName = fileName;
        this.suggestions = suggestions;
        this.fileId = fileId;
        try {
            this.lang = Lang.valueOf(lang);
        } catch (IllegalArgumentException e) {
            this.lang = Lang.undefined;
        }

    }

    public Lang getLang() {
        return lang;
    }

    public static class Info {
        public String id;
        public String name;

        public Info (String id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    @Override
    public void RetrieveUsers (String requesterID, UserStorage userStorage) {
        if (suggestions == null) return;

        for (Suggestion suggestion:suggestions) {
            suggestion.RetrieveUsers (requesterID, userStorage);
        }
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

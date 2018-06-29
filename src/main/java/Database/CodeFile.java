package Database;

import Data.Suggestion;
import Models.User;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class CodeFile {

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
    public CodeFile (String code, String fileId, String fileName, List<Suggestion> suggestions) {
        this.code = code;
        this.fileName = fileName;
        this.suggestions = suggestions;
        this.fileId = fileId;
        try {
            lang = Lang.valueOf(fileName.substring(fileName.lastIndexOf(".") + 1));
        } catch (IllegalArgumentException e) {
            lang = Lang.undefined;
        }

    }

    public Lang getLang() {
        return lang;
    }

    public static void main(String[] args) {
        ArrayList<Suggestion> arrayList = new ArrayList<>();
        CodeFile codeFile = new CodeFile("12", "1","bakuri.cpp", null);
        System.out.println(codeFile.getLang());
        System.out.println(codeFile);
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

package Data;

import Models.User;
import com.google.gson.Gson;
import java.util.Date;

public class Suggestion {

    //Type of suggestion
    public enum SuggestionType {
        Warning,
        Error
    }
    public SuggestionType type;

    private String user;
    private String imgSrc;
    private String codeID;
    private String suggestionID;

    //Information about commentList
    private Integer startInd;
    private Integer endInd;
    private String content;
    private Date timeStamp;


    public Suggestion (SuggestionType type, String user, String imgSrc, String codeID, String suggestionID, int startInd, int endInd, String content, Date timeStamp) {
        this.type = type;
        this.user = user;
        this.imgSrc = imgSrc;
        this.suggestionID = suggestionID;
        this.codeID = codeID;
        this.startInd = startInd;
        this.endInd = endInd;
        this.content = content;
        this.timeStamp = timeStamp;
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

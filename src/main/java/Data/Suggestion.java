package Data;

import com.google.gson.Gson;
import java.util.Date;

public class Suggestion {

    //Type of suggestion
    public enum SuggestionType {
        Warning,
        Error
    }
    public SuggestionType type;

    public String userID;
    public String imgSrc;
    public String fileID;
    public String suggestionID;

    //Information about commentList
    public String content;
    public Date timeStamp;
    public Integer startInd;
    public Integer endInd;


    public Suggestion (SuggestionType type, String userID, String imgSrc, String fileID, String suggestionID, int startInd, int endInd, String content, Date timeStamp) {
        this.type = type;
        this.userID = userID;
        this.imgSrc = imgSrc;
        this.suggestionID = suggestionID;
        this.fileID = fileID;
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

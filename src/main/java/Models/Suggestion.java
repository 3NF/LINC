package Models;

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

    public String userID;
    public String fileID;
    public String suggestionID;
    private UserMini user;

    //Information about suggestion content
    public String content;
    public Date timeStamp;
    public Integer startInd;
    public Integer endInd;


    public Suggestion (SuggestionType type, String userID, String fileID, String suggestionID, int startInd, int endInd, String content, Date timeStamp) {
        this.type = type;
        this.userID = userID;
        this.suggestionID = suggestionID;
        this.fileID = fileID;
        this.startInd = startInd;
        this.endInd = endInd;
        this.content = content;
        this.timeStamp = timeStamp;

        this.getUserObject();
    }

    private void getUserObject () {
        //Temporary
        this.user = (UserMini)new User("lchum16@freeuni.edu.ge", "Luka",  "Tchumburidze", userID, "https://api.adorable.io/avatars/285/luka-tchumburidze.png", "0", "0");
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

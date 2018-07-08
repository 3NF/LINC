package Models;

import Database.UserStorage;
import Interfaces.UserRetriever;
import Models.User;
import com.google.gson.Gson;
import java.util.Date;

public class Suggestion implements UserRetriever {
    //Type of suggestion
    public enum SuggestionType {
        Warning,
        Error
    }
    public SuggestionType type;

    public String fileID;
    public String suggestionID;
    public User user;

    //Information about suggestion content
    public String content;
    public Date timeStamp;
    public Integer startInd;
    public Integer endInd;


    public Suggestion (SuggestionType type, String userID, String fileID, String suggestionID, int startInd, int endInd, String content, Date timeStamp) {
        this.type = type;
        this.user = new User(userID);
        this.suggestionID = suggestionID;
        this.fileID = fileID;
        this.startInd = startInd;
        this.endInd = endInd;
        this.content = content;
        this.timeStamp = timeStamp;
    }

    @Override
    public void RetrieveUsers(String requesterID, UserStorage userStorage) {
        if (user.getEmail() == null) {
            user = userStorage.getUserWithID(requesterID, user.getUserId());
            System.out.println(user.getFirstName() + user.getLastName());
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

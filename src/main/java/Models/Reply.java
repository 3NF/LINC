package Models;

import Database.UserStorage;
import Interfaces.UserRetriever;
import Models.User;
import com.google.gson.Gson;

import java.util.Date;

public class Reply implements UserRetriever {
    private String suggestionID;
    private String replyID;
    public User user;

    //Information about reply
    private String content;
    private Date timeStamp;

    public Reply (String suggestionID, String replyID, String userID, String content, Date timeStamp) {
        this.suggestionID = suggestionID;
        this.replyID = replyID;
        this.user = new User(userID);
        this.content = content;
        this.timeStamp = timeStamp;
    }


    public Date getDate(){
        return timeStamp;
    }
    public String getSuggestionID(){
        return suggestionID;
    }
    public String getReplyID(){
        return replyID;
    }

    @Override
    public void RetrieveUsers(String requesterID, UserStorage userStorage) {
        if (user.getEmail() == null) {
            user = userStorage.getUserWithID(requesterID, user.getUserId());
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

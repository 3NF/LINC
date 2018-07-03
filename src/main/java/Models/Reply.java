package Models;

import Models.User;
import com.google.gson.Gson;

import java.util.Date;

public class Reply {
    private String suggestionID;
    private String replyID;
    private String userID;
    private User user;

    //Information about reply
    private String content;
    private Date timeStamp;

    public Reply (String suggestionID, String replyID, String userID, String content, Date timeStamp) {
        this.suggestionID = suggestionID;
        this.replyID = replyID;
        this.userID = userID;
        this.content = content;
        this.timeStamp = timeStamp;

        this.getUserObject();
    }

    private void getUserObject () {
        //Tempprary
        this.user = new User("gtsut16@freeuni.edu.ge", "Gvantsa",  "Tsutskhashvili", userID, "https://api.adorable.io/avatars/285/gvantsa-tsutskhashvili.png", "0", "0");
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
    /**
     * Converts this object to JSON string
     * @return JSON representation of the object
     */
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

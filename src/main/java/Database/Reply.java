package Database;

import Models.User;
import com.google.gson.Gson;

import java.util.Date;

public class Reply {
    private String suggestionID;
    private String replyID;

    //Information about reply
    private String user;
    private String content;
    private Date timeStamp;

    public Reply (String suggestionID, String replyID, String user, String content, Date timeStamp) {
        this.suggestionID = suggestionID;
        this.replyID = replyID;
        this.user = user;
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

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

    private User user;
    private String suggestionID;

    //Information about comment
    private Integer startInd;
    private Integer endInd;
    private String content;
    private Date timeStamp;


    public Suggestion (SuggestionType type, String suggestionID, User user, int startInd, int endInd, String content, Date timeStamp) {
        this.type = type;
        this.suggestionID = suggestionID;
        this.user = user;
        this.startInd = startInd;
        this.endInd = endInd;
        this.content = content;
        this.timeStamp = timeStamp;
    }

    public class Reply {

        private String suggestionID;
        private String replyID;

        //Information about reply
        private User user;
        private String content;
        private Date timeStamp;

        public Reply (String suggestionID, String replyID, User user, String content, Date timeStamp) {
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


    /**
     * Converts this object to JSON string
     * @return JSON representation of the object
     */
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}

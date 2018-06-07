package Models;

import com.google.api.services.drive.model.Reply;

/*
* represents teacher comment.
* Teacher can select any piece of sub code in students code and comment it.
* Any code comment can have ordered replies of student and teacher.
* */
public class CodeComment
{
    /**
     * Character index on which selection starts
     */
    private int selectionStart;


    /**
     * Character index on which selection ends
     */
    private int selectionEnd;


    /**
     * replies on comment
     */
    private Reply[] replies;


    private String comment;

    public CodeComment(int selectionStart, int selectionEnd, String comment){
        this.selectionStart = selectionStart;
        this.selectionEnd = selectionEnd;
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public int getSelectionStart() {
        return selectionStart;
    }

    public int getSelectionEnd() {
        return selectionEnd;
    }
}

package Models;


import java.time.LocalDateTime;


/**
 * Represent replies on teacher's comment.
 * Reply can be added by student, whom commented code belongs or any teacher.
 */
public class CommentReply
{
    private LocalDateTime postTime;
    private User from;
    private String text;

    public CommentReply(LocalDateTime postTime, User from, String text) {
        this.postTime = postTime;
        this.from = from;
        this.text = text;
    }

    public LocalDateTime getPostTime() {
        return postTime;
    }

    public User getFrom() {
        return from;
    }

    public String getText() {
        return text;
    }
}

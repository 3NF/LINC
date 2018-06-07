package Models;

import java.time.LocalDateTime;


/**
 * Class that represents info of already corrected assigment
 */
public class Assignment
{
    private int index;
    private String name;
    private LocalDateTime postTime;

    public Assignment(int index, String name, LocalDateTime postTime) {
        this.index = index;
        this.name = name;
        this.postTime = postTime;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getPostTime() {
        return postTime;
    }
}

package Models;


/**
 * represents classroom, but only contains basic information.
 */
public class BasicRoomInfo
{

    /**
     * Unique name of room.
     */
    private String id;

    private String title;


    /**
     * Full name of lecturer.
     */
    private String lecturerName;

    public String getLecturerName() {
        return lecturerName;
    }


    public BasicRoomInfo(String name, String title, String lecturerName) {
        this.id = name;
        this.title = title;
        this.lecturerName = lecturerName;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}

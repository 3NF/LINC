package Models;


/**
 * represents classroom, but only contains basic information.
 */
public class BasicRoomInfo
{
    private String name;
    private String title;

    private String lecturerName;

    public String getLecturerName() {
        return lecturerName;
    }


    public BasicRoomInfo(String name, String title, String coverImagePath) {
        this.name = name;
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }
}

package Models;


/**
 * represents classroom, but only contains basic information.
 */
public class BasicRoomInfo
{
    private String name;
    private String title;
    private String coverImagePath;


    public BasicRoomInfo(String name, String title, String coverImagePath) {
        this.name = name;
        this.title = title;
        this.coverImagePath = coverImagePath;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getCoverImagePath() {
        return coverImagePath;
    }
}

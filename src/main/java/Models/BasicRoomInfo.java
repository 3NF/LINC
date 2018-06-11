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

    private String imageSrc;
    /**
     * Full name of lecturer.
     */
    private String lecturerName;

    public String getLecturerName() {
        return lecturerName;
    }


    public BasicRoomInfo(String name, String title, String lecturerName, String imageSrc) {
        this.id = name;
        this.title = title;
        this.lecturerName = lecturerName;
        this.imageSrc = imageSrc;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImageSrc(){
        return imageSrc;
    }


}

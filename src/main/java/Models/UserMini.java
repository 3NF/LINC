package Models;

public class UserMini {


    protected String firstName;
    protected String lastName;
    protected String picturePath;

    public UserMini (String firstName, String lastName, String picturePath) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.picturePath = picturePath;
    }

    public String getFirstName()
    {
        return this.firstName;
    }

    public String getLastName()
    {
        return this.lastName;
    }

    public String getPicturePath()
    {
        return picturePath;
    }


}

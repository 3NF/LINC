package Models;


public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String userId;
    private String picturePath;
    private String idToken;
    private String accessToken;
    private String refreshToken;

    public User(String email, String FirstName, String lastName, String userId, String picturePath, String accessToken, String refreshToken)
    {
        this.firstName = FirstName;
        this.email = email;
        this.lastName = lastName;
        this.userId = userId;
        this.picturePath = picturePath;
        this.idToken = idToken;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getFirstName()
    {
        return this.firstName;
    }

    public String getLastName()
    {
        return this.lastName;
    }

    public String getEmail()
    {
        return this.email;
    }

    public String getUserId()
    {
        return userId;
    }

    public String getPicturePath()
    {
        return picturePath;
    }

    public String getIdToken()
    {
        return idToken;
    }

    public String getAccessToken()
    {
        return accessToken;
    }

    public String getRefreshToken()
    {
        return refreshToken;
    }
}

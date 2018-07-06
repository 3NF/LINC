package Models;


import com.google.gson.Gson;

public class User {
    private String firstName;
    private String lastName;
    private String picturePath;

    private transient String email;
    private transient String userId;
    private transient String idToken;
    private transient String accessToken;
    private transient String refreshToken;

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

    public User(String userId,String email,String picturePath){
        this.userId = userId;
        this.email = email;
        this.picturePath = picturePath;
    }

    public User(String userId){
        this.userId = userId;
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

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

package Models;


public class User extends UserMini {
    private String email;
    private String userId;
    private String idToken;
    private String accessToken;
    private String refreshToken;

    public User(String email, String firstName, String lastName, String userId, String picturePath, String accessToken, String refreshToken)
    {
        super(firstName, lastName, picturePath);
        this.userId = userId;


        this.email = email;
        this.idToken = idToken;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public User(String userId,String email,String picturePath){
        super( null, null, picturePath);
        this.userId = userId;
        this.email = email;
    }

    public User(String userId) {
        super(null, null, null);
        this.userId = userId;
    }

    public String getEmail()
    {
        return this.email;
    }

    public String getUserId()
    {
        return userId;
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

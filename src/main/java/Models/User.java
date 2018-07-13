package Models;


import Database.GAPIManager;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.gson.Gson;

public class User {
    private String firstName;
    private String lastName;
    private String picturePath;

    private transient String email;
    private transient String userId;
    private transient String accessToken;
    private transient String refreshToken;
    private transient GoogleCredential credentials;

    public User(String email, String FirstName, String lastName, String userId, String picturePath, String accessToken, String refreshToken) {
        this.firstName = FirstName;
        this.email = email;
        this.lastName = lastName;
        this.userId = userId;
        this.picturePath = processPictureLink(picturePath);
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;

        this.credentials =    new GoogleCredential.Builder().setJsonFactory(GAPIManager.JACKSON_FACTORY).setClientSecrets(GAPIManager.secrets).setTransport(GAPIManager.HTTP_TRANSPORT).
                build().setAccessToken(accessToken).setRefreshToken(refreshToken);
    }


    public User(String userId,String email,String picturePath){
        this.firstName = "Firstname";
        this.lastName = "Lastname";
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

    public String getAccessToken()
    {
        return accessToken;
    }

    public String getRefreshToken()
    {
        return refreshToken;
    }

    public String processPictureLink (String picturePath) {
        if (picturePath.charAt(0) == '/' && picturePath.charAt(1) =='/') {
            return "https:" + picturePath;
        }
        return picturePath;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public GoogleCredential getCredential() {
        return credentials;
    }

}

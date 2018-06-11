package Models;


public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String userId;
    private String picturePath;
    private String idToken;

    public User(String email , String FirstName, String lastName, String userId, String picturePath, String idToken) {
        this.firstName = FirstName;
        this.email = email;
        this.lastName = lastName;
        this.userId = userId;
        this.picturePath = picturePath;
        this.idToken = idToken;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public String getUserId() { return userId; }

    public String getPicturePath() { return picturePath; }

    public String getIdToken() { return idToken; }
}

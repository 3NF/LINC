package Models;

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String userId;

    public User(String email , String FirstName, String lastName, String userId) {
        this.firstName = FirstName;
        this.email = email;
        this.lastName = lastName;
        this.userId = userId;
    }

    /**
     * @return HelperClasses.User First Name
     */
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * @return HelperClasses.User SecondName
     */
    public String getLastName() {
        return this.lastName;
    }

    /**
     * @return HelperClasses.User e-mail Adress
     */
    public String getEmail() {
        return this.email;
    }
}

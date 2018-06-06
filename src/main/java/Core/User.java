package Core;

public class User {
    public enum Role {
        admin,
        lecturer,
        seminarLeader,
        student,
        sectionLeader
    }

    private String firstName;
    private String lastName;
    private String email;

    public User(String email , String FirstName, String lastName) {
        this.firstName = FirstName;
        this.email = email;
        this.lastName = lastName;
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

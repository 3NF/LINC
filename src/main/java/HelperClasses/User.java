package HelperClasses;

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
    private Role userRole;
    public User(String email , String FirstName, String lastName,Role userRole) {
        this.firstName = FirstName;
        this.email = email;
        this.lastName = lastName;
        this.userRole = userRole;
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


    /**
     * this is override of getRole function.
     *
     * @return The HelperClasses.User Role
     */
    public Role getRole() {
        return userRole;
    }
}

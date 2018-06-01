package HelperClasses;

public abstract class User {
    public enum Role {
        admin,
        lecturer,
        seminarLeader,
        student
    }

    /**
     * @return HelperClasses.User First Name
     */
    public abstract String getFirstNme();

    /**
     * @return HelperClasses.User SecondName
     */
    public abstract String getLastName();

    /**
     * @return HelperClasses.User e-mail Adress
     */
    public abstract String getEmail();

    /**
     * Administrator,HelperClasses.Lecturer,HelperClasses.Student and etc. is the HelperClasses.User role
     *
     * @return The HelperClasses.User Role
     */
    public abstract Role getRole();
}

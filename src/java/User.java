public abstract class User {
    public enum Role {
        admin,
        lecturer,
        seminarLeader,
        student;
    }

    /**
     * @return User First Name
     */
    public abstract String getFirstNme();

    /**
     * @return User SecondName
     */
    public abstract String getLastName();

    /**
     * @return User e-mail Adress
     */
    public abstract String getEmail();

    /**
     * Administrator,Lecturer,Student and etc. is the User role
     *
     * @return The User Role
     */
    public abstract Role getRole();
}

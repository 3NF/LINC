public class user {
    private String firstName;
    private String secondName;
    private String email;
    private String password;

    /**
     * seter of firstName
     * @param firstName
     *
     */
    private void setName(String firstName){
        this.firstName = firstName;
    }

    /**
     * seter of secondname
     * @param secondName
     */
    private void setSecondName(String secondName){
        this.secondName = secondName;
    }

    /**
     * seter of password
     * @param password
     */
    private void setPassword(String password){
        this.password = password;
    }

    /**
     *
     * @return User First Name
     */
    public String getFirstNme(){
        return this.firstName;
    }

    /**
     *
     * @return user SecondName
     */
    public String getSecondName(){
        return this.secondName;
    }

    public String getEmail(){
        return this.email;
    }
}

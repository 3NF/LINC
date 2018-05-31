package HelperClasses;

public  class Admin extends User{
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public Admin(String email, String Password,String FirstName,String LastName){
        this.firstName = FirstName;
        this.password = password;
        this.email = email;
        this.lastName = lastName;
    }

    /**
     *
     * @return HelperClasses.User First Name
     */
    public  String getFirstNme(){
        return this.firstName;
    }

    /**
     *
     * @return HelperClasses.User SecondName
     */
    public  String getLastName(){
        return this.lastName;
    }

    /**
     *
     * @return   HelperClasses.User e-mail Adress
     */
    public  String getEmail(){
        return this.email;
    }


    /**
     * this is override of getRole function.
     * @return The HelperClasses.User Role
     */
    @Override
    public Role getRole(){
        return Role.admin;
    }

}

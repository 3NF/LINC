package Database;

import Models.User;
import org.junit.Before;
import org.junit.Test;

public class UserDAOTester {

    @Test
    public void getInstructorTypeTester () {
        String userID = "114260512501360115146";
        String courseID = "15887333289";

        System.out.println (UserDAO.getInstructorType(userID, courseID));
    }


    @Test
    public void addUserTest(){
        String userID = "115209239224583784484";
        String courseID = "15887333289";
        UserDAO.Role role = UserDAO.Role.SeminarReader;
        UserDAO.addUser(userID , role , courseID);

    }
}

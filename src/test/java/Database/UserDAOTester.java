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
}

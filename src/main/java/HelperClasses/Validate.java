package HelperClasses;

import Database.GAPIManager;
import Models.User;
import com.google.api.services.classroom.model.Course;

import javax.servlet.http.HttpSession;
import java.util.List;

public class Validate {

    /**
     * @return if user is in Session return true,otherwise return false
     * */
    public static boolean isLogged(HttpSession session){
        return session.getAttribute("user") != null;
    }


    public static boolean isUserInClassroom(User user,String classromID){

        List<Course> courses = GAPIManager.getInstance().getAllRooms(user);
        assert courses != null;
        for (Course course : courses)
        {
            if (course.getId().equals(classromID)) return true;
        }
        return false;
    }
}

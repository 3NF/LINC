package HelperClasses;

import Database.RoomDAO;
import Models.BasicRoomInfo;
import Models.User;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.servlet.http.HttpSession;
import java.util.List;

public class Validate {

    /**
     * @return if user is in Session return true,otherwise return false
     * */
    public static boolean isSesion(HttpSession session){
        if (session.getAttribute("user") != null)
            return true;
        return false;
    }


    public static boolean isUserInClassroom(HttpSession session,String classromID){
        RoomDAO roomDAO = new RoomDAO();
        User user = (User) session.getAttribute("user");
        List<BasicRoomInfo> basicRoom = roomDAO.getUserRooms(user.getEmail());
        for (BasicRoomInfo room : basicRoom){
            if (room.getId().equals(classromID)) return true;
        }
        return false;
    }
}

package Database;

import Core.Room;
import Models.BasicRoomInfo;

public class RoomDAO implements RoomDAOI
{

    /**
     * @param room Unique name of room
     * @return corresponding Room object
     */
    public Room getRoomByName(String room)
    {
        return null;
    }


    /**
     * @param userID unique user id obtained from google
     * @return Rooms in which user is joined
     */
    public BasicRoomInfo[] getUserBasicRooms(String userID)
    {
        return null;
    }
}

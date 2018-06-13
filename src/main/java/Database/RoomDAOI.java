package Database;

import Models.BasicRoomInfo;

public interface RoomDAOI {

	BasicRoomInfo[] getUserBasicsRooms(String id_token);

}

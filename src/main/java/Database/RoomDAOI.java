package Database;

import Models.BasicRoomInfo;

public interface RoomDAOI {

	BasicRoomInfo[] getUserBasicRooms(String id_token);

}

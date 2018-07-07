package Database;

import Models.User;
import org.junit.Before;
import org.junit.Test;

public class UserStorageTester {

    UserStorage userStorage;
    //Logged in users

    String giorgiID = "105303857051815287047";
    String dzlieraID = "103800297754371593008";
    String bakurID = "";

    //Not yet logged in user
    String lukaID = "114260512501360115146";

    @Before
    public void createUserStorage () {
        userStorage = new UserStorage(GAPIManager.getInstance());
    }

    @Test
    public void checkLuka () {
        User user = userStorage.getUserWithID(dzlieraID, lukaID);
        System.out.println(user);
    }

    @Test
    public void checkGiorgi () {
        User user = userStorage.getUserWithID(dzlieraID, giorgiID);
        System.out.println(user);
    }

    @Test
    public void checkChudo()
    {
        User user = userStorage.getUserWithID(bakurID, dzlieraID);
        System.out.println(user);
    }

    @Test
    public void checkBakur() {
        User user = userStorage.getUserWithID(dzlieraID, bakurID);
        System.out.println(user);
    }
}

package Database;

import Models.User;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;

public class UserStorageTester {

    UserStorage userStorage;

    String giorgiID = "105303857051815287047";
    String dzlieraID = "103800297754371593008";
    String bakurID = "104176062122048371294";
    String lukaID = "114260512501360115146";

    @Before
    public void createUserStorage () {
        userStorage = new UserStorage(GAPIManager.getInstance());
    }

    @Test
    public void checkLuka () {
        User user = userStorage.getUserWithID(bakurID, lukaID);
        System.out.println(user);
        assertEquals(user.getEmail(), "lchum16@freeuni.edu.ge");
    }

    @Test
    public void checkGiorgi () {
        User user = userStorage.getUserWithID(dzlieraID, giorgiID);
        System.out.println(user);
        assertEquals(user.getEmail(), "gbagh16@freeuni.edu.ge");
    }

    @Test
    public void checkChudo()
    {
        User user = userStorage.getUserWithID(bakurID, dzlieraID);
        System.out.println(user);
        assertEquals(user.getEmail(), "gchkh16@freeuni.edu.ge");
    }

    @Test
    public void checkBakur() {
        User user = userStorage.getUserWithID(dzlieraID, bakurID);
        System.out.println(user);
        assertEquals(user.getEmail(), "btsut16@freeuni.edu.ge");
    }
}

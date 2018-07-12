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
    String gvantsaID = "106052993686948851837";
    String user1ID = "105167948292585555237";
    String user2ID = "105866623450691913059";
    String user3ID = "101813690455622470701";
    String user4ID = "107360172270692586846";
    String user5ID = "104406936880731125240";
    String user6ID = "103800297754371593008";

    @Before
    public void createUserStorage () {
        userStorage = new UserStorage(GAPIManager.getInstance());
    }

    @Test
    public void checkLuka () {
        User user = userStorage.getUserWithID(bakurID, giorgiID);
        System.out.println(user);
        assertEquals(user.getEmail(), "gbagh16@freeuni.edu.ge");
    }

    /*
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
    */

    @Test
    public void checkGvantsa() {
        User user = userStorage.getUserWithID(lukaID, gvantsaID);
        System.out.println(user);
    }

    @Test
    public void checkUser1() {
        User user = userStorage.getUserWithID(lukaID, user1ID);
        System.out.println(user);
    }

    @Test
    public void checkUser2() {
        User user = userStorage.getUserWithID(lukaID, user2ID);
        System.out.println(user);
    }

    @Test
    public void checkUser3() {
        User user = userStorage.getUserWithID(lukaID, user3ID);
        System.out.println(user);
    }

    @Test
    public void checkUser4() {
        User user = userStorage.getUserWithID(lukaID, user4ID);
        System.out.println(user);
    }

    @Test
    public void checkUser5() {
        User user = userStorage.getUserWithID(lukaID, user5ID);
        System.out.println(user);
    }

    @Test
    public void checkUser6() {
        User user = userStorage.getUserWithID(lukaID, user6ID);
        System.out.println(user);
    }
}

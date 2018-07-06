package Database;

import Models.User;
import org.junit.Before;
import org.junit.Test;

public class UserStorageTester {

    UserStorage userStorage;

    @Before
    public void createUserStorage () {
        userStorage = new UserStorage(GAPIManager.getInstance());
    }

    @Test
    public void checkLuka () {
        User user = userStorage.getUserWithID("114260512501360115146", "15887333289");
        System.out.println(user);
    }

    @Test
    public void checkGiorgi () {
        User user = userStorage.getUserWithID("105303857051815287047", "15887333289");
        System.out.println(user);
    }

}

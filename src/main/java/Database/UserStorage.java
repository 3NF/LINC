package Database;

import Models.User;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class UserStorage {

    HashMap<String, User> users;
    GAPIManager gapiManager;

    public UserStorage (GAPIManager gapiManager) {
        System.out.println(gapiManager);
        this.gapiManager = gapiManager;

        users = new HashMap<>();
    }

    public User getUserWithID (String id, String classroomID) {
        System.out.println("YES" + users);
        if (!users.containsKey(id)) {
            User ad = gapiManager.getUserById(id, classroomID);
            users.put(id, ad);
        }
        return users.get(id);
    }
}

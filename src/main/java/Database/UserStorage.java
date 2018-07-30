package Database;

import Models.User;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class UserStorage {

    HashMap<String, User> users;
    GAPIManager gapiManager;

    public UserStorage (GAPIManager gapiManager) {
        this.gapiManager = gapiManager;

        users = new HashMap<>();
    }

    public User getUserWithID (String requesterID, String targetId) {
        if (!users.containsKey(targetId)) {
            User ad = gapiManager.getUserProfile(requesterID, targetId);
            users.put(targetId, ad);
        }
        return users.get(targetId);
    }
}

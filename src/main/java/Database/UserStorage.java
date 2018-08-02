package Database;

import Models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class UserStorage {

    private HashMap<String, User> users;
    private GAPIManager gapiManager;

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

    public List<User> getUsersWithIds (String teacherIds, List<String> userIds) {
        List<User> res = new ArrayList<>();
        List<String> usersNotInUserStorage = new ArrayList<>();
        for (String userId : userIds) {
            if (!users.containsKey(userId)) {
                usersNotInUserStorage.add(userId);
            } else {
                res.add(users.get(userId));
            }
        }

        List<User> newUsers = gapiManager.getUsersWithIds(teacherIds, usersNotInUserStorage);
        res.addAll(newUsers);
        return res;
    }
}

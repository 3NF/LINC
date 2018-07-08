package Interfaces;

import Database.UserStorage;

public interface UserRetriever {

    void RetrieveUsers (String requesterID, UserStorage userStorage);
}

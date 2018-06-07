package HelperClasses;

import Models.User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {

    private User user1;
    private User user2;
    private User user3;
    private User user4;
    private User user5;

    @Before
    public void biuld() {
        user1 = new User("btsut16@freeuni.edu.ge", "bakuri", "tsutskhashvili");
        user2 = new User("lchum16@freeuni.edu.ge", "luka", "chumburidze");
        user3 = new User("gbagh16@freeuni.edu.ge",  "giorgi", "baghdavadze");
        user4 = new User("gchkh16@freeuni.edu.ge",  "giorgi", "chkhikvadze");
        user5 = new User("gtsut16@freeuni.edu.ge",  "gvantsa", "tsutskhashvili");
    }

    @Test
    public void getFirstName() {
        assertEquals(user1.getFirstName(), "bakuri");
        assertEquals(user2.getFirstName(), "luka");
        assertEquals(user3.getFirstName(), "giorgi");
        assertEquals(user4.getFirstName(), "giorgi");
        assertEquals(user5.getFirstName(), "gvantsa");
    }

    @Test
    public void getLastName() {
        assertEquals(user1.getLastName(), "tsutskhashvili");
        assertEquals(user2.getLastName(), "chumburidze");
        assertEquals(user3.getLastName(), "baghdavadze");
        assertEquals(user4.getLastName(), "chkhikvadze");
        assertEquals(user5.getLastName(), "tsutskhashvili");
    }

    @Test
    public void getEmail() {
        assertEquals(user1.getEmail(), "btsut16@freeuni.edu.ge");
        assertEquals(user2.getEmail(), "lchum16@freeuni.edu.ge");
        assertEquals(user3.getEmail(), "gbagh16@freeuni.edu.ge");
        assertEquals(user4.getEmail(), "gchkh16@freeuni.edu.ge");
        assertEquals(user5.getEmail(), "gtsut16@freeuni.edu.ge");

    }
}
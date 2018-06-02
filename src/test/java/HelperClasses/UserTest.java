package HelperClasses;

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
		user1 = new User("btsut16@freeuni.edu.ge", "matarebeli", "bakuri", "tsutskhashvili");
		user2 = new User("lchum16@freeuni.edu.ge", "123", "luka", "chumburidze");
		user3 = new User("gbagh16@freeuni.edu.ge", "giorgi12", "giorgi", "baghdavadze");
		user4 = new User("gchkh16@freeuni.edu.ge", "dzliera", "giorgi", "chkhikvadze");
		user5 = new User("gtsut16@freeuni.edu.ge", "green12", "gvantsa", "tsutskhashvili");
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
	}

	@Test
	public void getEmail() {
	}

	@Test
	public void getRole() {
	}
}
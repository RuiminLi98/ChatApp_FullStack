package edu.rice.comp504.model;

import edu.rice.comp504.model.user.RegisteredUser;
import edu.rice.comp504.model.user.User;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class UserDBTest {

    @Test
    public void getUsers() {
        assertEquals(UserDB.getUsers().size(), 4);
    }

    @Test
    public void addUser() {
        UserDB.addUser("test", "test2", 18,"swim", "1111");
        assertEquals(UserDB.getUsers().size(), 5);
    }

    @Test
    public void checkUser() {
    }

    @Test
    public void getSessionUserMap() {
    }

    @Test
    public void genNextUserId() {
    }

    @Test
    public void addSessionUser() {
    }

    @Test
    public void getUser() {
    }

    @Test
    public void removeUser() {
    }

    @Test
    public void getSessions() {
    }
}
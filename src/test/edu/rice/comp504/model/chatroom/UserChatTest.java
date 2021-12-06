package edu.rice.comp504.model.chatroom;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserChatTest {
    UserChat test = new UserChat(1 , 2, 2, "test", "userchat",
    "user1", "user2");
    @Test
    public void getUser1() {
        assertEquals(test.getUser1(), "user1");
    }

    @Test
    public void getUser2() {
        assertEquals(test.getUser2(), "user2");
    }

    @Test
    public void propertyChange() {
    }
}
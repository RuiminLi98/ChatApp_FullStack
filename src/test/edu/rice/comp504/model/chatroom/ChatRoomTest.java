package edu.rice.comp504.model.chatroom;

import org.junit.Test;

import java.beans.PropertyChangeEvent;

import static org.junit.Assert.*;

public class ChatRoomTest {
    ChatRoom test = new GroupChat(5, 1, "test", "test2", "test3",
   false, "111");

    @Test
    public void getRoomId() {
        int testNum = test.getRoomId();
        assertEquals(testNum, 1);
    }

    @Test
    public void getUserLimit() {
        int testNum = test.getUserLimit();
        assertEquals(testNum, 5);
    }

    @Test
    public void setUserLimit() {
        test.setUserLimit(10);
        assertEquals(test.getUserLimit(), 10);
    }

    @Test
    public void getRoomName() {
        assertEquals(test.getRoomName(), "test");
    }

    @Test
    public void setRoomName() {
        test.setRoomName("a");
        assertEquals(test.getRoomName(), "a");
    }

    @Test
    public void getType() {
        assertEquals(test.getType(), "groupchat");
    }
}
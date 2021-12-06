package edu.rice.comp504.model.user;

import edu.rice.comp504.model.chatroom.ChatRoom;
import edu.rice.comp504.model.chatroom.GroupChat;
import edu.rice.comp504.model.notification.Notification;
import edu.rice.comp504.model.notification.SimpleNotification;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class UserTest {
    User test = new RegisteredUser("test", "111", "Rice", 18, "test2");

    @Test
    public void addToInterests() {
        test.addToInterests("swim,read");
        assertEquals(test.getInterests().size(), 2);
    }

    @Test
    public void getInterests() {
        test.addToInterests("swim,read");
        assertEquals(test.getInterests().size(), 2);
    }

    @Test
    public void getAge() {
        assertEquals(test.getAge(), 18);
    }

    @Test
    public void getSchool() {
        assertEquals(test.getSchool(), "Rice");
    }

    @Test
    public void getNotificationsList() {
        assertEquals(test.getNotificationsList(), new ArrayList<Notification>());
    }

    @Test
    public void setAge() {
        test.setAge(16);
        assertEquals(test.getAge(), 16);
    }

    @Test
    public void setSchool() {
        test.setSchool("NYU");
        assertEquals(test.getSchool(), "NYU");
    }

    @Test
    public void getUsername() {
        assertEquals(test.getUsername(), "test");
    }

    @Test
    public void setUserName() {
        test.setUserName("test3");
        assertEquals(test.getUsername(), "test3");
    }

    @Test
    public void getPassword() {
        assertEquals(test.getPassword(), "111");
    }

    @Test
    public void setPassword() {
        test.setPassword("222");
        assertEquals(test.getPassword(), "222");
    }

    @Test
    public void getRoomList() {
        assertEquals(test.getRoomList(), new ArrayList<ChatRoom>() );
    }

    @Test
    public void addAChatRoom() {
        test.addAChatRoom(new GroupChat(5, 1, "test", "test2", "test3",
                false, "111"));
        assertEquals(test.getRoomList().size(),  1);
    }

    @Test
    public void removeAChatRoom() {
    }

    @Test
    public void addNotification() {
        test.addNotification(SimpleNotification.make("test", "test2", "test3"));
        assertEquals(test.getNotificationsList().size(), 1);
        test.addNotification(null);
    }

    @Test
    public void setNotificationList() {
        List<Notification> list = new ArrayList<>();
        test.setNotificationList((ArrayList<Notification>) list);
        assertEquals(test.getNotificationsList().size(), 0);
    }

    @Test
    public void removeAllNotification() {
    }
}
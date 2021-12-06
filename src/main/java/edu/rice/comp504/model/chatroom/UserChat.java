package edu.rice.comp504.model.chatroom;

import edu.rice.comp504.model.message.Message;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;

/**
 * A concrete class to represent user to user chat room.
 */
public class UserChat extends ChatRoom{

    private final String user1;
    private final String user2;
    private ArrayList<Message> messages;

    /**
     * A constructor for user chat room.
     * @param duration The duration
     * @param userLimit The user limit
     * @param roomId The room id
     * @param roomName The room name
     * @param type The type of the room, user chat will always be userchat
     * @param user1 The first user's username
     * @param user2 The second user's username
     */
    public UserChat(int duration , int userLimit, int roomId, String roomName, String type,
                    String user1, String user2) {
        super(userLimit, roomId, user1 + "," + user2, "userchat");
        this.user1 = user1;
        this.user2 = user2;
    }


    /**
     * Get user 1 of this chat.
     * @return User 1
     */
    public String getUser1() {
        return user1;
    }

    /**
     * Get user 2 of this chat.
     * @return User 2
     */
    public String getUser2() {
        return user2;
    }

    /**
     * According to property change event's content to modified current chat room's instances.
     * @param evt The property change event
     */
    public void propertyChange(PropertyChangeEvent evt) {

    }

}

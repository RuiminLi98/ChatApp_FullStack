package edu.rice.comp504.model.chatroom;

import java.beans.PropertyChangeListener;

/**
 * An abstract class to represent all chat room related objects.
 */
public abstract class ChatRoom implements PropertyChangeListener {


    private int userLimit;
    private int roomId;
    private String roomName;
    private String type;

    /**
     * A constructor for a chat room.
     * @param userLimit An int to limit max user size.
     * @param roomId An int to store current chat room's unique id.
     * @param roomName A string to display the chat room's name.
     * @param type A string to show type of this chat room.
     */
    public ChatRoom(int userLimit, int roomId, String roomName, String type) {
        this.userLimit = userLimit;
        this.roomId = roomId;
        this.roomName = roomName;
        this.type = type;
    }


    /**
     * Get the room id of this chat room.
     * @return The room id of this chat room
     */
    public int getRoomId() {
        return roomId;
    }

    /**
     * Get user limit of this chat room.
     * @return The user limit
     */
    public int getUserLimit() {
        return userLimit;
    }

    /**
     * Set the user limit of this chat room.
     * @param userLimit The user limit integer.
     */
    public void setUserLimit(int userLimit) {
        this.userLimit = userLimit;
    }

    /**
     * Get room name of this chat room.
     * @return The room name
     */
    public String getRoomName() {
        return roomName;
    }

    /**
     * Set the room name to a new room name.
     * @param roomName The new room name
     */
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    /**
     * Get current room's type.
     * @return The type
     */
    public String getType() {
        return type;
    }
}

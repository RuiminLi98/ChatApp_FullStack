package edu.rice.comp504.model;

import edu.rice.comp504.model.chatroom.ChatRoom;
import edu.rice.comp504.model.chatroom.GroupChat;
import edu.rice.comp504.model.chatroom.UserChat;
import edu.rice.comp504.model.user.User;

import java.beans.PropertyChangeEvent;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RoomDB {
    private static RoomDB ONLY;
    public int nextRoomID = 1;
    private final Map<String, ChatRoom> rooms = new ConcurrentHashMap<>();

    private RoomDB() {
    }

    public static RoomDB getOnly() {
        return ONLY;
    }

    /**
     * Make room.
     * @return room
     */
    public static RoomDB make() {
        if (ONLY == null) {
            ONLY = new RoomDB();
        }
        return ONLY;
    }

    public Map<String, ChatRoom> getRooms() {
        return rooms;
    }

    /**
     * Create a group chat room method .
     *
     * @return true is the room is created
     */
    public boolean addGroupRoom(int userLimit, String roomName, String interest, String ownerUsername,
                                boolean isPublic, String roomPassword) {
        if (rooms.containsKey(roomName)) {
            return false;
        }
        GroupChat temp = new GroupChat(userLimit, this.getNextRoomID(), roomName,
                interest, ownerUsername, isPublic, roomPassword);
        rooms.put(roomName, temp);
        return true;
    }

    /**
     * Create a user chat room method .
     *
     * @return true is the room is created
     */
    public boolean addUserChat(String userName, String friendName) {
        String roomName = userName + "," + friendName;
        if (rooms.containsKey(roomName)) {
            return false;
        }
        UserChat userChat = new UserChat(0,2,this.getNextRoomID(),roomName,
                "userchat",userName,friendName);
        rooms.put(roomName,userChat);
        return true;
    }

    public int getNextRoomID() {
        return this.nextRoomID ++;
    }
}

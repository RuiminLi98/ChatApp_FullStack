package edu.rice.comp504.model.chatroom;

import edu.rice.comp504.model.UserDB;
import edu.rice.comp504.model.user.User;

import java.beans.PropertyChangeEvent;
import java.util.*;

/**
 * A concrete class to represent multiple users chat room.
 */
public class GroupChat extends ChatRoom{

    // owner is the username of the owner
    private String owner;
    // adminList and userList are stored as ArrayList<String> with user's username
    private ArrayList<String> adminList;
    private ArrayList<String> userList;
    private int curNumUser;
    private boolean isPublic;
    private String roomPassword;
    private String interest;
    private ArrayList<String> rules;
    private Map<String, List<String>> blockMap;
    private ArrayList<String> muteList;


    /**
     * A constructor for group chat room.
     * @param userLimit The user limit
     * @param roomName The room name
     * @param ownerUsername The username of first user
     * @param isPublic If this room is public or not
     * @param roomPassword The room password
     */
    public GroupChat(int userLimit, int roomId, String roomName, String interest, String ownerUsername,
                     boolean isPublic, String roomPassword) {
        // Call super constructor
        super(userLimit, roomId, roomName, "groupchat");
        // Initialize attributes
        this.owner = ownerUsername;
        this.curNumUser = 1;
        this.isPublic = isPublic;
        this.roomPassword = roomPassword;
        this.interest = interest;
        // Initialize list attributes
        this.adminList = new ArrayList<>();
        this.userList = new ArrayList<>();
        this.rules = new ArrayList<>();
        this.blockMap = new HashMap<>();
        this.muteList = new ArrayList<>();
        // Add elements to list attributes
        this.addToUserList(ownerUsername);
    }

    /**
     * Get admin list of this room.
     * @return The admin list
     */
    public ArrayList<String> getAdminList() {
        return adminList;
    }

    /**
     * Get block map of this room.
     * @return The block map
     */
    public Map<String, List<String>> getBlockMap() {
        return blockMap;
    }

    /**
     * Get all user list of this room
     * @return The user list.
     */
    public ArrayList<String> getUserList() {
        return userList;
    }

    /**
     * Get whether this room is public or not.
     * @return A boolean flag
     */
    public boolean isPublic() {
        return isPublic;
    }


    /**
     * Get current number of all users.
     * @return The num of all users
     */
    public int getCurNumUser() {
        return curNumUser;
    }

    /**
     * Get central admin of this room (room owner).
     * @return The owner's username string
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Get room's password.
     * @return Room password string
     */
    public String getRoomPassword() {
        return roomPassword;
    }

    /**
     * Get room's rule as a list.
     * @return Room rules ArrayList
     */
    public ArrayList<String> getRules() {
        return rules;
    }

    /**
     * get room's mute list.
     * @return Room rules ArrayList
     */
    public ArrayList<String> getMuteList() {
        return muteList;
    }

    /**
     * Add a user into room's mute list.
     * @param username username
     */
    public void addToMuteList(String username) {
        if (!this.muteList.contains(username)) {
            this.muteList.add(username);
        }
    }


    /**
     * Set number of users.
     * @param curNumUser Number of users.
     */
    public void setCurNumUser(int curNumUser) {
        this.curNumUser = curNumUser;
    }

    /**
     * Set this chat room's owner.
     * @param owner Owner's username
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * Set current chat room to private.
     */
    public void setToPrivate() {
        this.isPublic = false;
    }

    /**
     * Set current chat room to public.
     */
    public void setToPublic() {
        this.isPublic = true;
    }

    /**
     * Set the password to this room.
     * @param roomPassword The room's password.
     */
    public void setRoomPassword(String roomPassword) {
        this.roomPassword = roomPassword;
    }

    /**
     * Add rules to rule list.
     * @param rules The rules need to be added
     */
    public void addRules(String rules) {
        try {
            this.rules.addAll(Arrays.asList(rules.split(",")));
        } catch (StringIndexOutOfBoundsException sioobe) {
            System.out.println("IndexOutOfBoundsException, addRules failed!");
        } catch (NullPointerException npe) {
            System.out.println("NullPointerException, addRules failed!");
        }
    }

    public void setUserList(ArrayList<String> userList) {
        this.userList = userList;
    }

    /**
     * Add users to user list.
     * @param users The users as a string input like "aaa, bbb, ..."
     */
    public void addToUserList(String users) {
        try {
            //this.adminList.addAll(Arrays.asList(users.split(",")));
            this.userList.addAll(Arrays.asList(users.split(",")));
        } catch (StringIndexOutOfBoundsException sioobe) {
            System.out.println("IndexOutOfBoundsException, addToBlockList failed!");
        } catch (NullPointerException npe) {
            System.out.println("NullPointerException, addToBlockList failed!");
        }
    }


    /**
     * Add admin to adminList.
     * @param admins The admin or admins as a string input like "aaa, bbb, ..."
     */
    public void addToAdminList(String admins) {
        try {
            this.adminList.addAll(Arrays.asList(admins.split(",")));
        } catch (StringIndexOutOfBoundsException sioobe) {
            System.out.println("IndexOutOfBoundsException, addToAdminList failed!");
        } catch (NullPointerException npe) {
            System.out.println("NullPointerException, addToAdminList failed!");
        }
    }

//    /**
//     * Add block users to blocklist.
//     * @param blockUsers The block users as a string input like "aaa, bbb, ..."
//     */
//    public void addToBlockList(String blockUsers) {
//        try {
//            this.adminList.addAll(Arrays.asList(blockUsers.split(",")));
//        } catch (StringIndexOutOfBoundsException sioobe) {
//            System.out.println("IndexOutOfBoundsException, addToBlockList failed!");
//        } catch (NullPointerException npe) {
//            System.out.println("NullPointerException, addToBlockList failed!");
//        }
//    }

    /**
     * Add block users to blocklist.
     * @param blockUser The block users as a string input like "aaa, bbb, ..."
     */
    public void addToBlockList(String blockUser,String blockedUser) {
        try {
            if (this.blockMap.containsKey(blockUser)) {
                this.blockMap.get(blockUser).add(blockedUser);
            } else {
                List<String> blockList = new ArrayList<>();
                blockList.add(blockedUser);
                this.blockMap.put(blockUser,blockList);
            }
        } catch (StringIndexOutOfBoundsException sioobe) {
            System.out.println("IndexOutOfBoundsException, addToBlockList failed!");
        } catch (NullPointerException npe) {
            System.out.println("NullPointerException, addToBlockList failed!");
        }
    }

    public void addMuteList(String username) {
        this.muteList.add(username);
    }

    /**
     * @return room interest.
     * */
    public String getInterest() {
        return interest;
    }

    /**
     * According to property change event's content to modified current chat room's instances.
     * @param evt The property change event
     */
    public void propertyChange(PropertyChangeEvent evt) {

    }

    /**
     * kick a user out of the room.
     * @param kickedUser kicked
     */
    public void kickUser(String kickedUser) {
        Iterator<String> iterator = this.userList.iterator();
        while (iterator.hasNext()) {
            String user = iterator.next();
            if (user.equals(kickedUser)) {
                iterator.remove();
            }
        }
        // decrease the num by 1
        this.setCurNumUser(this.getCurNumUser() - 1);
        // remove the room from the kicked person's room list
        User user = UserDB.getUsers().get(kickedUser);
        user.removeAChatRoom(this.getRoomName());
    }

}

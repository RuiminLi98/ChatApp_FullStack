package edu.rice.comp504.model.user;

import edu.rice.comp504.model.chatroom.ChatRoom;
import edu.rice.comp504.model.notification.Notification;

import java.beans.PropertyChangeListener;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * User abstract class to store User's related attributes.
 */
public abstract class User implements PropertyChangeListener {
    //private final int userId;
    private String username;
    private String password;
    //private String nickname;
    private String dateOfBirth;
    //private String emailAddress;
    //private final String userType;
    private ArrayList<ChatRoom> roomList;
    private ArrayList<Notification> notificationsList;
    private String school;
    private int age;
    private ArrayList<String> interests;


    /**
     * A constructor for a user.
     */
    public User(String username, String password, String school, int age, String interests) {
        //this.userId = uid;
        this.username = username;
        this.password = password;
        //this.nickname = nickname;
        //this.dateOfBirth = dateOfBirth;
        //this.emailAddress = emailAddress;
        // we can delete the type since the room class has difficult arrays for different roles
        // if we assign the type to the user class, we need to change it when we change the room
        //this.userType = type; be careful !
        this.roomList = new ArrayList<>();
        this.notificationsList = new ArrayList<>();
        this.interests = new ArrayList<>();
        this.school = school;
        this.age = age;
        this.addToInterests(interests);
    }

    /**
     * Add interests as a string to string array to this user.
     * @param interests The interests
     */
    public void addToInterests(String interests) {
        try {
            this.interests.addAll(Arrays.asList(interests.split(", ")));
        } catch (NullPointerException npe) {
            System.out.println("NullPointerException, addToInterests failed!");
        }
    }

    /**
     * Get interests array list of this user.
     * @return The interests string array list
     */
    public ArrayList<String> getInterests() {
        return interests;
    }

    /**
     * Get age of this user.
     * @return The age
     */
    public int getAge() {
        return age;
    }

    /**
     * Get school name of this user.
     * @return The school string
     */
    public String getSchool() {
        return school;
    }

    /**
     * Get notification list for this user.
     * @return The arraylist of notification object.
     */
    public ArrayList<Notification> getNotificationsList() {
        return notificationsList;
    }

    /**
     * Set age of this user.
     * @param age The age
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Set school of this user.
     * @param school The school
     */
    public void setSchool(String school) {
        this.school = school;
    }

    /**
     * Get user ID of current user.
     * @return The user ID
     */
    /*public int getUserId() {
        return this.userId;
    }*/

    /**
     * Get username of current user.
     * @return The username
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Set the username of current user.
     * @param username The username that needs to be changed to
     */
    public void setUserName(String username) {
        this.username = username;
    }

    /**
     * Get password of current user.
     * @return Password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the password to a new one for current user.
     * @param password The password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get nickname or display name of current user.
     * @return The nickname
     */
    /*public String getNickname() {
        return nickname;
    }*/

    /**
     * Set the current user's nickname to target nickname.
     * @param nickname The target nickname
     */
    /*public void setNickname(String nickname) {
        this.nickname = nickname;
    }*/

    /**
     * Get date of birth of current user.
     * @return String type of date of birth.
     */
    /*public String getDateOfBirth() {
        return dateOfBirth;
    }*/

    /**
     * Set the date of birth of current user.
     * @param dateOfBirth The date of birth that needs to be set to current user
     */
    /*public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }*/

    /**
     * Get email address of current user.
     * @return Email address
     */
    /*public String getEmailAddress() {
        return emailAddress;
    }*/

    /**
     * Set email address of current user to a new address.
     * @param emailAddress The new email address
     */
    /*public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }*/

    /**
     * Get the user's type.
     * @return User type
     */
    /*public String getUserType() {
        return userType;
    }*/

    /**
     * Get all rooms this user is in.
     * @return An ArrayList of chat room of this user
     */
    public ArrayList<ChatRoom> getRoomList() {
        return roomList;
    }

    /**
     * Add one ChatRoom object to user's chat room list.
     * @param cr The ChatRoom object.
     */
    public void addAChatRoom(ChatRoom cr) {
        try {
            this.roomList.add(cr);
        } catch (NullPointerException npe) {
            System.out.println("NullPointerException, addAChatRoom failed!");
        }
    }

    /**
     * Remove a chat room by its room id.
     * @return A ChatRoom object
     */
    public ChatRoom removeAChatRoom(ChatRoom room) {
        //TODO: Remove a specific chat room for this user
        this.roomList.remove(room);
        return null;
    }

    /**
     * Remove.
     * @param roomName room
     */
    public void removeAChatRoom(String roomName) {
        Iterator<ChatRoom> iterator = this.getRoomList().iterator();
        while (iterator.hasNext()) {
            ChatRoom room = iterator.next();
            if (room.getRoomName().equals(roomName)) {
                iterator.remove();
            }
        }
    }

    /**
     * Add one Notification object to user's notifications list
     * @param n The Notification object.
     */
    public void addNotification(Notification n) {
        try {
            this.notificationsList.add(n);
        } catch (NullPointerException npe) {
            System.out.println("NullPointerException, addNotification failed!");
        }
    }

    /**
     * set a new notifications list.
     * @param list The Notification object.
     */
    public void setNotificationList(ArrayList<Notification> list) {
        try {
            this.notificationsList = list;
        } catch (NullPointerException npe) {
            System.out.println("NullPointerException, set Notification List failed!");
        }
    }

    /**
     * Remove all notification.
     */
    public void removeAllNotification() {
        this.notificationsList.clear();
    }

}

package edu.rice.comp504.model;

import edu.rice.comp504.model.user.RegisteredUser;
import edu.rice.comp504.model.user.User;
import org.eclipse.jetty.websocket.api.Session;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * UserDB is used to store users.
 */
public class UserDB {
    private static final Map<Session, String> sessionUserMap =  new ConcurrentHashMap<>();
    private static int nextUserId = 1;
    // TODO: This userDB will be implemented to something like UserStore, it will store all users
    //      and their relationship to their chatroom.
    private static final Map<String, User> users = new ConcurrentHashMap<>(){
        {
            put("admin", new RegisteredUser("admin", "123", "Rice University", 20, "swim, music"));
            put("aaa", new RegisteredUser("aaa", "123", "Nanjing University", 21, "swim"));
            put("bbb", new RegisteredUser("bbb", "123", "Yale University", 22, "swim, football"));
            put("ccc", new RegisteredUser("ccc", "123", "MIT", 23, "swim, music, aaaaaaaaaaaaaaaaaaaaaa"));
        }
    };

    private static Map<String,Integer> hateSpeechCount = new ConcurrentHashMap<>();

    /**
     * Constructor.
     */
    public UserDB() {
    }

    /**
     * Get the username to User Class map.
     * @return The available users
     */
    public static Map<String, User> getUsers() {
        return users;
    }

    /**
     * Add new user to User Class map.
     */
    public static void addUser(String username, String school, int age, String interests, String password) {
        RegisteredUser newUser = new RegisteredUser(username, password, school, age, interests);
        users.put(newUser.getUsername(), newUser);
    }

    /**
     * Check if the user is in User Class map.
     */
    public static Boolean checkUser(String username) {
        return users.containsKey(username);
    }

    /**
     * Get the session to username map.
     * @return The session to username map
     */
    public static Map<Session,String> getSessionUserMap() {
        return sessionUserMap;
    }

    /**
     * Generate the next user id.
     * @return The next user id
     */
    public static int genNextUserId() {
        return nextUserId++;
    }

    /**
     * Add a session user.
     * @param session The session.
     * @param username The username.
     */
    public static void addSessionUser(Session session, String username) {
        sessionUserMap.put(session, username);
    }

    /**
     * Get the user.
     * @param session The session.
     * @return The username
     */
    public static String getUser(Session session) {
        return sessionUserMap.get(session);
    }

    /**
     * Remove user.
     * @param session The session.
     */
    public static void removeUser(Session session) {
        sessionUserMap.remove(session);
    }

    /**
     * Get open sessions.
     * @return All open sessions
     */
    public static Set<Session>  getSessions() {
        return sessionUserMap.keySet();
    }

    public static String getUserBySession(Session session) {
        return sessionUserMap.get(session);
    }

    public static Map<String, Integer> getHateSpeechCount() {
        return hateSpeechCount;
    }

    public static void setHateSpeechCount(Map<String, Integer> hateSpeechCount) {
        UserDB.hateSpeechCount = hateSpeechCount;
    }


    /*public static String getUserBySession(Session session) {
        return sessionUserMap.getOrDefault(session,"null");
    }*/
}

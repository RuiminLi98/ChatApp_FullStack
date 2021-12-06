package edu.rice.comp504.model.user;

import java.beans.PropertyChangeEvent;

/**
 * NullUser class is to implement User abstract class to prevent null pointer exception.
 */
public class NullUser extends User{

    /**
     * Null user constructor.
     //* @param uid User ID
     * @param username Username
     * @param password Password
     //* @param nickname Nickname
     //* @param dateOfBirth Date of birth
     //* @param emailAddress Email address
     */
    /*public NullUser(int uid, String username, String password, String nickname,
                          String dateOfBirth, String emailAddress) {
        super(-1, "null", "null", "null",
                "00-00-0000",  "null", "null", "null", -1, "null");
    }*/
    public NullUser(String username, String password, String school, int age, String interests) {
        super("null", "null",
                "null",  -1, "null");
    }

    /**
     * According to property change event's content to modified current chat room's instances.
     * @param evt The property change event
     */
    public void propertyChange(PropertyChangeEvent evt) {

    }

}

package edu.rice.comp504.model.user;

import java.beans.PropertyChangeEvent;

/**
 * RegisteredUser class is a concrete class to implement User abstract class,
 * to any registered user, we should use this class to store data and make functions.
 */
public class RegisteredUser extends User{

    /**
     * Registered user constructor.
     //* @param uid User ID
     * @param username Username
     * @param password Password
     //* @param nickname Nickname
     //* @param dateOfBirth Date of birth
     //* @param emailAddress Email address
     */
    public RegisteredUser(String username, String password, String school, int age, String interests) {
        super( username, password, school, age, interests);
    }


    /**
     * According to property change event's content to modified current chat room's instances.
     * @param evt The property change event
     */
    public void propertyChange(PropertyChangeEvent evt) {

    }

}

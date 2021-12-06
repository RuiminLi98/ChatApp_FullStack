package edu.rice.comp504.model.notification;

/**
 * Interface for Notification Factory.
 */
public interface INotificationFac {

    /**
     * Make function.
     * @return A notification
     */
    Notification make(String type, String sender, String receiver, String info);


}

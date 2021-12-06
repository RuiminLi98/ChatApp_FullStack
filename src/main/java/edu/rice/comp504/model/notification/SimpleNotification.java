package edu.rice.comp504.model.notification;

/**
 * A concrete class to represent all those notifications without any interactions.
 */
public class SimpleNotification extends Notification{


    /**
     * A constructor.
     * @param receiver Receive user's username
     * @param info The notification info
     */
    private SimpleNotification(String sender, String receiver, String info) {
        super(sender, receiver, info, "simple", false);
    }


    /**
     * Make function.
     * @param receiver Receive user's username
     * @param info The notification info
     * @return A invite notification
     */
    public static Notification make(String sender, String receiver, String info) {
        return new SimpleNotification(sender, receiver, info);
    }



}

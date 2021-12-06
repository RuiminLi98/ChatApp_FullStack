package edu.rice.comp504.model.notification;

/**
 * A concrete class to represent all those notifications which can have interaction buttons.
 */
public class InteractNotification extends Notification{


    /**
     * A constructor.
     * @param sender Send user's username
     * @param receiver Receive user's username
     * @param info The notification info
     */
    private InteractNotification(String sender, String receiver, String info) {
        super(sender, receiver, info, "interact", true);
    }

    /**
     * Make function.
     * @param sender Send user's username
     * @param receiver Receive user's username
     * @param info The notification info
     * @return A invite notification
     */
    public static Notification make(String sender, String receiver, String info) {
        return new InteractNotification(sender, receiver, info);
    }

}

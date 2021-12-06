package edu.rice.comp504.model.notification;

public class AcceptNotification extends Notification{

    /**
     * A constructor for notification.
     * @param sender Send user's username
     * @param receiver  Receive user's username
     * @param info      The notification info
     */
    public AcceptNotification(String sender, String receiver, String info) {
        super(sender, receiver, info, "accept", false);
    }

    /**
     * Make function.
     * @param sender Send user's username
     * @param receiver Receive user's username
     * @param info The notification info
     * @return A invite notification
     */
    public static Notification make(String sender, String receiver, String info) {
        return new AcceptNotification(sender, receiver, info);
    }
}

package edu.rice.comp504.model.notification;

public class RejectNotification extends Notification{

    /**
     * A constructor for notification.
     * @param sender Send user's username
     * @param receiver  Receive user's username
     * @param info      The notification info
     */
    public RejectNotification(String sender, String receiver, String info) {
        super(sender, receiver, info, "reject", false);
    }

    /**
     * Make function.
     * @param sender Send user's username
     * @param receiver Receive user's username
     * @param info The notification info
     * @return A invite notification
     */
    public static Notification make(String sender, String receiver, String info) {
        return new RejectNotification(sender, receiver, info);
    }
}

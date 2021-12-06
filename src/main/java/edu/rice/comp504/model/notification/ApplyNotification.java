package edu.rice.comp504.model.notification;

public class ApplyNotification extends Notification{

    /**
     * A constructor for notification.
     * @param sender Send user's username
     * @param receiver  Receive user's username
     * @param info      The notification info
     */
    public ApplyNotification(String sender, String receiver, String info) {
        super(sender, receiver, info, "apply", true);
    }

    /**
     * Make function.
     * @param sender Send user's username
     * @param receiver Receive user's username
     * @param info The notification info
     * @return A invite notification
     */
    public static Notification make(String sender, String receiver, String info) {
        return new ApplyNotification(sender, receiver, info);
    }
}

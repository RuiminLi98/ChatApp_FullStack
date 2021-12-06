package edu.rice.comp504.model.notification;

public class WarningNotification extends Notification{

    /**
     * A constructor for notification.
     * @param sender Send user's username
     * @param receiver  Receive user's username
     * @param info      The notification info
     */
    public WarningNotification(String sender, String receiver, String info) {
        super(sender, receiver, info, "warn", false);
    }

    /**
     * Make function.
     * @param sender Send user's username
     * @param receiver Receive user's username
     * @param info The notification info
     * @return A warning notification
     */
    public static Notification make(String sender, String receiver, String info) {
        return new WarningNotification(sender, receiver, info);
    }
}

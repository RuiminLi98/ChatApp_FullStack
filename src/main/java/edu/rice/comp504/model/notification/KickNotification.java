package edu.rice.comp504.model.notification;

public class KickNotification extends Notification{
    /**
     * A constructor for notification.
     * @param sender Send user's username
     * @param receiver  Receive user's username
     * @param info      The notification info
     */
    public KickNotification(String sender, String receiver, String info) {
        super(sender, receiver, info, "kick", false);
    }

    /**
     * Make function.
     * @param sender Send user's username
     * @param receiver Receive user's username
     * @param info The notification info
     * @return A kick notification
     */
    public static Notification make(String sender, String receiver, String info) {
        return new KickNotification(sender, receiver, info);
    }
}

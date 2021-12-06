package edu.rice.comp504.model.notification;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * An abstract class to represent all notifications with invariant attributes across all types of notifications.
 */
public abstract class Notification implements INotificationFac{

    private String receiver;
    private String sender;
    private String info;
    private boolean readStatus;
    private String type;
    private boolean hasButton;
    private String timestamp;

    /**
     * A constructor for notification.
     * @param receiver Receive user's username
     * @param info The notification info
     * @param type Type of this notification
     */
    public Notification(String sender, String receiver, String info, String type, boolean hasButton) {
        this.receiver = receiver;
        this.sender = sender;
        this.info = info;
        this.readStatus = false;
        this.type = type;
        this.hasButton = hasButton;
        //
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        this.timestamp = dtf.format(now);
    }

    /**
     * Get this notification's type.
     * @return The type
     */
    public String getType() {
        return type;
    }

    /**
     * Get if this notification has buttons or not.
     * @return Has button flag
     */
    public boolean isHasButton() {
        return hasButton;
    }

    /**
     * Get if this notification is read or not.
     * @return Read status flag
     */
    public boolean isReadStatus() {
        return readStatus;
    }

    /**
     * Get info of this notification.
     * @return The information
     */
    public String getInfo() {
        return info;
    }

    /**
     * Get receiver of this notification.
     * @return The receiver's username
     */
    public String getReceiver() {
        return receiver;
    }

    /**
     * Set this notification as no longer has buttons.
     */
    public void buttonRemove() {
        this.hasButton = false;
    }

    /**
     * Set this notification as read.
     */
    public void notificationRead() {
        this.readStatus = true;
    }


    /**
     * Make function.
     */
    @Override
    public Notification make(String type, String sender, String receiver, String info) {
        return NullNotification.make();
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

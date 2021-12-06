package edu.rice.comp504.model.notification;

/**
 * A concrete class to represent all those non-meaningful notifications, prevent null pointer exception.
 */
public class NullNotification extends Notification{

    private static Notification ONLY;

    /**
     * A constructor.
     */
    private NullNotification() {
        super("null", "null", "null", "null",false);
    }

    /**
     * Make function.
     * @return A NullNotification
     */
    public static Notification make() {
        if (ONLY == null) {
            ONLY = new NullNotification();
        }
        return ONLY;
    }

}

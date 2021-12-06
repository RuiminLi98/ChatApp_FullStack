package edu.rice.comp504.model.message;

/**
 * A concrete class to represent one non-meaningful message, prevent null pointer exception.
 */
public class NullMessage extends Message{

    private static Message ONLY;

    /**
     * A constructor for Null Message.
     */
    private NullMessage() {
        super("00-00-0000", "null", "null");
    }

    /**
     * make a message.
     */
    public static Message make() {
        if (ONLY == null) {
            ONLY = new NullMessage();
        }
        return ONLY;
    }


}

package edu.rice.comp504.model.message;

/**
 * A factory class to make messages.
 */
public class MessageFac implements IMessageFac{

    private static Message ONLY;

    /**
     * A constructor.
     */
    public MessageFac() {

    }

    /**
     * Make function.
     * @return The default made message
     */
    public Message make(String type) {
        if (ONLY == null) {
            ONLY = NullMessage.make();
        }
        return ONLY;
    }

}

package edu.rice.comp504.model.chatroom;

import java.beans.PropertyChangeEvent;

/**
 * A concrete class to represent non-meaningful chat room, prevent null pointer exception.
 */
public class NullRoom extends ChatRoom{

    public NullRoom() {
        super(0, 0, "null", "null");
    }

    /**
     * According to property change event's content to modified current chat room's instances.
     * @param evt The property change event
     */
    public void propertyChange(PropertyChangeEvent evt) {

    }

}

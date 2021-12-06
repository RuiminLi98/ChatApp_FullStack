package edu.rice.comp504.model;

import edu.rice.comp504.model.message.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MessageDB {

    private static MessageDB ONLY;
    public int nextMessageID = 1;
    private final Map<String, ArrayList<Message>> messageMap = new ConcurrentHashMap<>();

    /**
     * A constructor.
     */
    private MessageDB() {

    }

    /**
     * Make function for singleton pattern.
     * @return Static MessageDB object
     */
    public static MessageDB make() {
        if (ONLY == null) {
            ONLY = new MessageDB();
        }
        return ONLY;
    }


    /**
     * Getter for MessageMap.
     * @return map
     */
    public Map<String, ArrayList<Message>> getMessageMap() {
        return messageMap;
    }

    /**
     * Add message to the database and return the message.
     * @param sender Sender's username
     * @param room Room name
     * @param body Message body string
     * @param type Message type
     * @return a message
     */
    public Message addMessage(String sender, String room, String body, String type) {
        Message newMessage = NullMessage.make();
        // Attention: here using some pre-defined parameter since we did not set it up in front-end
        switch (type) {
            case "text":
                newMessage = TextMessage.make("auto", sender, body, "default", "black", 12);
                break;

            case "image":
                newMessage = ImageMessage.make("auto", sender, body, 1.0);
                break;

            case "composite":
                newMessage = new CompositeMessage("auto", sender);
                ((CompositeMessage) newMessage).addMultipleChildFromString(body);
                break;

            default:
                return newMessage;
        }
        // If new message is not null which means it is created successfully, return true and put it in map
        if (!newMessage.getType().equals("null")) {
            if (!messageMap.containsKey(room)) {
                messageMap.put(room, new ArrayList<>());
            }
            messageMap.get(room).add(newMessage);
        } else {
            return newMessage;
        }
        return newMessage;
    }



    /**
     * Get next message IDs.
     * @return nextMessageID
     */
    public int getNextMessageID() {
        return this.nextMessageID ++;
    }


}

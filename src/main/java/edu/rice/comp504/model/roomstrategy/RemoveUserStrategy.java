package edu.rice.comp504.model.roomstrategy;

import edu.rice.comp504.model.chatroom.ChatRoom;

/**
 * A strategy to remove multiple users in one ChatRoom.
 */
public class RemoveUserStrategy implements IUpdateRoomStrategy{

    /**
     * A constructor.
     */
    public RemoveUserStrategy() {

    }

    /**
     * Get name of this strategy.
     * @return Strategy name
     */
    @Override
    public String getName() {
        return "removeuser";
    }

    /**
     *
     * @param context The chat room.
     */
    @Override
    public void updateState(ChatRoom context) {
        // TODO: Implement the update state function for this chat room.
    }
}

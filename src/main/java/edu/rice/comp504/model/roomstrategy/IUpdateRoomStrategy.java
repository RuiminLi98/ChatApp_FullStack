package edu.rice.comp504.model.roomstrategy;

import edu.rice.comp504.model.chatroom.ChatRoom;

/**
 * Interface for chatroom's package's update strategy.
 */
public interface IUpdateRoomStrategy {

    /**
     * The name of the strategy.
     * @return strategy name
     */
    String getName();

    /**
     * Update the state of the ball.
     * @param context The ball.
     */
    void updateState(ChatRoom context);

}

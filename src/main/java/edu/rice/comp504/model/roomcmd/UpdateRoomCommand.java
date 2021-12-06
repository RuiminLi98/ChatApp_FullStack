package edu.rice.comp504.model.roomcmd;

import edu.rice.comp504.model.chatroom.ChatRoom;

/**
 * A command to update ChatRoom objects.
 */
public class UpdateRoomCommand implements IRoomCmd{


    /**
     * A constructor.
     */
    public UpdateRoomCommand() {

    }

    /**
     * Execute function.
     * @param context The receiver on which the command is executed.
     */
    @Override
    public void execute(ChatRoom context) {
        //TODO: Implement the command to update chatroom context
    }
}

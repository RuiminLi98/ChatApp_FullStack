package edu.rice.comp504.model.roomcmd;

import edu.rice.comp504.model.chatroom.ChatRoom;

/**
 * Interface for chatroom's command.
 */
public interface IRoomCmd {


    /**
     * Execute the command.
     * @param context The receiver on which the command is executed.
     */
    public void execute(ChatRoom context);




}

package edu.rice.comp504.model.roomcmd;

/**
 * Interface for chatroom's package's command factory.
 */
public interface IRoomCmdFac {


    /**
     * Make a room command.
     * @return A room command
     */
    IRoomCmd make(String cmdType);


}

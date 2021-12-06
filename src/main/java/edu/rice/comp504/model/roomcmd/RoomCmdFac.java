package edu.rice.comp504.model.roomcmd;

/**
 * A factory class to make different types of command.
 */
public class RoomCmdFac implements IRoomCmdFac{

    /**
     * A constructor.
     */
    public RoomCmdFac() {

    }

    /**
     * Make function.
     * @param cmdType Command type.
     * @return A room command
     */
    @Override
    public IRoomCmd make(String cmdType) {
        //TODO: Implement the factory design pattern based on command type input.
        switch (cmdType) {
            default:
                return new UpdateRoomCommand();
        }
    }
}

package edu.rice.comp504.model.roomstrategy;

/**
 * A factory class to make different types of room related strategies.
 */
public class RoomStrategyFac implements IRoomStrategyFac{

    /**
     * A constructor.
     */
    public RoomStrategyFac() {

    }

    /**
     * A make function.
     */
    @Override
    public IRoomStrategyFac make(String name) {
        // TODO: Implement factory design pattern based on name.
        return null;
    }
}

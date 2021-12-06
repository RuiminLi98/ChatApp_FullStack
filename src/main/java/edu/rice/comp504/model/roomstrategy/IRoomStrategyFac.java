package edu.rice.comp504.model.roomstrategy;

/**
 * Interface for chatroom's strategies.
 */
public interface IRoomStrategyFac {


    /**
     * Makes a strategy.
     * @return A strategy
     */
    IRoomStrategyFac make(String name);





}

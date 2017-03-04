package com.pploder.ehc;

import com.pploder.events.Event;

/**
 * A console that is provided via a network.
 * The received messages as well as newly opened and closed connection can be observed
 * via message and connection events.
 *
 * @author Philipp Ploder
 * @version 2.0.0
 * @since 1.0.0
 */
public interface Console extends AutoCloseable {

    /**
     * Starts the console.
     *
     * @throws IllegalStateException If the object is not in a state where this action is possible.
     * @throws Exception             If something goes wrong.
     */
    void start() throws Exception;

    /**
     * @return The event for received messages.
     */
    Event<Message> messageReceivedEvent();

    /**
     * @return The event for new active connections.
     */
    Event<Connection> connectionAddedEvent();

    /**
     * @return The event for no longer active connections.
     */
    Event<Connection> connectionRemovedEvent();

    /**
     * Stops the console.
     *
     * @throws IllegalStateException If the object is not in a state where this action is possible.
     * @throws Exception             If something goes wrong.
     */
    void close() throws Exception;

    /**
     * @return The amount of active connections.
     */
    int getConnectionCount();

    /**
     * @return All active connections in no defined order.
     */
    Iterable<Connection> connections();

}

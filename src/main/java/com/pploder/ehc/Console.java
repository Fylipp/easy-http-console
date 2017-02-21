package com.pploder.ehc;

/**
 * A console that is provided via a network.
 * The received messages can be obtained by adding a {@link MessageListener}.
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
     * Adds a message listener.
     *
     * @param messageListener The message listener.
     * @throws NullPointerException If the given reference is {@code null}.
     */
    void addMessageListener(MessageListener messageListener) throws NullPointerException;

    /**
     * Removes the message listener.
     *
     * @param messageListener The message listener.
     * @throws NullPointerException If the given reference is {@code null}.
     */
    void removeMessageListener(MessageListener messageListener) throws NullPointerException;

    /**
     * Supplies the object with a message that will be processed by the appropriate listeners.
     *
     * @param message The message.
     */
    void supplyMessage(Message message);

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

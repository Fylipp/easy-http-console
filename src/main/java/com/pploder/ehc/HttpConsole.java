package com.pploder.ehc;

/**
 * A console that is provided via HTTP.
 *
 * @author Philipp Ploder
 * @version 2.0.0
 * @since 1.0.0
 */
public interface HttpConsole extends AutoCloseable {

    /**
     * Starts the console.
     *
     * @throws Exception If something goes wrong.
     */
    void start() throws Exception;

    /**
     * @return The host that the console is running on.
     */
    String getHost();

    /**
     * @return The port that the console is running on.
     */
    int getPort();

    /**
     * @return The URL of the websocket of the console.
     */
    String getWebsocketURL();

    /**
     * @return The URL of the HTTP console provider.
     */
    String getHttpURL();

    /**
     * Adds a message listener,
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
     * @throws Exception If something goes wrong.
     */
    void close() throws Exception;

}

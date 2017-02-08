package com.pploder.ehc;

/**
 * A console that is provided via HTTP.
 *
 * @author Philipp Ploder
 * @version 1.0.0
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
     * @return The message handler of the console.
     */
    MessageHandler getMessageHandler();

    /**
     * Stops the console.
     *
     * @throws Exception If something goes wrong.
     */
    void close() throws Exception;
}

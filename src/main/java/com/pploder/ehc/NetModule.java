package com.pploder.ehc;

/**
 * An interface used to provide network I/O functionality for a {@link SimpleConsole}.
 *
 * @author Philipp Ploder
 * @version 2.0.0
 * @since 2.0.0
 */
public interface NetModule {

    /**
     * Initializes the module and stores the console.
     *
     * @param console The console.
     */
    void init(Console console) throws Exception;

    /**
     * Starts the module.
     *
     * @throws IllegalStateException If the object is not in a state where this action is possible.
     * @throws Exception             If something goes wrong.
     */
    void start() throws Exception;

    /**
     * Stops the module.
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

    /**
     * @return The host that the console is running on.
     */
    String getHost();

    /**
     * @return The port that the console is running on.
     */
    int getPort();

    /**
     * @return The URL of the HTTP console provider.
     */
    String getHttpURL();

    /**
     * @return The URL of the websocket of the console.
     */
    String getWebsocketURL();

}

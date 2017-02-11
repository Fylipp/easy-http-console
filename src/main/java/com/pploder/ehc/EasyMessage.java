package com.pploder.ehc;

import org.webbitserver.WebSocketConnection;

import java.util.Objects;

/**
 * The default implementation of the {@link Message} interface.
 *
 * @author Philipp Ploder
 * @version 1.0.0
 * @since 1.0.0
 */
public class EasyMessage implements Message {

    private final HttpConsole server;
    private final WebSocketConnection connection;
    private final String message;

    /**
     * Creates a new instance using the given arguments.
     * {@link String}
     *
     * @param server     The server from which the connection originates.
     * @param connection The connection from which the message originates.
     * @param message    The message itself.
     */
    public EasyMessage(HttpConsole server, WebSocketConnection connection, String message) {
        this.server = Objects.requireNonNull(server);
        this.connection = Objects.requireNonNull(connection);
        this.message = Objects.requireNonNull(message).trim();
    }

    @Override
    public HttpConsole getHttpConsole() {
        return server;
    }

    @Override
    public WebSocketConnection getConnection() {
        return connection;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
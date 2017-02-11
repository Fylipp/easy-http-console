package com.pploder.ehc;

import org.webbitserver.WebSocketConnection;

/**
 * Represents input received from a {@link WebSocketConnection} belonging to a {@link HttpConsole}.
 *
 * @author Philipp Ploder
 * @version 1.0.0
 * @since 1.0.0
 */
public interface Message {
    /**
     * @return The server that provided the connection for the message.
     */
    HttpConsole getHttpConsole();

    /**
     * @return The connection from which the message originated.
     */
    WebSocketConnection getConnection();

    /**
     * @return The message in a trimmed form.
     */
    String getMessage();
}
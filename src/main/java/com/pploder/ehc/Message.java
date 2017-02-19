package com.pploder.ehc;

/**
 * Represents input received from a {@link Connection} belonging to a {@link Console}.
 *
 * @author Philipp Ploder
 * @version 2.0.0
 * @since 1.0.0
 */
public interface Message {

    /**
     * @return The server that provided the connection for the message.
     */
    Console getConsole();

    /**
     * @return The connection from which the message originated.
     */
    Connection getConnection();

    /**
     * @return The message in a trimmed form.
     */
    String getMessage();

}

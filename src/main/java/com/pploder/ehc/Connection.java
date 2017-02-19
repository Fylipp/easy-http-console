package com.pploder.ehc;

/**
 * Represents a network connection to a client.
 *
 * @author Philipp Ploder
 * @version 2.0.0
 * @since 2.0.0
 */
public interface Connection {

    /**
     * @return The {@link Console} of this connection.
     */
    Console getConsole();

    /**
     * @return The remote address of the client.
     */
    String getRemoteAddress();

    /**
     * Sends a message via the connection.
     *
     * @param messageContent The content of the message.
     */
    void send(MessageContent messageContent);

    /**
     * Sends an unstyled message with the given content.
     *
     * @param charSequence The content.
     */
    default void send(CharSequence charSequence) {
        send(MessageContent.of(charSequence));
    }

    /**
     * Closes the connection.
     *
     * @throws Exception If something goes wrong.
     */
    void close() throws Exception;

}

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
     * @return The connection from which the message originated.
     */
    Connection getConnection();

    /**
     * @return The message in a trimmed form.
     */
    String getMessage();

    /**
     * Responds to the message with the given message content.
     * This is a convenience method.
     *
     * @param messageContent The message content to send.
     */
    default void respond(MessageContent messageContent) {
        getConnection().send(messageContent);
    }

    /**
     * Responds to the messsage with the given character sequence.
     * This is a convenience method.
     *
     * @param charSequence The character sequence to send.
     */
    default void respond(CharSequence charSequence) {
        getConnection().send(charSequence);
    }

}

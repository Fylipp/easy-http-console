package com.pploder.ehc;

/**
 * Represents a {@link Message} that was tokenized for simpler interpretation.
 *
 * @author Philipp Ploder
 * @version 2.0.0
 * @since 1.0.0
 */
public interface Command {

    /**
     * @return The original message that was used to create this object.
     */
    Message getSource();

    /**
     * @return The command name.
     */
    String getName();

    /**
     * @return The amount of arguments.
     */
    int getArgsCount();

    /**
     * @param i The argument index to get the argument of.
     * @return The argument at the specified index.
     */
    String getArg(int i);

    /**
     * @return The arguments in the correct order.
     */
    Iterable<String> args();

    /**
     * Responds to the message with the given message content.
     * This is a convenience method.
     *
     * @param messageContent The message content to send.
     */
    default void respond(MessageContent messageContent) {
        getSource().respond(messageContent);
    }

    /**
     * Responds to the messsage with the given character sequence.
     * This is a convenience method.
     *
     * @param charSequence The character sequence to send.
     */
    default void respond(CharSequence charSequence) {
        getSource().respond(charSequence);
    }

}

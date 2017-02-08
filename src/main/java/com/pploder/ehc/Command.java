package com.pploder.ehc;

/**
 * Represents a {@link Message} that was tokenized for simpler interpretation.
 *
 * @author Philipp Ploder
 * @version 1.0.0
 * @since 1.0.0
 */
public interface Command {
    /**
     * @return The original message.
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
     * @param i The argument index.
     * @return The argument at the specified index.
     */
    String getArg(int i);
}

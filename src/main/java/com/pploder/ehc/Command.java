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

}

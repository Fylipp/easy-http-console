package com.pploder.ehc;

/**
 * Represents a mapping of commands to {@link CommandHandler} instances.
 *
 * @author Philipp Ploder
 * @version 1.0.0
 * @since 1.0.0
 */
public interface CommandRegistry {
    /**
     * @return The commands registered.
     */
    Iterable<String> registeredCommands();

    /**
     * @param command The command to get the handler of.
     * @return The command handler for the given command.
     */
    CommandHandler getCommandHandler(String command);

    /**
     * @return The command handler that is used when an incoming command is not registered.
     */
    CommandHandler getUnknownCommandHandler();
}

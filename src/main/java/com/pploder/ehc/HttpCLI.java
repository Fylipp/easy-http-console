package com.pploder.ehc;

/**
 * A command-line interface that is provided over HTTP.
 *
 * @author Philipp Ploder
 * @version 2.0.0
 * @since 2.0.0
 */
public interface HttpCLI extends HttpConsole {

    /**
     * Sets the command listener for a command.
     * This will overwrite any previous command listener.
     *
     * @param command         The command for which the listener should be set.
     * @param commandListener The command listener.
     */
    void registerCommandListener(String command, CommandListener commandListener);

    /**
     * Removes the command listener for the given command.
     *
     * @param command The command for which the listener should be removed.
     */
    void removeCommandListener(String command);

    /**
     * @return The commands registered.
     */
    Iterable<String> registeredCommands();

    /**
     * @param command The command to get the listener of.
     * @return The command listener for the given command.
     */
    CommandListener getCommandHandler(String command);

    /**
     * @return The command listener that is used when an incoming command is not registered.
     */
    CommandListener getUnknownCommandListener();

    /**
     * Sets the unknown command listener.
     * This will overwrite any previous unknown command listeners.
     *
     * @param unknownCommandListener The new unknown command listener.
     */
    void setUnknownCommandListener(CommandListener unknownCommandListener);

}

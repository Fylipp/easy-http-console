package com.pploder.ehc;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * A mapping of commands to consumers.
 * Additionally a listener for unknown commands can be registered.
 *
 * @author Philipp Ploder
 * @version 2.0.0
 * @since 2.0.0
 */
public interface CommandRegistry {

    /**
     * Supplies the object with a message that will be processed by the appropriate listener.
     *
     * @param message The message.
     */
    void supplyMessage(Message message);

    /**
     * Sets the command listener for a command.
     * This will overwrite any previous command listener.
     *
     * @param command         The command for which the listener should be set.
     * @param commandListener The command listener.
     */
    void put(String command, Consumer<Command> commandListener);

    /**
     * Removes the command listener for the given command.
     *
     * @param command The command for which the listener should be removed.
     */
    void remove(String command);

    /**
     * @return All the commands registered.
     */
    Iterable<String> commands();

    /**
     * @param command The command to get the listener of.
     * @return The optional command listener for the given command.
     */
    Optional<Consumer<Command>> getCommandListener(String command);

    /**
     * @param command The command to get the listener of.
     * @return The command listener for the given command or the result of {@link #getUnknownCommandListener()} if not found.
     */
    Optional<Consumer<Command>> getCommandListenerOrFallback(String command);

    /**
     * @return The command listener that is used when an incoming command is not registered.
     */
    Optional<Consumer<Command>> getUnknownCommandListener();

    /**
     * Sets the unknown command listener.
     * This will overwrite any previous unknown command listeners.
     *
     * @param unknownCommandListener The new unknown command listener.
     */
    void setUnknownCommandListener(Consumer<Command> unknownCommandListener);

}

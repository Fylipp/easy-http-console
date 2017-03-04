package com.pploder.ehc;

import edu.rice.cs.util.ArgumentTokenizer;
import lombok.extern.slf4j.XSlf4j;

import java.util.*;
import java.util.function.Consumer;

/**
 * The default implementation of {@link CommandRegistry}.
 *
 * @author Philipp Ploder
 * @version 2.0.0
 * @since 1.0.0
 */
@XSlf4j
public class SimpleCommandRegistry implements CommandRegistry {

    /**
     * The default unknown command listener.
     */
    public static final Consumer<Command> DEFAULT_UNKNOWN_COMMAND_LISTENER = cmd -> cmd.getSource().getConnection().send("Unknown command: " + cmd.getName());

    private final Map<String, Consumer<Command>> commandListeners = new HashMap<>();

    private Consumer<Command> unknownCommandListener;

    /**
     * Creates a new instance using the default unknown command listener.
     * The unknown command listener is set to {@link #DEFAULT_UNKNOWN_COMMAND_LISTENER}.
     */
    public SimpleCommandRegistry() {
        this(DEFAULT_UNKNOWN_COMMAND_LISTENER);
    }

    /**
     * Creates a new instance using the given unknown command listener.
     *
     * @param unknownCommandListener The listener for unknown commands.
     */
    public SimpleCommandRegistry(Consumer<Command> unknownCommandListener) {
        setUnknownCommandListener(unknownCommandListener);
    }

    @Override
    public void supplyMessage(Message message) {
        String[] split = ArgumentTokenizer.tokenize(message.getMessage()).toArray(new String[0]);
        String[] args;

        if (split.length > 1) {
            args = Arrays.copyOfRange(split, 1, split.length);
        } else {
            args = new String[0];
        }

        String command = split[0];
        Command cmd = new SimpleCommand(message, command, args);

        getCommandListenerOrFallback(command).ifPresent(commandListener -> commandListener.accept(cmd));
    }

    @Override
    public void put(String command, Consumer<Command> commandListener) {
        commandListeners.put(Objects.requireNonNull(command), Objects.requireNonNull(commandListener));
    }

    @Override
    public void remove(String command) {
        commandListeners.remove(Objects.requireNonNull(command));
    }

    @Override
    public Iterable<String> commands() {
        return commandListeners.keySet();
    }

    @Override
    public Optional<Consumer<Command>> getCommandListener(String command) {
        return Optional.ofNullable(commandListeners.get(Objects.requireNonNull(command)));
    }

    @Override
    public Optional<Consumer<Command>> getCommandListenerOrFallback(String command) {
        Optional<Consumer<Command>> commandListener = getCommandListener(command);

        if (commandListener.isPresent()) {
            return commandListener;
        }

        return getUnknownCommandListener();
    }

    @Override
    public Optional<Consumer<Command>> getUnknownCommandListener() {
        return Optional.ofNullable(unknownCommandListener);
    }

    @Override
    public void setUnknownCommandListener(Consumer<Command> unknownCommandListener) {
        this.unknownCommandListener = unknownCommandListener;
    }

}

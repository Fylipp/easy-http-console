package com.pploder.ehc;

import edu.rice.cs.util.ArgumentTokenizer;

import java.io.IOException;
import java.util.*;

/**
 * An {@link AbstractHttpConsole} with command-line functionality.
 *
 * @author Philipp Ploder
 * @version 1.0.0
 * @since 1.0.0
 */
public class EasyHttpCLI extends AbstractHttpConsole implements CommandRegistry {

    public static final CommandHandler DEFAULT_UNKNOWN_COMMAND_HANDLER = cmd -> cmd.getSource().getConnection().send("[easy-http-console] Unknown command: " + cmd.getName());

    private final Map<String, CommandHandler> commandHandlers = new HashMap<>();

    private CommandHandler unknownCommandHandler;

    /**
     * Creates a new instance using the default unknown command handler.
     *
     * @throws IOException If such an exception is thrown by the super-constructor.
     */
    public EasyHttpCLI() throws IOException {
        this(DEFAULT_UNKNOWN_COMMAND_HANDLER);
    }

    /**
     * Creates a new instance using the given unknown command handler.
     *
     * @param unknownCommandHandler The command handler to use when an incoming command is unrecognized.
     * @throws IOException If such an exception is thrown by the super-constructor.
     */
    public EasyHttpCLI(CommandHandler unknownCommandHandler) throws IOException {
        super();
        this.unknownCommandHandler = unknownCommandHandler;
    }

    /**
     * Creates a new instance using the given port.
     *
     * @param port The port to provide the console on.
     * @throws IOException If such an exception is thrown by the super-constructor.
     */
    public EasyHttpCLI(int port) throws IOException {
        this(port, DEFAULT_UNKNOWN_COMMAND_HANDLER);
    }

    /**
     * Creates a new instance using the given port and unknown command handler.
     *
     * @param port                  The port to provide the console on.
     * @param unknownCommandHandler The command handler to use when an incoming command is unrecognized.
     * @throws IOException If such an exception is thrown by the super-constructor.
     */
    public EasyHttpCLI(int port, CommandHandler unknownCommandHandler) throws IOException {
        super(port);
        this.unknownCommandHandler = unknownCommandHandler;
    }

    /**
     * Creates a new instance using the given host and port.
     *
     * @param host The host to provide the console on.
     * @param port The port to provide the console on.
     * @throws IOException If such an exception is thrown by the super-constructor.
     */
    public EasyHttpCLI(String host, int port) throws IOException {
        this(host, port, DEFAULT_UNKNOWN_COMMAND_HANDLER);
    }

    /**
     * Creates a new instance using the given host, port and unknown command handler.
     *
     * @param host                  The host to provide the console on.
     * @param port                  The port to provide the console on.
     * @param unknownCommandHandler The command handler to use when an incoming command is unrecognized.
     * @throws IOException If such an exception is thrown by the super-constructor.
     */
    public EasyHttpCLI(String host, int port, CommandHandler unknownCommandHandler) throws IOException {
        super(host, port);
        this.unknownCommandHandler = unknownCommandHandler;
    }

    /**
     * Handles incoming messages.
     * This method is returned as a {@link MessageHandler} by {@link #getMessageHandler()}.
     *
     * @param message The incoming message.
     */
    private void handleMessage(Message message) {
        List<String> split = ArgumentTokenizer.tokenize(message.getMessage());

        String[] args = new String[split.size() - 1];

        Iterator<String> iter = split.iterator();
        if (iter.hasNext()) {
            iter.next();

            int i = 0;

            while (iter.hasNext()) {
                args[i++] = iter.next();
            }
        }

        Command cmd = new EasyCommand(message, split.get(0), args);

        CommandHandler commandHandler = getCommandHandler(cmd.getName());
        if (commandHandler == null) {
            CommandHandler fallback = getUnknownCommandHandler();
            if (fallback != null) {
                fallback.accept(cmd);
            }
        } else {
            commandHandler.accept(cmd);
        }
    }

    @Override
    public Iterable<String> registeredCommands() {
        return commandHandlers.keySet();
    }

    @Override
    public CommandHandler getCommandHandler(String command) {
        return commandHandlers.get(command);
    }

    @Override
    public CommandHandler getUnknownCommandHandler() {
        return unknownCommandHandler;
    }

    /**
     * Sets the unknown command handler.
     *
     * @param unknownCommandHandler The new unknown command handler.
     */
    public void setUnknownCommandHandler(CommandHandler unknownCommandHandler) {
        this.unknownCommandHandler = Objects.requireNonNull(unknownCommandHandler);
    }

    @Override
    public MessageHandler getMessageHandler() {
        return this::handleMessage;
    }

    /**
     * Sets the command handler for a command.
     * This will overwrite any previous command handler.
     *
     * @param command        The command for which the handler should be set.
     * @param commandHandler The command handler.
     */
    public void registerCommandHandler(String command, CommandHandler commandHandler) {
        commandHandlers.put(Objects.requireNonNull(command), Objects.requireNonNull(commandHandler));
    }

    /**
     * Removes the command handler for the given command.
     *
     * @param command The command for which the handler should be removed.
     * @return The removed command handler, if there was one, otherwise {@code null}.
     */
    public CommandHandler removeCommandHandler(String command) {
        return commandHandlers.remove(command);
    }
}

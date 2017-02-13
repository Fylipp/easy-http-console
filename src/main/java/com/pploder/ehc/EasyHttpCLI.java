package com.pploder.ehc;

import edu.rice.cs.util.ArgumentTokenizer;

import java.io.IOException;
import java.util.*;

/**
 * An {@link EasyHttpConsole} with command-line functionality.
 *
 * @author Philipp Ploder
 * @version 2.0.0
 * @since 1.0.0
 */
public class EasyHttpCLI extends EasyHttpConsole implements HttpCLI {

    /**
     * The default unknown command listener.
     */
    public static final CommandListener DEFAULT_UNKNOWN_COMMAND_LISTENER = cmd -> cmd.getSource().getConnection().send("Unknown command: " + cmd.getName());

    private final Map<String, CommandListener> commandListeners = new HashMap<>();

    private CommandListener unknownCommandListener;

    /**
     * Creates a new instance using the default unknown command handler.
     * The unknown command listener is set to {@link #DEFAULT_UNKNOWN_COMMAND_LISTENER}.
     *
     * @throws IOException If such an exception is thrown by the super-constructor.
     */
    public EasyHttpCLI() throws IOException {
        super();
        sharedSetup();
    }

    /**
     * Creates a new instance using the given port.
     * The unknown command listener is set to {@link #DEFAULT_UNKNOWN_COMMAND_LISTENER}.
     *
     * @param port The port to provide the console on.
     * @throws IOException If such an exception is thrown by the super-constructor.
     */
    public EasyHttpCLI(int port) throws IOException {
        super(port);
        sharedSetup();
    }

    /**
     * Creates a new instance using the given host and port.
     * The unknown command listener is set to {@link #DEFAULT_UNKNOWN_COMMAND_LISTENER}.
     *
     * @param host The host to provide the console on.
     * @param port The port to provide the console on.
     * @throws IOException If such an exception is thrown by the super-constructor.
     */
    public EasyHttpCLI(String host, int port) throws IOException {
        super(host, port);
        sharedSetup();
    }

    private void sharedSetup() {
        addMessageListener(this::messageHandler);
        setUnknownCommandListener(DEFAULT_UNKNOWN_COMMAND_LISTENER);
    }

    private void messageHandler(Message message) {
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

        CommandListener commandListener = getCommandHandler(cmd.getName());
        if (commandListener == null) {
            CommandListener fallback = getUnknownCommandListener();
            if (fallback != null) {
                fallback.accept(cmd);
            }
        } else {
            commandListener.accept(cmd);
        }
    }

    @Override
    public void registerCommandListener(String command, CommandListener commandListener) {
        commandListeners.put(Objects.requireNonNull(command), Objects.requireNonNull(commandListener));
    }

    @Override
    public void removeCommandListener(String command) {
        commandListeners.remove(Objects.requireNonNull(command));
    }

    @Override
    public Iterable<String> registeredCommands() {
        return commandListeners.keySet();
    }

    @Override
    public CommandListener getCommandHandler(String command) {
        return commandListeners.get(Objects.requireNonNull(command));
    }

    @Override
    public CommandListener getUnknownCommandListener() {
        return unknownCommandListener;
    }

    @Override
    public void setUnknownCommandListener(CommandListener unknownCommandListener) {
        this.unknownCommandListener = Objects.requireNonNull(unknownCommandListener);
    }
}

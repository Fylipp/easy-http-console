package com.pploder.ehc;

import edu.rice.cs.util.ArgumentTokenizer;
import lombok.extern.slf4j.XSlf4j;

import java.util.*;

/**
 * The default implementation of {@link CommandRegistry}.
 *
 * @author Philipp Ploder
 * @version 2.0.0
 * @since 1.0.0
 */
@XSlf4j
public class EasyCommandRegistry implements CommandRegistry {

    /**
     * The default unknown command listener.
     */
    public static final CommandListener DEFAULT_UNKNOWN_COMMAND_LISTENER = cmd -> cmd.getSource().getConnection().send("Unknown command: " + cmd.getName());

    private final Map<String, CommandListener> commandListeners = new HashMap<>();

    private CommandListener unknownCommandListener;

    /**
     * Creates a new instance using the default unknown command handler.
     * The unknown command listener is set to {@link #DEFAULT_UNKNOWN_COMMAND_LISTENER}.
     */
    public EasyCommandRegistry() {
        this(DEFAULT_UNKNOWN_COMMAND_LISTENER);
    }

    /**
     * Creates a new instance using the given port.
     * The unknown command listener is set to {@link #DEFAULT_UNKNOWN_COMMAND_LISTENER}.
     *
     * @param unknownCommandListener The listener for unknown commands.
     */
    public EasyCommandRegistry(CommandListener unknownCommandListener) {
        setUnknownCommandListener(unknownCommandListener);
    }

    @Override
    public void supplyMessage(Message message) {
        log.debug("Message supplied to command registry: {}", message.getMessage());

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

        String command = split.get(0);

        Command cmd = new EasyCommand(message, command, args);

        CommandListener commandListener = getCommandListener(cmd.getName());
        if (commandListener == null) {
            log.debug("Unknown command '{}', defaulting to unknown command handler (if available)", command);

            CommandListener fallback = getUnknownCommandListener();
            if (fallback != null) {
                try {
                    fallback.accept(cmd);
                } catch (Exception e) {
                    log.catching(e);
                }
            }
        } else {
            try {
                commandListener.accept(cmd);
            } catch (Exception e) {
                log.catching(e);
            }
        }
    }

    @Override
    public void put(String command, CommandListener commandListener) {
        commandListeners.put(Objects.requireNonNull(command), Objects.requireNonNull(commandListener));

        log.debug("New command listener set for '{}'", command);
    }

    @Override
    public void remove(String command) {
        commandListeners.remove(Objects.requireNonNull(command));

        log.debug("Command listener removed for '{}'", command);
    }

    @Override
    public Iterable<String> commands() {
        return commandListeners.keySet();
    }

    @Override
    public CommandListener getCommandListener(String command) {
        return commandListeners.get(Objects.requireNonNull(command));
    }

    @Override
    public CommandListener getCommandListenerOrFallback(String command) {
        CommandListener commandListener = getCommandListener(command);
        return commandListener == null ? getUnknownCommandListener() : commandListener;
    }

    @Override
    public CommandListener getUnknownCommandListener() {
        return unknownCommandListener;
    }

    @Override
    public void setUnknownCommandListener(CommandListener unknownCommandListener) {
        this.unknownCommandListener = Objects.requireNonNull(unknownCommandListener);

        log.debug("Set unknown command listener");
    }
}

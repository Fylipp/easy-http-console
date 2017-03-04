package com.pploder.ehc;

import java.util.Arrays;
import java.util.Objects;

/**
 * The default implementation of the {@link Command} interface.
 * This class achieves no more than what is specified in the interface.
 *
 * @author Philipp Ploder
 * @version 2.0.0
 * @since 1.0.0
 */
public class SimpleCommand implements Command {

    private final Message source;
    private final String command;
    private final String[] args;

    /**
     * Creates a new instance using the given arguments.
     *
     * @param source The original message.
     * @param name   The name of the command.
     * @param args   The arguments of the command.
     */
    public SimpleCommand(Message source, String name, String[] args) {
        this.source = Objects.requireNonNull(source);
        this.command = Objects.requireNonNull(name);
        this.args = Objects.requireNonNull(args);
    }

    @Override
    public Message getSource() {
        return source;
    }

    @Override
    public String getName() {
        return command;
    }

    @Override
    public int getArgsCount() {
        return args.length;
    }

    @Override
    public String getArg(int i) {
        return args[i];
    }

    @Override
    public Iterable<String> args() {
        return Arrays.asList(args);
    }

}

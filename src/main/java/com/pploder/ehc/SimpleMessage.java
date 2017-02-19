package com.pploder.ehc;

import java.util.Objects;

/**
 * The default implementation of the {@link Message} interface.
 *
 * @author Philipp Ploder
 * @version 2.0.0
 * @since 1.0.0
 */
public class SimpleMessage implements Message {

    private final Connection connection;
    private final String message;

    /**
     * Creates a new instance using the given arguments.
     *
     * @param connection The connection from which the message originates.
     * @param message    The message itself.
     */
    public SimpleMessage(Connection connection, String message) {
        this.connection = Objects.requireNonNull(connection);
        this.message = Objects.requireNonNull(message).trim();
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public String getMessage() {
        return message;
    }

}

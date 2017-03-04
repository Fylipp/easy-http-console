package com.pploder.ehc;

import com.pploder.events.Event;
import lombok.extern.slf4j.XSlf4j;

import java.util.Objects;

/**
 * The non-I/O behaviour defined by {@link Console} with I/O outsourced to a {@link NetModule}.
 *
 * @param <N> The type of {@link NetModule} that will be used.
 * @author Philipp Ploder
 * @version 2.0.0
 * @since 2.0.0
 */
@XSlf4j
public class SimpleConsole<N extends NetModule> implements Console {

    private final N io;

    /**
     * Creates a new instance.
     *
     * @param io The I/O operation performer.
     * @throws Exception If the {@link NetModule} throws an exception when {@link NetModule#init(Console)} is called.
     */
    public SimpleConsole(N io) throws Exception {
        this.io = Objects.requireNonNull(io);

        io.init(this);
    }

    /**
     * @return The {@link NetModule} used for the I/O functionality.
     */
    public N getIOModule() {
        return io;
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", getClass().getSimpleName(), getIOModule());
    }

    @Override
    public Event<Message> messageReceivedEvent() {
        return getIOModule().messageReceivedEvent();
    }

    @Override
    public Event<Connection> connectionAddedEvent() {
        return getIOModule().connectionOpenedEvent();
    }

    @Override
    public Event<Connection> connectionRemovedEvent() {
        return getIOModule().connectionClosedEvent();
    }

    @Override
    public void start() throws Exception {
        log.info("Starting {}...", this);

        io.start();
    }

    @Override
    public void close() throws Exception {
        log.info("Closing {}...", this);

        io.close();
    }

    @Override
    public int getConnectionCount() {
        return io.getConnectionCount();
    }

    @Override
    public Iterable<Connection> connections() {
        return io.connections();
    }

}

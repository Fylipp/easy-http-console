package com.pploder.ehc;

import lombok.extern.slf4j.XSlf4j;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

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

    private final List<MessageListener> messageListeners = new CopyOnWriteArrayList<>();

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
    public void addMessageListener(MessageListener messageListener) throws NullPointerException {
        messageListeners.add(Objects.requireNonNull(messageListener));

        log.debug("Message listener added to {}", this);
    }

    @Override
    public void removeMessageListener(MessageListener messageListener) throws NullPointerException {
        messageListeners.remove(Objects.requireNonNull(messageListener));

        log.debug("Message listener removed from {}", this);
    }

    @Override
    public void supplyMessage(Message message) {
        log.debug("Message supplied to {}: {}", this, message.getMessage());

        for (MessageListener messageListener : messageListeners) {
            try {
                messageListener.accept(message);
            } catch (Exception e) {
                log.catching(e);
            }
        }
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

package com.pploder.ehc;

import com.pploder.events.Event;
import com.pploder.events.SimpleEvent;
import lombok.extern.slf4j.XSlf4j;
import org.webbitserver.BaseWebSocketHandler;
import org.webbitserver.WebSocketConnection;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Hosts the websocket for console communication.
 *
 * @author Philipp Ploder
 * @version 2.0.0
 * @since 1.0.0
 */
@XSlf4j
class ConsoleWebSocketHandler extends BaseWebSocketHandler {

    private final Event<Message> messageReceivedEvent = new SimpleEvent<>();
    private final Event<Connection> connectionOpenedEvent = new SimpleEvent<>();
    private final Event<Connection> connectionClosedEvent = new SimpleEvent<>();

    private final Console console;

    private final Map<WebSocketConnection, Connection> connections = new ConcurrentHashMap<>();

    public ConsoleWebSocketHandler(Console console) {
        this.console = console;
    }

    @Override
    public void onOpen(WebSocketConnection connection) throws Exception {
        log.debug("Websocket connection opened ({})", connection.httpRequest().remoteAddress());

        Connection c = connections.put(connection, new WebSocketConnectionWrapper(console, connection));
        connectionOpenedEvent().trigger(c);
    }

    @Override
    public void onClose(WebSocketConnection connection) throws Exception {
        log.debug("Websocket connection closed ({})", connection.httpRequest().remoteAddress());

        Connection c = connections.remove(connection);
        connectionClosedEvent().trigger(c);
    }

    @Override
    public void onMessage(WebSocketConnection connection, String msg) throws Throwable {
        log.debug("Websocket message received ({}): {}", connection.httpRequest().remoteAddress(), msg);

        messageReceivedEvent().trigger(new SimpleMessage(connections.get(connection), msg));
    }

    /**
     * @return The console for which the websocket is hosted.
     */
    public Console getConsole() {
        return console;
    }

    /***
     * @return The event for received messages.
     */
    public Event<Message> messageReceivedEvent() {
        return messageReceivedEvent;
    }

    /**
     * This event is triggered after a new connection has been opened and registered.
     *
     * @return The event for opened connections.
     */
    public Event<Connection> connectionOpenedEvent() {
        return connectionOpenedEvent;
    }

    /**
     * This event is triggered after a connection has been closed and unregistered.
     * Any I/O operations on the connection will produce undefined behaviour.
     *
     * @return The event for closed connections.
     */
    public Event<Connection> connectionClosedEvent() {
        return connectionClosedEvent;
    }

    /**
     * @return The amount of active connection.
     */
    public int getConnectionCount() {
        return connections.size();
    }

    /**
     * Returns a read-only iterable of all active connections.
     *
     * @return All active connections.
     */
    public Iterable<Connection> connections() {
        return connections.values();
    }

}

package com.pploder.ehc;

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

    private final Console server;

    private final Map<WebSocketConnection, Connection> connections = new ConcurrentHashMap<>();

    public ConsoleWebSocketHandler(Console server) {
        this.server = server;
    }

    @Override
    public void onOpen(WebSocketConnection connection) throws Exception {
        log.debug("Websocket connection opened ({})", connection.httpRequest().remoteAddress());

        connections.put(connection, new WebSocketConnectionWrapper(server, connection));
    }

    @Override
    public void onClose(WebSocketConnection connection) throws Exception {
        log.debug("Websocket connection closed ({})", connection.httpRequest().remoteAddress());

        connections.remove(connection);
    }

    @Override
    public void onMessage(WebSocketConnection connection, String msg) throws Throwable {
        log.debug("Websocket message received ({}): {}", connection.httpRequest().remoteAddress(), msg);

        server.supplyMessage(new SimpleMessage(connections.get(connection), msg));
    }

    /**
     * @return The console for which the websocket is hosted.
     */
    public Console getHttpConsole() {
        return server;
    }

    /**
     * @return The amount of active connection.
     */
    public int getConnectionCount() {
        return connections.size();
    }

    /**
     * @return All active connections.
     */
    public Iterable<Connection> connections() {
        return connections.values();
    }

}

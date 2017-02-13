package com.pploder.ehc;

import org.webbitserver.BaseWebSocketHandler;
import org.webbitserver.WebSocketConnection;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Hosts the websocket for console communication.
 *
 * @author Philipp Ploder
 * @version 2.0.0
 * @since 1.0.0
 */
class ConsoleWebSocketHandler extends BaseWebSocketHandler {

    private final HttpConsole server;

    private final List<WebSocketConnection> connections = new CopyOnWriteArrayList<>();

    public ConsoleWebSocketHandler(HttpConsole server) {
        this.server = server;
    }

    @Override
    public void onOpen(WebSocketConnection connection) throws Exception {
        connections.add(connection);
    }

    @Override
    public void onClose(WebSocketConnection connection) throws Exception {
        connections.remove(connection);
    }

    @Override
    public void onMessage(WebSocketConnection connection, String msg) throws Throwable {
        server.supplyMessage(new EasyMessage(getHttpConsole(), connection, msg));
    }

    /**
     * @return The console for which the websocket is hosted.
     */
    public HttpConsole getHttpConsole() {
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
    public Iterable<WebSocketConnection> getConnections() {
        return connections;
    }
}

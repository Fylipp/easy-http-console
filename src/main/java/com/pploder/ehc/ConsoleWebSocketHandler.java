package com.pploder.ehc;

import org.webbitserver.BaseWebSocketHandler;
import org.webbitserver.WebSocketConnection;

import java.util.ArrayList;
import java.util.List;

/**
 * Hosts the websocket for console communication.
 *
 * @author Philipp Ploder
 * @version 1.0.0
 * @since 1.0.0
 */
class ConsoleWebSocketHandler extends BaseWebSocketHandler {

    public static final MessageHandler DEFAULT_MESSAGE_HANDLER = message -> {
        message.getConnection().send("[AbstractHttpConsole] No message handler has been set.");
        message.getConnection().close();
    };

    private final HttpConsole server;

    private final List<WebSocketConnection> connections = new ArrayList<>();

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
        MessageHandler messageHandler = getHttpConsole().getMessageHandler();
        Message message = new EasyMessage(getHttpConsole(), connection, msg);

        if (messageHandler == null) {
            DEFAULT_MESSAGE_HANDLER.accept(message);
        } else {
            messageHandler.accept(message);
        }
    }

    /**
     * @return The console for which the websocket is hosted.
     */
    public HttpConsole getHttpConsole() {
        return server;
    }

    /**
     * @return All active connections.
     */
    public Iterable<WebSocketConnection> getConnections() {
        return connections;
    }
}

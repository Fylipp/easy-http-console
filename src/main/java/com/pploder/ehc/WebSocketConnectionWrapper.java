package com.pploder.ehc;

import lombok.extern.slf4j.XSlf4j;
import org.webbitserver.WebSocketConnection;

import java.util.Objects;

/**
 * Wraps a {@link WebSocketConnection} to provide abstracted usage.
 * This abstracts the inner workings of websockets.
 *
 * @author Philipp Ploder
 * @version 2.0.0
 * @since 2.0.0
 */
@XSlf4j
class WebSocketConnectionWrapper implements Connection {

    private final Console console;
    private final WebSocketConnection webSocketConnection;

    private final String remoteAddress;

    /**
     * Creates a new instance with the given console and websocket connection.
     *
     * @param console             The console that this message originated from.
     * @param webSocketConnection The websocket connection to wrap around.
     */
    public WebSocketConnectionWrapper(Console console, WebSocketConnection webSocketConnection) {
        this.console = Objects.requireNonNull(console);
        this.webSocketConnection = Objects.requireNonNull(webSocketConnection);

        remoteAddress = webSocketConnection.httpRequest().remoteAddress().toString();
    }

    @Override
    public Console getConsole() {
        return console;
    }

    @Override
    public String getRemoteAddress() {
        return remoteAddress;
    }

    @Override
    public void send(MessageContent messageContent) {
        String jsonString = messageContent.asJSON().toJSONString();

        log.debug("Sending message to '{}': {}", getRemoteAddress(), jsonString);

        webSocketConnection.send(jsonString);
    }

    @Override
    public void close() throws Exception {
        log.debug("Closing connection to {}...", getRemoteAddress());
        webSocketConnection.close();
    }

    @Override
    public String toString() {
        return String.format("WebSocketConnectionWrapper{remoteAddress='%s'}", remoteAddress);
    }

}

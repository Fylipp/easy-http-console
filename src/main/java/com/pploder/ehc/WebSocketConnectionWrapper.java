package com.pploder.ehc;

import lombok.extern.slf4j.XSlf4j;
import org.webbitserver.WebSocketConnection;

import java.util.Objects;

@XSlf4j
class WebSocketConnectionWrapper implements Connection {

    private final HttpConsole httpConsole;
    private final WebSocketConnection webSocketConnection;

    private final String remoteAddress;

    public WebSocketConnectionWrapper(HttpConsole httpConsole, WebSocketConnection webSocketConnection) {
        this.httpConsole = Objects.requireNonNull(httpConsole);
        this.webSocketConnection = Objects.requireNonNull(webSocketConnection);

        remoteAddress = webSocketConnection.httpRequest().remoteAddress().toString();
        ;
    }

    @Override
    public HttpConsole getHttpConsole() {
        return httpConsole;
    }

    @Override
    public String getRemoteAddress() {
        return remoteAddress;
    }

    @Override
    public void send(MessageContent messageContent) {
        String jsonString = messageContent.asJSON().toJSONString();

        log.debug("Sending message to <{}>: {}", getRemoteAddress(), jsonString);

        webSocketConnection.send(jsonString);
    }

    @Override
    public void close() throws Exception {
        log.debug("Closing connection to {}...", getRemoteAddress());
        webSocketConnection.close();
    }
}

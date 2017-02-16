package com.pploder.ehc;

import org.webbitserver.WebSocketConnection;

import java.util.Objects;

public class WebSocketConnectionWrapper implements Connection {

    private final HttpConsole httpConsole;
    private final WebSocketConnection webSocketConnection;

    public WebSocketConnectionWrapper(HttpConsole httpConsole, WebSocketConnection webSocketConnection) {
        this.httpConsole = Objects.requireNonNull(httpConsole);
        this.webSocketConnection = Objects.requireNonNull(webSocketConnection);
    }

    @Override
    public HttpConsole getHttpConsole() {
        return httpConsole;
    }

    @Override
    public String getRemoteAddress() {
        return webSocketConnection.httpRequest().remoteAddress().toString();
    }

    @Override
    public void send(MessageContent messageContent) {
        webSocketConnection.send(messageContent.asJSON().toJSONString());
    }

    @Override
    public void close() throws Exception {
        webSocketConnection.close();
    }
}

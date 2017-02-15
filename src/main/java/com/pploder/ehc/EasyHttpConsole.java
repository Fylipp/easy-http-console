package com.pploder.ehc;

import org.webbitserver.WebServer;
import org.webbitserver.WebServers;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The default implementation of {@link HttpConsole}.
 *
 * @author Philipp Ploder
 * @version 2.0.0
 * @since 2.0.0
 */
public class EasyHttpConsole implements HttpConsole {

    /**
     * The URI of the websocket.
     */
    public static final String WEBSOCKET_URI = "/ws";

    /**
     * The resource path of the console page.
     */
    public static final String HTML_PATH = "/com/pploder/ehc/interface.html";

    /**
     * The default port that is used when none is specified.
     */
    public static final int DEFAULT_PORT = 8080;

    /**
     * The default host that is used when none is specified.
     */
    public static final String DEFAULT_HOST = "localhost";

    private final String host;
    private final int port;

    private final WebServer server;
    private final ConsoleWebSocketHandler webSocketHandler;

    private final List<MessageListener> messageListeners = new CopyOnWriteArrayList<>();

    /**
     * Creates a new instance using the default host and port.
     *
     * @throws IOException If something goes wrong whilst creating the web server.
     */
    public EasyHttpConsole() throws IOException {
        this(DEFAULT_PORT);
    }

    /**
     * Creates a new instance using the given port and default host.
     *
     * @param port The port to run the server on.
     * @throws IOException If something goes wrong whilst creating the web server.
     */
    public EasyHttpConsole(int port) throws IOException {
        this(DEFAULT_HOST, port);
    }

    /**
     * Creates a new instance using the given host and port.
     *
     * @param host The host to run the server on.
     * @param port The port to run the server on.
     * @throws IOException If something goes wrong whilst creating the web server.
     */
    public EasyHttpConsole(String host, int port) throws IOException {
        this.host = host;
        this.port = port;

        webSocketHandler = new ConsoleWebSocketHandler(this);

        server = WebServers.createWebServer(port)
                .add(WEBSOCKET_URI, webSocketHandler)
                .add(new ConsoleHttpHandler(this, HTML_PATH));
    }

    @Override
    public void start() throws Exception {
        server.start().get();
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public String getWebsocketURL() {
        return "ws://" + getHost() + ":" + getPort() + "/ws";
    }

    @Override
    public String getHttpURL() {
        return "http://" + getHost() + ":" + getPort() + "/";
    }

    @Override
    public void addMessageListener(MessageListener messageListener) throws NullPointerException {
        messageListeners.add(Objects.requireNonNull(messageListener));
    }

    @Override
    public void removeMessageListener(MessageListener messageListener) throws NullPointerException {
        messageListeners.remove(Objects.requireNonNull(messageListener));
    }

    @Override
    public void supplyMessage(Message message) {
        for (MessageListener messageListener : messageListeners) {
            try {
                messageListener.accept(message);
            } catch (Exception e) {
                // Suppress
            }
        }
    }

    @Override
    public void close() throws IOException {
        server.stop();
    }

    @Override
    public int getConnectionCount() {
        return webSocketHandler.getConnectionCount();
    }

    @Override
    public Iterable<Connection> getConnections() {
        return webSocketHandler.getConnections();
    }
}

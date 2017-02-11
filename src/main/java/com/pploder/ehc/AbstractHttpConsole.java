package com.pploder.ehc;

import org.webbitserver.WebServer;
import org.webbitserver.WebServers;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * A console that is accessible via HTTP and communicates via a websocket.
 *
 * @author Philipp Ploder
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class AbstractHttpConsole implements HttpConsole {

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

    /**
     * The default message handler. It simply sends a message stating that this is the default behaviour.
     */
    public static final MessageHandler DEFAULT_MESSAGE_HANDLER = msg -> msg.getConnection().send("[easy-http-console] This is the default message handler");

    private final String host;
    private final int port;

    private final WebServer server;
    private final ConsoleWebSocketHandler webSocketHandler;

    private MessageHandler messageHandler;

    /**
     * Creates a new instance using the default host and port.
     *
     * @throws IOException If something goes wrong whilst creating the web server.
     */
    public AbstractHttpConsole() throws IOException {
        this(DEFAULT_PORT);
    }

    /**
     * Creates a new instance using the given port and default host.
     *
     * @param port The port to run the server on.
     * @throws IOException If something goes wrong whilst creating the web server.
     */
    public AbstractHttpConsole(int port) throws IOException {
        this(DEFAULT_HOST, port);
    }

    /**
     * Creates a new instance using the given host and port.
     *
     * @param host The host to run the server on.
     * @param port The port to run the server on.
     * @throws IOException If something goes wrong whilst creating the web server.
     */
    public AbstractHttpConsole(String host, int port) throws IOException {
        this.host = host;
        this.port = port;

        webSocketHandler = new ConsoleWebSocketHandler(this);

        server = WebServers.createWebServer(port)
                .add(WEBSOCKET_URI, webSocketHandler)
                .add(new ConsoleHttpHandler(this, HTML_PATH));
    }

    @Override
    public void start() throws ExecutionException, InterruptedException {
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
    public void close() throws IOException {
        server.stop();
    }
}

package com.pploder.ehc;

import lombok.extern.slf4j.XSlf4j;
import org.webbitserver.WebServer;
import org.webbitserver.WebServers;

import java.io.IOException;

/**
 * The default implementation of {@link HttpConsole}.
 *
 * @author Philipp Ploder
 * @version 2.0.0
 * @since 2.0.0
 */
@XSlf4j
public class EasyHttpConsole extends AbstractHttpConsole {

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

    private final WebServer server;
    private final ConsoleWebSocketHandler webSocketHandler;

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
        super(port, host, "http://" + host + ":" + port + "/",
                "ws://" + host + ":" + port + "/ws");

        webSocketHandler = new ConsoleWebSocketHandler(this);

        server = WebServers.createWebServer(port)
                .add(WEBSOCKET_URI, webSocketHandler)
                .add(new ConsoleHttpHandler(this, HTML_PATH));

        log.info("Webserver created on {}:{}", host, port);
    }

    @Override
    public void start() throws Exception {
        log.info("Server will be started (blocking)...");

        server.start().get();

        log.info("Server successfully started.");
    }

    @Override
    public void close() throws IOException {
        log.info("Server will be stopped (non-blocking)...");

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

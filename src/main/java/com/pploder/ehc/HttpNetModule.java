package com.pploder.ehc;

import lombok.extern.slf4j.XSlf4j;
import org.webbitserver.WebServer;
import org.webbitserver.WebServers;

import java.io.IOException;
import java.util.Objects;

/**
 * This class provides network I/O functionality via HTTP and websockets.
 *
 * @author Philipp Ploder
 * @version 2.0.0
 * @since 2.0.0
 */
@XSlf4j
public class HttpNetModule implements NetModule {

    /**
     * The default URI of the websocket.
     * *Not* to be confused with the websocket URL which features a protocol, host, port and URI.
     */
    public static final String DEFAULT_WEBSOCKET_URI = "/ws";

    /**
     * The default resource path of the console page.
     */
    public static final String DEFAULT_HTML_PATH = "/com/pploder/ehc/interface.html";

    /**
     * The default port that is used when none is specified.
     */
    public static final int DEFAULT_PORT = 8080;

    /**
     * The default host that is used when none is specified.
     */
    public static final String DEFAULT_HOST = "localhost";

    private Console console;

    private final String host;
    private final int port;
    private final String httpURL;
    private final String websocketURL;

    private WebServer server;
    private ConsoleWebSocketHandler webSocketHandler;

    /**
     * Creates a new instance with the default host on the given port.
     * The HTTP and websocket URLs will get standard values.
     *
     * @throws IOException If something goes wrong whilst creating the web server.
     */
    public HttpNetModule() throws IOException {
        this(DEFAULT_PORT);
    }

    /**
     * Creates a new instance with the default host on the given port.
     * The HTTP and websocket URLs will get standard values.
     *
     * @param port The port to run the server on.
     * @throws IOException If something goes wrong whilst creating the web server.
     */
    public HttpNetModule(int port) throws IOException {
        this(DEFAULT_HOST, port);
    }

    /**
     * Creates a new instance with the given host on the given port.
     * The HTTP and websocket URLs will get standard values.
     *
     * @param host The host to run the server on.
     * @param port The port to run the server on.
     * @throws IOException If something goes wrong whilst creating the web server.
     */
    public HttpNetModule(String host, int port) throws IOException {
        this(host, port, "http://" + host + ":" + port + "/", "ws://" + host + ":" + port + "/ws");
    }

    /**
     * Creates a new instance with the given host on the given port.
     *
     * @param host The host to run the server on.
     * @param port The port to run the server on.
     * @throws IOException If something goes wrong whilst creating the web server.
     */
    public HttpNetModule(String host, int port, String httpURL, String websocketURL) throws IOException {
        this.host = Objects.requireNonNull(host);
        this.port = port;
        this.httpURL = Objects.requireNonNull(httpURL);
        this.websocketURL = Objects.requireNonNull(websocketURL);
    }

    /**
     * @return The console that this module belongs to.
     */
    public Console getConsole() {
        return console;
    }

    @Override
    public String toString() {
        return String.format("%s@%s:%d", getClass().getSimpleName(), getHost(), getPort());
    }

    @Override
    public void init(Console console) throws Exception {
        this.console = Objects.requireNonNull(console);

        webSocketHandler = new ConsoleWebSocketHandler(console);

        server = WebServers.createWebServer(port)
                .add(DEFAULT_WEBSOCKET_URI, webSocketHandler)
                .add(new ConsoleHttpHandler(this, DEFAULT_HTML_PATH));

        log.info("Webserver created on {}:{}", host, port);
    }

    @Override
    public void start() throws Exception {
        log.info("{} will be started (blocking)...", this);

        server.start().get();

        log.info("{} successfully started.", this);
    }

    @Override
    public void close() throws IOException {
        log.info("{} will be stopped (non-blocking)...", this);

        server.stop();
    }

    @Override
    public int getConnectionCount() {
        return webSocketHandler.getConnectionCount();
    }

    @Override
    public Iterable<Connection> connections() {
        return webSocketHandler.connections();
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
    public String getHttpURL() {
        return httpURL;
    }

    @Override
    public String getWebsocketURL() {
        return websocketURL;
    }

}

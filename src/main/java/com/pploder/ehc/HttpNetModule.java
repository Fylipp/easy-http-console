package com.pploder.ehc;

import com.pploder.events.Event;
import lombok.extern.slf4j.XSlf4j;
import org.webbitserver.WebServer;
import org.webbitserver.WebServers;

import java.io.IOException;
import java.util.Objects;

/**
 * This class provides network I/O functionality via HTTP and websockets.
 * <p>
 * Instances expose a single interface site (at {@code /}), a websocket (configurable)
 * and an arbitrary amount of general-purpose resources (such as icons).
 * <p>
 * The {@link #init(Console)} method must be called before {@link #start()}. Additionally the module should
 * be closed when it is longer being used. The behaviour that occurs when the state-controlling methods
 * are not invoked in order is undefined. After invoking {@link #close()} an instance may be recycled by
 * invoking {@link #init(Console)} and {@link #start()} again.
 *
 * @author Philipp Ploder
 * @version 2.0.0
 * @since 2.0.0
 */
@XSlf4j
public class HttpNetModule implements NetModule {

    /**
     * The default URI of the websocket.
     * <b>Not</b> to be confused with the websocket URL which features a protocol, host, port and URI.
     */
    public static final String DEFAULT_WEBSOCKET_URI = "/ws";

    /**
     * The default port that is used when none is specified.
     */
    public static final int DEFAULT_PORT = 8080;

    /**
     * The default host that is used when none is specified.
     * Usually {@code localhost} is a loopback address that directs traffic to the local machine
     * without actually using any networking capacities.
     */
    public static final String DEFAULT_HOST = "localhost";

    private Console console;

    private final String host;
    private final int port;
    private final String websocketURI;
    private final String httpURL;
    private final String websocketURL;
    private final SiteHttpSupplier httpSupplier;

    private WebServer server;
    private ConsoleWebSocketHandler webSocketHandler;

    /**
     * Creates a new instance with the default host on the default port and the default websocket URI.
     * The default host in {@link #DEFAULT_HOST}, the default port is {@link #DEFAULT_PORT}
     * and the default websocket URI is {@link #DEFAULT_WEBSOCKET_URI}.
     * The site supplier from {@link SiteHttpSupplier#defaultSite()} will be used.
     *
     * @throws IOException If something goes wrong whilst creating the web server.
     */
    public HttpNetModule() throws IOException {
        this(SiteHttpSupplier.defaultSite());
    }

    /**
     * Creates a new instance with the default host on the given port and the default websocket URI.
     * The default port is {@link #DEFAULT_PORT} and the default websocket URI is {@link #DEFAULT_WEBSOCKET_URI}.
     * The site supplier from {@link SiteHttpSupplier#defaultSite()} will be used.
     *
     * @param port The port to run the server on.
     * @throws IOException If something goes wrong whilst creating the web server.
     */
    public HttpNetModule(int port) throws IOException {
        this(SiteHttpSupplier.defaultSite(), port);
    }

    /**
     * Creates a new instance with the given host on the given port and the default websocket URI.
     * The default websocket URI is {@link #DEFAULT_WEBSOCKET_URI}.
     * The site supplier from {@link SiteHttpSupplier#defaultSite()} will be used.
     *
     * @param host The host to run the server on.
     * @param port The port to run the server on.
     * @throws IOException If something goes wrong whilst creating the web server.
     */
    public HttpNetModule(String host, int port) throws IOException {
        this(SiteHttpSupplier.defaultSite(), host, port);
    }

    /**
     * Creates a new instance with the given host on the given port and the given websocket URI.
     * The site supplier from {@link SiteHttpSupplier#defaultSite()} will be used.
     *
     * @param host         The host to run the server on.
     * @param port         The port to run the server on.
     * @param websocketURI The URI that the websocket will be served over. The leading {@code /} is optional.
     * @throws IOException If something goes wrong whilst creating the web server.
     */
    public HttpNetModule(String host, int port, String websocketURI) throws IOException {
        this(SiteHttpSupplier.defaultSite(), host, port, websocketURI);
    }

    /**
     * Creates a new instance with the default host on the default port and with the default websocket URI.
     * The default host in {@link #DEFAULT_HOST}, the default port is {@link #DEFAULT_PORT}
     * and the default websocket URI is {@link #DEFAULT_WEBSOCKET_URI}.
     *
     * @param httpSupplier The supplier of HTTP responses.
     * @throws IOException If something goes wrong whilst creating the web server.
     */
    public HttpNetModule(SiteHttpSupplier httpSupplier) throws IOException {
        this(httpSupplier, DEFAULT_PORT);
    }

    /**
     * Creates a new instance with the default host on the given port and with the default websocket URI.
     * The default host in {@link #DEFAULT_HOST} and the default websocket URI is {@link #DEFAULT_WEBSOCKET_URI}.
     *
     * @param httpSupplier The supplier of HTTP responses.
     * @param port         The port to run the server on.
     * @throws IOException If something goes wrong whilst creating the web server.
     */
    public HttpNetModule(SiteHttpSupplier httpSupplier, int port) throws IOException {
        this(httpSupplier, DEFAULT_HOST, port);
    }

    /**
     * Creates a new instance with the given host on the given port with the default websocket URI.
     * The default websocket URI is {@link #DEFAULT_WEBSOCKET_URI}.
     *
     * @param httpSupplier The supplier of HTTP responses.
     * @param host         The host to run the server on.
     * @param port         The port to run the server on.
     * @throws IOException If something goes wrong whilst creating the web server.
     */
    public HttpNetModule(SiteHttpSupplier httpSupplier, String host, int port) throws IOException {
        this(httpSupplier, host, port, DEFAULT_WEBSOCKET_URI);
    }

    /**
     * Creates a new instance with the given host on the given port and with the given websocket URI.
     *
     * @param httpSupplier The supplier of HTTP responses.
     * @param host         The host to run the server on.
     * @param port         The port to run the server on.
     * @param websocketURI The URI that the websocket will be served over. The leading {@code /} is optional.
     * @throws IOException If something goes wrong whilst creating the web server.
     */
    public HttpNetModule(SiteHttpSupplier httpSupplier, String host, int port, String websocketURI)
            throws IOException {
        this.httpSupplier = Objects.requireNonNull(httpSupplier);
        this.host = Objects.requireNonNull(host);
        this.port = port;
        this.websocketURI = websocketURI.startsWith("/") ? websocketURI : "/" + websocketURI;
        this.httpURL = String.format("http://%s:%d/", host, port);
        this.websocketURL = String.format("ws://%s:%d%s", host, port, this.websocketURI);
    }

    /**
     * @return The console that this module belongs to.
     */
    public Console getConsole() {
        return console;
    }

    /**
     * @return The module's supplier of the interface.
     */
    public SiteHttpSupplier getHttpSupplier() {
        return httpSupplier;
    }

    @Override
    public String toString() {
        return String.format("%s@%s:%d", getClass().getSimpleName(), getHost(), getPort());
    }

    @Override
    public void init(Console console) throws Exception {
        this.console = Objects.requireNonNull(console);

        webSocketHandler = new ConsoleWebSocketHandler(console);

        ConsoleHttpHandler httpHandler = new ConsoleHttpHandler(this, httpSupplier, DefaultResource.values());

        server = WebServers.createWebServer(port)
                .add(websocketURI, webSocketHandler)
                .add(httpHandler);

        log.info("Webserver created on {}:{}{}", host, port, websocketURI);
    }

    @Override
    public Event<Message> messageReceivedEvent() {
        return webSocketHandler.messageReceivedEvent();
    }

    @Override
    public Event<Connection> connectionOpenedEvent() {
        return webSocketHandler.connectionOpenedEvent();
    }

    @Override
    public Event<Connection> connectionClosedEvent() {
        return webSocketHandler.connectionClosedEvent();
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

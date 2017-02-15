package com.pploder.ehc;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The non-I/O behaviour defined by {@link HttpConsole}.
 *
 * @author Philipp Ploder
 * @version 2.0.0
 * @since 2.0.0
 */
public abstract class AbstractHttpConsole implements HttpConsole {

    private final String host;
    private final int port;

    private final String httpURL, websocketURL;

    private final List<MessageListener> messageListeners = new CopyOnWriteArrayList<>();

    /**
     * Creates a new instance.
     *
     * @param port         The port.
     * @param host         The host.
     * @param httpURL      The URL of the HTTP resource.
     * @param websocketURL The URL of the websocket resource.
     */
    public AbstractHttpConsole(int port, String host, String httpURL, String websocketURL) {
        this.port = port;
        this.host = host;
        this.httpURL = httpURL;
        this.websocketURL = websocketURL;
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

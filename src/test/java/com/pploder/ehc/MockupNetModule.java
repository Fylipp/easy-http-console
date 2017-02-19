package com.pploder.ehc;

public class MockupNetModule implements NetModule {

    private boolean initCalled, startCalled, closeCalled, getConnectionCountCalled, getConnectionsCalled;

    private int connectionsCount;
    private Iterable<Connection> connections;
    private String host;
    private int port;

    public boolean wasInitCalled() {
        return initCalled;
    }

    public boolean wasStartCalled() {
        return startCalled;
    }

    public boolean wasCloseCalled() {
        return closeCalled;
    }

    public boolean wasGetConnectionCountCalled() {
        return getConnectionCountCalled;
    }

    public boolean wasGetConnectionsCalled() {
        return getConnectionsCalled;
    }

    public void setConnectionsCount(int connectionsCount) {
        this.connectionsCount = connectionsCount;
    }

    public void setConnections(Iterable<Connection> connections) {
        this.connections = connections;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "MockupNetModule";
    }

    @Override
    public void init(Console console) throws Exception {
        initCalled = true;
    }

    @Override
    public void start() throws Exception {
        startCalled = true;
    }

    @Override
    public void close() throws Exception {
        closeCalled = true;
    }

    @Override
    public int getConnectionCount() {
        getConnectionCountCalled = true;

        return connectionsCount;
    }

    @Override
    public Iterable<Connection> connections() {
        getConnectionsCalled = true;

        return connections;
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
        return null;
    }

    @Override
    public String getWebsocketURL() {
        return null;
    }

}

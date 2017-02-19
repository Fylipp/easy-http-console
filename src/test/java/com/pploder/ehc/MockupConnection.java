package com.pploder.ehc;

import java.util.function.Consumer;

public class MockupConnection implements Connection {

    private final Console console;
    private final String remoteAddress;
    private final Consumer<MessageContent> sendListener;

    private boolean sendCalled, closeCalled;

    public MockupConnection(Console console, String remoteAddress, Consumer<MessageContent> sendListener) {
        this.console = console;
        this.remoteAddress = remoteAddress;
        this.sendListener = sendListener;
    }

    public boolean wasSendCalled() {
        return sendCalled;
    }

    public boolean wasCloseCalled() {
        return closeCalled;
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
        sendCalled = true;

        if (sendListener != null) {
            sendListener.accept(messageContent);
        }
    }

    @Override
    public void close() throws Exception {
        closeCalled = true;
    }

}

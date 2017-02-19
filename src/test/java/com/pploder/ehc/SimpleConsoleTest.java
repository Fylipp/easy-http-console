package com.pploder.ehc;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class SimpleConsoleTest {

    @Test
    public void testConstructor() throws Exception {
        MockupNetModule mockupNetModule = new MockupNetModule();

        SimpleConsole simpleConsole = new SimpleConsole(mockupNetModule);

        Assert.assertEquals(mockupNetModule, simpleConsole.getIOModule());
        Assert.assertTrue(mockupNetModule.wasInitCalled());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNull() throws Exception {
        new SimpleConsole(null);
    }

    @Test
    public void testToString() throws Exception {
        MockupNetModule mockupNetModule = new MockupNetModule();
        SimpleConsole simpleConsole = new SimpleConsole(mockupNetModule);

        Assert.assertEquals(String.format("SimpleConsole(%s)", mockupNetModule), simpleConsole.toString());
    }

    @Test
    public void testStart() throws Exception {
        MockupNetModule mockupNetModule = new MockupNetModule();
        SimpleConsole simpleConsole = new SimpleConsole(mockupNetModule);

        simpleConsole.start();

        Assert.assertTrue(mockupNetModule.wasStartCalled());
    }

    @Test
    public void testClose() throws Exception {
        MockupNetModule mockupNetModule = new MockupNetModule();
        SimpleConsole simpleConsole = new SimpleConsole(mockupNetModule);

        simpleConsole.close();

        Assert.assertTrue(mockupNetModule.wasCloseCalled());
    }

    @Test
    public void testMessageListenerExecution() throws Exception {
        AtomicBoolean listenerCalled = new AtomicBoolean();
        MessageListener messageListener = msg -> listenerCalled.set(true);

        MockupNetModule mockupNetModule = new MockupNetModule();
        SimpleConsole simpleConsole = new SimpleConsole(mockupNetModule);
        Connection connection = new MockupConnection(simpleConsole, "RemoteAddress", null);

        simpleConsole.addMessageListener(messageListener);
        simpleConsole.supplyMessage(new SimpleMessage(connection, "Test"));

        Assert.assertTrue(listenerCalled.get());
    }

    @Test
    public void testMultipleMessageListenerExecution() throws Exception {
        AtomicBoolean listenerCalled1 = new AtomicBoolean();
        AtomicBoolean listenerCalled2 = new AtomicBoolean();
        AtomicBoolean listenerCalled3 = new AtomicBoolean();
        MessageListener messageListener1 = msg -> listenerCalled1.set(true);
        MessageListener messageListener2 = msg -> listenerCalled2.set(true);
        MessageListener messageListener3 = msg -> listenerCalled3.set(true);

        MockupNetModule mockupNetModule = new MockupNetModule();
        SimpleConsole simpleConsole = new SimpleConsole(mockupNetModule);
        Connection connection = new MockupConnection(simpleConsole, "RemoteAddress", null);

        simpleConsole.addMessageListener(messageListener1);
        simpleConsole.addMessageListener(messageListener2);
        simpleConsole.addMessageListener(messageListener3);
        simpleConsole.supplyMessage(new SimpleMessage(connection, "Test"));

        Assert.assertTrue(listenerCalled1.get());
        Assert.assertTrue(listenerCalled2.get());
        Assert.assertTrue(listenerCalled3.get());
    }

    @Test
    public void testMessageListenerRemoval() throws Exception {
        AtomicBoolean listenerCalled = new AtomicBoolean();
        MessageListener messageListener = msg -> listenerCalled.set(true);

        MockupNetModule mockupNetModule = new MockupNetModule();
        SimpleConsole simpleConsole = new SimpleConsole(mockupNetModule);
        Connection connection = new MockupConnection(simpleConsole, "RemoteAddress", null);

        simpleConsole.addMessageListener(messageListener);
        simpleConsole.removeMessageListener(messageListener);
        simpleConsole.supplyMessage(new SimpleMessage(connection, "Test"));

        Assert.assertFalse(listenerCalled.get());
    }

    @Test
    public void testGetConnectionsCount() throws Exception {
        MockupNetModule mockupNetModule = new MockupNetModule();
        SimpleConsole simpleConsole = new SimpleConsole(mockupNetModule);

        mockupNetModule.setConnectionsCount(101);

        Assert.assertEquals(101, simpleConsole.getConnectionCount());
        Assert.assertTrue(mockupNetModule.wasGetConnectionCountCalled());
    }

    @Test
    public void testGetConnections() throws Exception {
        Set<Connection> connections = new HashSet<>(3);
        MockupNetModule mockupNetModule = new MockupNetModule();
        SimpleConsole<?> simpleConsole = new SimpleConsole<>(mockupNetModule);

        connections.add(new MockupConnection(simpleConsole, "RemoteAddress1", null));
        connections.add(new MockupConnection(simpleConsole, "RemoteAddress2", null));
        connections.add(new MockupConnection(simpleConsole, "RemoteAddress3", null));

        mockupNetModule.setConnections(connections);

        Set<Connection> collector = new HashSet<>();
        simpleConsole.connections().forEach(collector::add);
        Assert.assertEquals(connections, collector);

        Assert.assertTrue(mockupNetModule.wasGetConnectionsCalled());
    }

}

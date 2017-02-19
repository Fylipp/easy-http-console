package com.pploder.ehc;

import junit.framework.Assert;
import org.junit.Test;

public class SimpleMessageTest {

    @Test
    public void testConstructor() throws Exception {
        Console console = new SimpleConsole<>(new MockupNetModule());
        Connection connection = new MockupConnection(console, "RemoteAddress");

        SimpleMessage simpleMessage = new SimpleMessage(connection, "\t\n\r Message \t\r\n");

        Assert.assertEquals(connection, simpleMessage.getConnection());
        Assert.assertEquals("Message", simpleMessage.getMessage());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorConnectionNull() {
        new SimpleMessage(null, "Message");
    }

    @Test(expected = NullPointerException.class)
    public void testConstuctorMessageNull() throws Exception {
        Console console = new SimpleConsole<>(new MockupNetModule());
        Connection connection = new MockupConnection(console, "RemoteAddress");

        new SimpleMessage(connection, null);
    }

}

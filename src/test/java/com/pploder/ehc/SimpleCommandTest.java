package com.pploder.ehc;

import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;

public class SimpleCommandTest {

    @Test
    public void testConstructor() throws Exception {
        Console console = new SimpleConsole<>(new MockupNetModule());
        Message source = new SimpleMessage(
                new MockupConnection(console, "RemoteAddress"),
                "Command Arg1 Arg2 Arg3");

        SimpleCommand simpleCommand = new SimpleCommand(source, "Command",
                new String[]{"Arg1", "Arg2", "Arg3"});

        Assert.assertEquals(source, simpleCommand.getSource());
        Assert.assertEquals("Command", simpleCommand.getName());

        Assert.assertEquals(3, simpleCommand.getArgsCount());
        Assert.assertEquals("Arg1", simpleCommand.getArg(0));
        Assert.assertEquals("Arg2", simpleCommand.getArg(1));
        Assert.assertEquals("Arg3", simpleCommand.getArg(2));

        Iterator<String> argsIterator = simpleCommand.args().iterator();
        Assert.assertEquals("Arg1", argsIterator.next());
        Assert.assertEquals("Arg2", argsIterator.next());
        Assert.assertEquals("Arg3", argsIterator.next());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorMessageNull() {
        new SimpleCommand(null, "Command", new String[0]);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorCommandNull() throws Exception {
        Console console = new SimpleConsole<>(new MockupNetModule());
        Message source = new SimpleMessage(
                new MockupConnection(console, "RemoteAddress"),
                "Command Arg1 Arg2 Arg3");

        new SimpleCommand(source, null, new String[]{"Arg1", "Arg2", "Arg3"});
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorArgsNull() throws Exception {
        Console console = new SimpleConsole<>(new MockupNetModule());
        Message source = new SimpleMessage(
                new MockupConnection(console, "RemoteAddress"),
                "Command Arg1 Arg2 Arg3");

        new SimpleCommand(source, "Command", null);
    }

}

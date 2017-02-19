package com.pploder.ehc;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class SimpleCommandRegistryTest {

    @Test
    public void testEmptyConstructor() {
        CommandRegistry commandRegistry = new SimpleCommandRegistry();

        Assert.assertEquals(SimpleCommandRegistry.DEFAULT_UNKNOWN_COMMAND_LISTENER, commandRegistry.getUnknownCommandListener());
    }

    @Test
    public void testNormalConstructor() {
        CommandListener commandListener = cmd -> {};

        CommandRegistry commandRegistry = new SimpleCommandRegistry(commandListener);

        Assert.assertEquals(commandListener, commandRegistry.getUnknownCommandListener());
    }

    @Test
    public void testNormalConstructorNull() {
        CommandRegistry commandRegistry = new SimpleCommandRegistry(null);

        Assert.assertNull(commandRegistry.getUnknownCommandListener());
    }

    @Test
    public void testSetUnknownCommandHandler() {
        CommandListener commandListener1 = cmd -> {};
        CommandListener commandListener2 = cmd -> {};

        CommandRegistry commandRegistry = new SimpleCommandRegistry(commandListener1);

        Assert.assertEquals(commandListener1, commandRegistry.getUnknownCommandListener());
        commandRegistry.setUnknownCommandListener(commandListener2);
        Assert.assertEquals(commandListener2, commandRegistry.getUnknownCommandListener());
    }

    @Test
    public void testSetUnknownCommandHandlerNull() {
        CommandRegistry commandRegistry = new SimpleCommandRegistry();

        commandRegistry.setUnknownCommandListener(null);
        Assert.assertNull(commandRegistry.getUnknownCommandListener());
    }

    @Test
    public void testPutAndRetrieveCommandListener() {
        String command = "command";
        CommandListener commandListener = cmd -> {};

        CommandRegistry commandRegistry = new SimpleCommandRegistry();
        commandRegistry.put(command, commandListener);

        Assert.assertEquals(commandListener, commandRegistry.getCommandListener(command));
    }

    @Test
    public void testPutAndExecuteCommandListener() {
        AtomicBoolean executed = new AtomicBoolean();
        String command = "command";
        CommandListener commandListener = cmd -> executed.set(true);

        CommandRegistry commandRegistry = new SimpleCommandRegistry();
        commandRegistry.put(command, commandListener);

        commandRegistry.getCommandListener(command).accept(null);

        Assert.assertTrue(executed.get());
    }

    @Test
    public void testPutAndRemoveCommandListener() {
        String command = "command";
        CommandListener commandListener = cmd -> {};

        CommandRegistry commandRegistry = new SimpleCommandRegistry();
        commandRegistry.put(command, commandListener);

        Assert.assertEquals(commandListener, commandRegistry.getCommandListener(command));

        commandRegistry.remove(command);

        Assert.assertNull(commandRegistry.getCommandListener(command));
        Assert.assertFalse(commandRegistry.commands().iterator().hasNext());
    }

    @Test(expected = NullPointerException.class)
    public void testPutCommandListenerNull() {
        CommandRegistry commandRegistry = new SimpleCommandRegistry();

        commandRegistry.put("command", null);
    }

    @Test
    public void testFallback() {
        CommandListener fallback = cmd -> {};

        CommandRegistry commandRegistry = new SimpleCommandRegistry(fallback);

        Assert.assertEquals(fallback, commandRegistry.getCommandListenerOrFallback("command"));
    }

    @Test
    public void testIteration() {
        String command1 = "command1";
        String command2 = "command2";
        String command3 = "command3";
        CommandListener commandListener = cmd -> {};

        CommandRegistry commandRegistry = new SimpleCommandRegistry();
        commandRegistry.put(command1, commandListener);
        commandRegistry.put(command2, commandListener);
        commandRegistry.put(command3, commandListener);

        List<String> commands = new ArrayList<>();
        commandRegistry.commands().forEach(commands::add);

        Assert.assertEquals(3, commands.size());
        Assert.assertTrue(commands.contains(command1));
        Assert.assertTrue(commands.contains(command2));
        Assert.assertTrue(commands.contains(command3));
    }

}

package com.pploder.ehc;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class SimpleCommandRegistryTest {

    @Test
    public void testEmptyConstructor() {
        CommandRegistry commandRegistry = new SimpleCommandRegistry();

        Assert.assertEquals(SimpleCommandRegistry.DEFAULT_UNKNOWN_COMMAND_LISTENER,
                commandRegistry.getUnknownCommandListener().get());
    }

    @Test
    public void testNormalConstructor() {
        Consumer<Command> commandListener = cmd -> {};

        CommandRegistry commandRegistry = new SimpleCommandRegistry(commandListener);

        Assert.assertEquals(commandListener, commandRegistry.getUnknownCommandListener().get());
    }

    @Test
    public void testNormalConstructorNull() {
        CommandRegistry commandRegistry = new SimpleCommandRegistry(null);

        Assert.assertFalse(commandRegistry.getUnknownCommandListener().isPresent());
    }

    @Test
    public void testSetUnknownCommandHandler() {
        Consumer<Command> commandListener1 = cmd -> {};
        Consumer<Command> commandListener2 = cmd -> {};

        CommandRegistry commandRegistry = new SimpleCommandRegistry(commandListener1);

        Assert.assertEquals(commandListener1, commandRegistry.getUnknownCommandListener().get());
        commandRegistry.setUnknownCommandListener(commandListener2);
        Assert.assertEquals(commandListener2, commandRegistry.getUnknownCommandListener().get());
    }

    @Test
    public void testSetUnknownCommandHandlerNull() {
        CommandRegistry commandRegistry = new SimpleCommandRegistry();

        commandRegistry.setUnknownCommandListener(null);
        Assert.assertFalse(commandRegistry.getUnknownCommandListener().isPresent());
    }

    @Test
    public void testPutAndRetrieveCommandListener() {
        String command = "command";
        Consumer<Command> commandListener = cmd -> {};

        CommandRegistry commandRegistry = new SimpleCommandRegistry();
        commandRegistry.put(command, commandListener);

        Assert.assertEquals(commandListener, commandRegistry.getCommandListener(command).get());
    }

    @Test
    public void testPutAndExecuteCommandListener() {
        AtomicBoolean executed = new AtomicBoolean();
        String command = "command";
        Consumer<Command> commandListener = cmd -> executed.set(true);

        CommandRegistry commandRegistry = new SimpleCommandRegistry();
        commandRegistry.put(command, commandListener);

        commandRegistry.getCommandListener(command).ifPresent(commandConsumer -> commandConsumer.accept(null));

        Assert.assertTrue(executed.get());
    }

    @Test
    public void testPutAndRemoveCommandListener() {
        String command = "command";
        Consumer<Command> commandListener = cmd -> {};

        CommandRegistry commandRegistry = new SimpleCommandRegistry();
        commandRegistry.put(command, commandListener);

        Assert.assertEquals(commandListener, commandRegistry.getCommandListener(command).get());

        commandRegistry.remove(command);

        Assert.assertFalse(commandRegistry.getCommandListener(command).isPresent());
        Assert.assertFalse(commandRegistry.commands().iterator().hasNext());
    }

    @Test(expected = NullPointerException.class)
    public void testPutCommandListenerNull() {
        CommandRegistry commandRegistry = new SimpleCommandRegistry();

        commandRegistry.put("command", null);
    }

    @Test
    public void testFallback() {
        Consumer<Command> fallback = cmd -> {};

        CommandRegistry commandRegistry = new SimpleCommandRegistry(fallback);

        Assert.assertEquals(fallback, commandRegistry.getCommandListenerOrFallback("command").get());
    }

    @Test
    public void testIteration() {
        String command1 = "command1";
        String command2 = "command2";
        String command3 = "command3";
        Consumer<Command> commandListener = cmd -> {};

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

    @Test
    public void testMessageSupply() throws Exception {
        AtomicInteger argsCount = new AtomicInteger();
        AtomicReference<Iterator> args = new AtomicReference<>();
        String command = "command";
        Message message = new SimpleMessage(
                new MockupConnection(new SimpleConsole<>(new MockupNetModule()), "RemoteAddress"),
                "command 1 2 3");
        Consumer<Command> commandListener = cmd -> {
            argsCount.set(cmd.getArgsCount());
            args.set(cmd.args().iterator());
        };

        CommandRegistry commandRegistry = new SimpleCommandRegistry();
        commandRegistry.put(command, commandListener);

        commandRegistry.supplyMessage(message);

        Assert.assertEquals(3, argsCount.get());
        Assert.assertEquals("1", args.get().next());
        Assert.assertEquals("2", args.get().next());
        Assert.assertEquals("3", args.get().next());
    }

}

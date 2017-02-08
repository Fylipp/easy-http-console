package com.pploder.ehc;

import java.util.function.Consumer;

/**
 * A function that handles a {@link Command}.
 *
 * @author Philipp Ploder
 * @version 1.0.0
 * @since 1.0.0
 */
@FunctionalInterface
public interface CommandHandler extends Consumer<Command> {

}

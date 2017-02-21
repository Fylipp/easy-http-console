package com.pploder.ehc;

import java.util.function.Consumer;

/**
 * A function that handles a {@link Connection}.
 *
 * @author Philipp Ploder
 * @version 2.0.0
 * @since 2.0.0
 */
@FunctionalInterface
public interface ConnectionListener extends Consumer<Connection> {

}

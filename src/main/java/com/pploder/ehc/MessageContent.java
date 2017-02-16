package com.pploder.ehc;

import org.json.simple.JSONArray;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * Represents the content of a styled message.
 * A styled message consists of an ordered sequence of snippets.
 *
 * @author Philipp Ploder
 * @version 2.0.0
 * @since 2.0.0
 */
public class MessageContent implements Iterable<MessageSnippet> {

    private final List<MessageSnippet> snippets;

    /**
     * Creates a new instance.
     *
     * @param snippets An array that may be empty but may not contain {@code null}.
     * @throws NullPointerException If the given array or any contained item is {@code null}.
     */
    public MessageContent(MessageSnippet... snippets) throws NullPointerException {
        for (MessageSnippet snippet : snippets) {
            if (snippet == null) {
                throw new IllegalArgumentException("One of the snippets is null");
            }
        }

        this.snippets = Arrays.asList(snippets);
    }

    /**
     * Creates a new instance containing a single default style snippet with the given content.
     *
     * @param charSequence The content of the single snippet.
     * @return A new instance containing a single default style snippet with the given content.
     */
    public static MessageContent of(CharSequence charSequence) {
        return new MessageContent(new MessageSnippet(charSequence.toString()));
    }

    /**
     * @return The amount of snippets.
     */
    public final int getSnippetsCount() {
        return snippets.size();
    }

    /**
     * Retrieves the snippet at the given index.
     *
     * @param index The index of the requested snippet.
     * @return The snippet at the requested index.
     * @throws IndexOutOfBoundsException If the given index does not exist
     */
    public final MessageSnippet getSnippet(int index) throws IndexOutOfBoundsException {
        return snippets.get(index);
    }

    /**
     * An iterator to stream generated instances of {@link MessageSnippet} from a JSON array.
     *
     * @param array The JSON array.
     * @return An iterator
     */
    public static Iterator<MessageSnippet> iterateFromJSON(JSONArray array) {
        return new MessageSnippetIterator(array);
    }

    /**
     * Generates a JSON array from the snippets.
     *
     * @return The snippets as a JSON array.
     */
    public JSONArray asJSON() {
        JSONArray array = new JSONArray();

        for (MessageSnippet snippet : this) {
            array.add(snippet.asJSON());
        }

        return array;
    }

    @Override
    public Iterator<MessageSnippet> iterator() {
        return snippets.iterator();
    }

    @Override
    public void forEach(Consumer<? super MessageSnippet> action) {
        snippets.forEach(action);
    }

    @Override
    public Spliterator<MessageSnippet> spliterator() {
        return snippets.spliterator();
    }
}

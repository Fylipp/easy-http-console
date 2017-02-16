package com.pploder.ehc;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Iterator;

/**
 * An iterator for streaming a collection of generated {@link MessageSnippet} instances from a JSON array.
 *
 * @author Philipp Ploder
 * @version 2.0.0
 * @since 2.0.0
 */
class MessageSnippetIterator implements Iterator<MessageSnippet> {

    private final Iterator iterator;

    /**
     * Creates a new instance.
     *
     * @param array The JSON array.
     */
    public MessageSnippetIterator(JSONArray array) {
        this.iterator = array.iterator();
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public MessageSnippet next() {
        return MessageSnippet.fromJSON((JSONObject) iterator.next());
    }

}

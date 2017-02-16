package com.pploder.ehc;

import javafx.scene.paint.Color;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

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

    private static final String JSON_TEXT = "text";
    private static final String JSON_COLOR = "color";
    private static final String JSON_BOLD = "bold";
    private static final String JSON_ITALIC = "italic";
    private static final String JSON_UNDERLINED = "underlined";
    private static final String JSON_STRIKETHROUGH = "strikethrough";

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
     * Deserializes an object from JSON.
     *
     * @param json The JSON.
     * @return The deserialized object.
     * @throws Exception If something goes wrong.
     */
    public static MessageContent fromJSON(String json) throws Exception {
        JSONParser parser = new JSONParser();
        JSONArray array = (JSONArray) parser.parse(json);

        MessageSnippet[] snippets = new MessageSnippet[array.size()];

        for (int i = 0; i < snippets.length; i++) {
            JSONObject object = (JSONObject) array.get(i);

            String text = (String) object.get(JSON_TEXT);

            Color colorOverride = (Color) object.get(JSON_COLOR);
            Boolean boldOverride = (Boolean) object.get(JSON_BOLD);
            Boolean italicOverride = (Boolean) object.get(JSON_ITALIC);
            Boolean underlinedOverride = (Boolean) object.get(JSON_UNDERLINED);
            Boolean strikethroughtOverride = (Boolean) object.get(JSON_STRIKETHROUGH);

            snippets[i] = new MessageSnippet(text, colorOverride, boldOverride, italicOverride, underlinedOverride, strikethroughtOverride);
        }

        return new MessageContent(snippets);
    }

    /**
     * Serializes this object as JSON.
     *
     * @return This object serialized as JSON.
     */
    public String asJSON() {
        JSONArray array = new JSONArray();

        for (MessageSnippet snippet : this) {
            JSONObject object = new JSONObject();

            object.put(JSON_TEXT, JSONObject.escape(snippet.getText()));

            Color colorOverride = snippet.getColorOverride();
            if (colorOverride != null) {
                JSONArray rgb = new JSONArray();
                rgb.add((int) (colorOverride.getRed() * 255));
                rgb.add((int) (colorOverride.getGreen() * 255));
                rgb.add((int) (colorOverride.getBlue() * 255));

                object.put(JSON_COLOR, rgb);
            }

            Boolean boldOverride = snippet.getBoldOverride();
            if (boldOverride != null) {
                object.put(JSON_BOLD, boldOverride.booleanValue());
            }

            Boolean italicOverride = snippet.getItalicOverride();
            if (italicOverride != null) {
                object.put(JSON_ITALIC, italicOverride.booleanValue());
            }

            Boolean underlinedOverride = snippet.getUnderlinedOverride();
            if (underlinedOverride != null) {
                object.put(JSON_UNDERLINED, underlinedOverride.booleanValue());
            }

            Boolean strikethroughOverride = snippet.getStrikethroughOverride();
            if (strikethroughOverride != null) {
                object.put(JSON_STRIKETHROUGH, strikethroughOverride.booleanValue());
            }

            array.add(object);
        }

        return array.toJSONString();
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

package com.pploder.ehc;

import javafx.scene.paint.Color;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Objects;

/**
 * Represents a uniformly styled portion of a message.
 * A snippet must contain a text (non-{@code null}) and may override certain display properties if needed.
 *
 * @author Philipp Ploder
 * @version 2.0.0
 * @since 2.0.0
 */
public class MessageSnippet {

    private static final String JSON_TEXT = "text";
    private static final String JSON_COLOR = "color";
    private static final String JSON_BOLD = "bold";
    private static final String JSON_ITALIC = "italic";
    private static final String JSON_UNDERLINED = "underlined";
    private static final String JSON_STRIKETHROUGH = "strikethrough";

    private final String text;
    private final Color colorOverride;
    private final Boolean boldOverride;
    private final Boolean italicOverride;
    private final Boolean underlinedOverride;
    private final Boolean strikethroughOverride;

    /**
     * Creates a new instance with the given text and no style overrides.
     *
     * @param text The snippet text.
     */
    public MessageSnippet(String text) {
        this(text, null);
    }

    /**
     * Creates a new instance with the given text and a potential color override.
     *
     * @param text          The snippet text.
     * @param colorOverride The color override or {@code null}.
     */
    public MessageSnippet(String text, Color colorOverride) {
        this(text, colorOverride, null, null);
    }

    /**
     * Creates a new instance with the given text and a potential override for color, bold and italic.
     *
     * @param text           The snippet text.
     * @param colorOverride  The color override or {@code null}.
     * @param boldOverride   The bold override or {@code null}.
     * @param italicOverride The italic override or {@code null}.
     */
    public MessageSnippet(String text, Color colorOverride, Boolean boldOverride, Boolean italicOverride) {
        this(text, colorOverride, boldOverride, italicOverride, null, null);
    }

    /**
     * Creates a new instance with the given text and a potential override for color, bold, italic, underlined and strikethrough.
     *
     * @param text                  The snippet text.
     * @param colorOverride         The color override or {@code null}.
     * @param boldOverride          The bold override or {@code null}.
     * @param italicOverride        The italic override or {@code null}.
     * @param underlinedOverride    The underlined override or {@code null}.
     * @param strikethroughOverride The strikethrough override or {@code null}.
     */
    public MessageSnippet(String text, Color colorOverride, Boolean boldOverride, Boolean italicOverride, Boolean underlinedOverride, Boolean strikethroughOverride) {
        this.text = Objects.requireNonNull(text);
        this.colorOverride = colorOverride;
        this.boldOverride = boldOverride;
        this.italicOverride = italicOverride;
        this.underlinedOverride = underlinedOverride;
        this.strikethroughOverride = strikethroughOverride;
    }

    /**
     * Generates a JSON object from a snippet.
     *
     * @return The snippet as a JSON object.
     */
    public JSONObject asJSON() {
        JSONObject object = new JSONObject();

        object.put(JSON_TEXT, JSONObject.escape(getText()));

        Color colorOverride = getColorOverride();
        if (colorOverride != null) {
            JSONArray rgb = new JSONArray();
            rgb.add((int) (colorOverride.getRed() * 255));
            rgb.add((int) (colorOverride.getGreen() * 255));
            rgb.add((int) (colorOverride.getBlue() * 255));

            object.put(JSON_COLOR, rgb);
        }

        Boolean boldOverride = getBoldOverride();
        if (boldOverride != null) {
            object.put(JSON_BOLD, boldOverride.booleanValue());
        }

        Boolean italicOverride = getItalicOverride();
        if (italicOverride != null) {
            object.put(JSON_ITALIC, italicOverride.booleanValue());
        }

        Boolean underlinedOverride = getUnderlinedOverride();
        if (underlinedOverride != null) {
            object.put(JSON_UNDERLINED, underlinedOverride.booleanValue());
        }

        Boolean strikethroughOverride = getStrikethroughOverride();
        if (strikethroughOverride != null) {
            object.put(JSON_STRIKETHROUGH, strikethroughOverride.booleanValue());
        }

        return object;
    }

    /**
     * Generates an instance from a JSON object.
     *
     * @param object The JSON object.
     * @return The generated instance.
     */
    public static MessageSnippet fromJSON(JSONObject object) {
        String text = (String) object.get(JSON_TEXT);

        Color colorOverride = (Color) object.get(JSON_COLOR);
        Boolean boldOverride = (Boolean) object.get(JSON_BOLD);
        Boolean italicOverride = (Boolean) object.get(JSON_ITALIC);
        Boolean underlinedOverride = (Boolean) object.get(JSON_UNDERLINED);
        Boolean strikethroughtOverride = (Boolean) object.get(JSON_STRIKETHROUGH);

        return new MessageSnippet(text, colorOverride, boldOverride, italicOverride, underlinedOverride, strikethroughtOverride);
    }

    /**
     * @return The snippet text.
     */
    public final String getText() {
        return text;
    }

    /**
     * @return The color override or {@code null}.
     */
    public final Color getColorOverride() {
        return colorOverride;
    }

    /**
     * @return The bold override or {@code null}.
     */
    public Boolean getBoldOverride() {
        return boldOverride;
    }

    /**
     * @return The italic override or {@code null}.
     */
    public Boolean getItalicOverride() {
        return italicOverride;
    }

    /**
     * @return The underlined override or {@code null}.
     */
    public Boolean getUnderlinedOverride() {
        return underlinedOverride;
    }

    /**
     * @return The strikethrough override or {@code null}.
     */
    public Boolean getStrikethroughOverride() {
        return strikethroughOverride;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageSnippet that = (MessageSnippet) o;

        if (!text.equals(that.text)) return false;
        if (colorOverride != null ? !colorOverride.equals(that.colorOverride) : that.colorOverride != null)
            return false;
        if (boldOverride != null ? !boldOverride.equals(that.boldOverride) : that.boldOverride != null) return false;
        if (italicOverride != null ? !italicOverride.equals(that.italicOverride) : that.italicOverride != null)
            return false;
        if (underlinedOverride != null ? !underlinedOverride.equals(that.underlinedOverride) : that.underlinedOverride != null)
            return false;
        return strikethroughOverride != null ? strikethroughOverride.equals(that.strikethroughOverride) : that.strikethroughOverride == null;
    }

    @Override
    public int hashCode() {
        int result = text.hashCode();
        result = 31 * result + (colorOverride != null ? colorOverride.hashCode() : 0);
        result = 31 * result + (boldOverride != null ? boldOverride.hashCode() : 0);
        result = 31 * result + (italicOverride != null ? italicOverride.hashCode() : 0);
        result = 31 * result + (underlinedOverride != null ? underlinedOverride.hashCode() : 0);
        result = 31 * result + (strikethroughOverride != null ? strikethroughOverride.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MessageSnippet{" +
                "text='" + text + '\'' +
                ", colorOverride=" + colorOverride +
                ", boldOverride=" + boldOverride +
                ", italicOverride=" + italicOverride +
                ", underlinedOverride=" + underlinedOverride +
                ", strikethroughOverride=" + strikethroughOverride +
                '}';
    }
}

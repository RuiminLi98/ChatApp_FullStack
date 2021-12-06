package edu.rice.comp504.model.message;

/**
 * A concrete class to represent one text type message.
 */
public class TextMessage extends Message{


    private final String body;
    private final String font;
    private final String color;
    private final int size;

    /**
     * A constructor for TextMessage.
     * @param timestamp The timestamp
     * @param sendUser The message sender's username
     * @param body The content of the text message
     * @param font The font
     * @param color The color
     * @param size The size of text
     */
    private TextMessage(String timestamp, String sendUser, String body, String font, String color, int size) {
        super(timestamp, sendUser, "text");
        this.body = body;
        this.font = font;
        this.color = color;
        this.size = size;
    }

    /**
     * Make function.
     * @param timestamp The timestamp
     * @param sendUser The message sender's username
     * @param body The content of the text message
     * @param font The font
     * @param color The color
     * @param size The size of text
     * @return TextMessage object
     */
    public static Message make(String timestamp, String sendUser, String body, String font, String color, int size) {
        return new TextMessage(timestamp, sendUser, body, font, color, size);
    }

    /**
     * Get size of text.
     * @return The size
     */
    public int getSize() {
        return size;
    }

    /**
     * Get content of text.
     * @return The body content
     */
    public String getBody() {
        return body;
    }

    /**
     * Get color of text.
     * @return The text color
     */
    public String getColor() {
        return color;
    }

    /**
     * Get font of the text.
     * @return The font
     */
    public String getFont() {
        return font;
    }

}

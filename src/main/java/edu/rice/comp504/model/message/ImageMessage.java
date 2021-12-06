package edu.rice.comp504.model.message;

/**
 * A concrete class to represent one image type message.
 */
public class ImageMessage extends Message{

    private final String sourceUrl;
    private final double scale;

    /**
     * A constructor for ImageMessage.
     * @param timestamp The timestamp
     * @param sendUser The message sender's username
     * @param sourceUrl The source URL of the image
     * @param scale The scale of the image
     */
    private ImageMessage(String timestamp, String sendUser, String sourceUrl, double scale) {
        super(timestamp, sendUser, "image");
        this.sourceUrl = sourceUrl;
        this.scale = scale;
    }

    /**
     * Make function.
     * @param timestamp The timestamp
     * @param sendUser The message sender's username
     * @param sourceUrl The source URL of the image
     * @param scale The scale of the image
     * @return ImageMessage object
     */
    public static Message make(String timestamp, String sendUser, String sourceUrl, double scale) {
        return new ImageMessage(timestamp, sendUser, sourceUrl, scale);
    }

    /**
     * Get scale of the image.
     * @return The scale in double
     */
    public double getScale() {
        return scale;
    }

    /**
     * Get source url of the image.
     * @return The source url
     */
    public String getSourceUrl() {
        return sourceUrl;
    }
}

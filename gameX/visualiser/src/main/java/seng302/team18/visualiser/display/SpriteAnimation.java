package seng302.team18.visualiser.display;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class SpriteAnimation extends Transition {

    private final ImageView imageView;
    private final int count;
    private final int columns;
    private final int offsetX;
    private final int offsetY;
    private final int width;
    private final int height;

    private int lastIndex;


    /**
     * Create a new sprite animation by loading a sprite sheet.
     *
     * @param imageView The ImageView to place the sprite on.
     * @param duration How long each frame lasts for (milliseconds).
     * @param count Number of frames in the animation (number of frames on sprite sheet).
     * @param columns Number of columns of frames on sprite sheet.
     * @param offsetX Number of pixels between columns of frames on sprite sheet (usually 0).
     * @param offsetY Number of pixels between rows of frames on sprite sheet (usually 0).
     * @param width Width of sprite sheet in pixels.
     * @param height Height of sprite sheet in pixels.
     */
    public SpriteAnimation(
            ImageView imageView,
            Duration duration,
            int count,   int columns,
            int offsetX, int offsetY,
            int width,   int height) {
        this.imageView = imageView;
        this.count     = count;
        this.columns   = columns;
        this.offsetX   = offsetX;
        this.offsetY   = offsetY;
        this.width     = width;
        this.height    = height;
        setCycleDuration(duration);
        setInterpolator(Interpolator.LINEAR);
    }


    /**
     * Method overridden from Transition super class.
     * Called every frame to update animation.
     *
     * @param k Current position in animation.
     */
    protected void interpolate(double k) {
        final int index = Math.min((int) Math.floor(k * count), count - 1);
        if (index != lastIndex) {
            final int x = (index % columns) * width  + offsetX;
            final int y = (index / columns) * height + offsetY;
            imageView.setViewport(new Rectangle2D(x, y, width, height));
            lastIndex = index;
        }
    }
}
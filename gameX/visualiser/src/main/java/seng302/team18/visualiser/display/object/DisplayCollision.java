package seng302.team18.visualiser.display.object;

import javafx.animation.*;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import seng302.team18.util.XYPair;
import seng302.team18.visualiser.util.PixelMapper;

import java.util.*;
import java.util.function.Consumer;


/**
 * Controls the display of a collision visualisation
 */
public class DisplayCollision extends DisplayBoatDecorator {


    /**
     * Minimum number of particles
     */
    private final static int MIN_SHAPES = 5;
    /**
     * Maximum number of particles
     */
    private final static int MAX_SHAPES = 20;
    /**
     * Minimum size of particles in boat lengths
     */
    private final static double MIN_SIZE = 0.08;
    /**
     * Maximum size of particles in boat lengths
     */
    private final static double MAX_SIZE = 0.2;
    /**
     * Minimum duration of life of particle in milliseconds
     */
    private final static int MIN_DURATION = 250;
    /**
     * Maximum duration of life of particle in milliseconds
     */
    private final static int MAX_DURATION = 600;
    /**
     * Minimum distance of particle path, in boat lengths
     */
    private final static double MIN_TRANSLATION = 0.5;
    /**
     * Maximum distance of particle path, in boat lengths
     */
    private final static double MAX_TRANSLATION = 1.5;
    private final static Color colourSea = Color.rgb(28, 100, 219);
    private final static Color colourFoam = Color.rgb(189, 219, 165);
    private final Collection<Shape> shapes;
    private PixelMapper pixelMapper;
    private Group group;
    private Random random = new Random();
    private final Consumer<Boolean> collisionSoundEffect;


    /**
     * Constructs a new DisplayCollision decorator
     *
     * @param pixelMapper          the mapper to use when mapping geographic coordinates onto the screen
     * @param displayBoat          the display boat to be decorated
     * @param collisionSoundEffect a procedure to be executed when the collision should play its sound effect. takes
     *                             a boolean parameter, which is true if the collision is from the player's boat, else false
     */
    public DisplayCollision(PixelMapper pixelMapper, DisplayBoat displayBoat, Consumer<Boolean> collisionSoundEffect) {
        super(displayBoat);

        this.pixelMapper = pixelMapper;
        this.collisionSoundEffect = collisionSoundEffect;
        shapes = new HashSet<>();
    }


    @Override
    public void addToGroup(Group group) {
        this.group = group;
        super.addToGroup(group);
    }


    @Override
    public void removeFrom(Group group) {
        removeCollisionEffect();
        this.group = null;
        super.removeFrom(group);
    }


    @Override
    public void setHasCollided(boolean hasCollided) {
        super.setHasCollided(hasCollided);

        if (hasCollided) {
            playCollisionAnimation();
            collisionSoundEffect.accept(isControlled());
        }
    }


    /**
     * Plays a collision effect
     */
    private void playCollisionAnimation() {
        int numShapes = MIN_SHAPES + random.nextInt(MAX_SHAPES - MIN_SHAPES);

        for (int i = 0; i < numShapes; i++) {
            Shape shape = generateShape(pixelMapper.mapToPane(getCoordinate()), getColor());
            Animation animation = createTransitions(shape);
            shapes.add(shape);
            group.getChildren().add(shape);
            shape.toFront();
            animation.play();
        }
    }


    /**
     * Sets the animations for the given particle
     *
     * @param shape the particle to be animated
     * @return the animation applied to the shape
     */
    private Animation createTransitions(Shape shape) {
        Duration duration = Duration.millis(MIN_DURATION + random.nextInt(MAX_DURATION - MIN_DURATION));

        FadeTransition fadeTransition = new FadeTransition(duration, shape);
        fadeTransition.setToValue(0);
        fadeTransition.setCycleCount(1);

        final double boatLength = getBoatLength() * pixelMapper.mappingRatio();
        double distance = (MIN_TRANSLATION + (MAX_TRANSLATION - MIN_TRANSLATION) * random.nextDouble()) * boatLength;
        double bearing = Math.PI * 2 * random.nextDouble();
        TranslateTransition translateTransition = new TranslateTransition(duration, shape);
        translateTransition.setFromX(0.5 * boatLength * Math.cos(bearing));
        translateTransition.setFromY(0.5 * boatLength * Math.sin(bearing));
        translateTransition.setToX(distance * Math.cos(bearing));
        translateTransition.setToY(distance * Math.sin(bearing));
        translateTransition.setCycleCount(1);
        translateTransition.setInterpolator(Interpolator.EASE_OUT);

        RotateTransition rotateTransition = new RotateTransition(duration, shape);
        rotateTransition.setByAngle(360 * random.nextDouble() * ((random.nextBoolean()) ? 1 : -1));
        rotateTransition.setCycleCount(1);

        double scale = Math.min(Math.max(random.nextGaussian(), -2), 3);
        ScaleTransition scaleTransition = new ScaleTransition(duration, shape);
        scaleTransition.setToX(scale);
        scaleTransition.setToY(scale);
        scaleTransition.setCycleCount(1);

        ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.getChildren().addAll(
                fadeTransition,
                translateTransition,
                rotateTransition,
                scaleTransition
        );
        parallelTransition.setCycleCount(1);
        parallelTransition.setOnFinished(event -> shapes.remove(shape));

        return parallelTransition;
    }


    /**
     * Creates a particle
     *
     * @param xy        the origin of the colliding object
     * @param boatColor the colour of the colliding object
     * @return the particle
     */
    private Shape generateShape(XYPair xy, Color boatColor) {
        double size = (MIN_SIZE + random.nextDouble() * (MAX_SIZE - MIN_SIZE)) * getBoatLength() * pixelMapper.mappingRatio();
        size = Math.max(size, 8);
        Shape shape = new Rectangle(xy.getX(), xy.getY(), size, size);
        shape.setFill(getRandomColour(boatColor));
        shape.setRotate(random.nextDouble() * 360);
        return shape;
    }


    /**
     * @param boatColour the colour of the colliding object
     * @return a random colour that is 'close to' the given colour or one of the pre defined colours
     */
    private Color getRandomColour(Color boatColour) {
        List<Color> colours = Arrays.asList(colourSea, colourFoam, boatColour);
        Color color = colours.get(random.nextInt(colours.size()));

        final double variation = 15 / 255;
        double red = Math.max(0.0, Math.min(1.0, color.getRed() + (variation * random.nextGaussian())));
        double green = Math.max(0.0, Math.min(1.0, color.getGreen() + (variation * random.nextGaussian())));
        double blue = Math.max(0.0, Math.min(1.0, color.getBlue() + (variation * random.nextGaussian())));

        return new Color(red, green, blue, 1.0);
    }


    /**
     * Destroy the collision effect and all particles
     */
    private void removeCollisionEffect() {
        group.getChildren().removeAll(shapes);
        shapes.clear();
    }
}

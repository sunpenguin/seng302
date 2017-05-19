package seng302.team18.visualiser.display;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import seng302.team18.model.Coordinate;
import seng302.team18.util.XYPair;
import seng302.team18.visualiser.util.PixelMapper;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Manages the rendering of a boat and its associated effects (wake and annotations)
 */
public class DisplayBoat {
    private Polyline boat;
    private Color boatColor;

    private Polygon wake;
    private Color wakeColor = Color.CADETBLUE;
    private double wakeScaleFactor = 1.0d / 32.0d; // the wake is at normal size when the boat is moving 32 speed
    private double minWakeSize = 0.1;

    private Text annotation;
    private String name;
    private Long estimatedTime;
    private Long timeSinceLastMark;
    private Double speed;
    private Coordinate location;
    private int decimalPlaces = 1; // for speed annotation
    private Map<AnnotationType, Boolean> visibleAnnotations;

    private final int ANNOTATION_OFFSET_X = 10;

    private final double BOAT_HEIGHT = 10;
    private final double BOAT_WIDTH = 10;
    private final double WAKE_OFFSET = 0;
    private final double WAKE_WIDTH = 10;
    private final double WAKE_HEIGHT = 20;

    private final Double[] BOAT_SHAPE = new Double[]{
            0.0, BOAT_HEIGHT / -2,
            0.0, BOAT_HEIGHT / 2,
            BOAT_WIDTH / -2, BOAT_HEIGHT / 2,
            0.0, BOAT_HEIGHT / -2,
            BOAT_WIDTH / 2, BOAT_HEIGHT / 2,
            0.0, BOAT_HEIGHT / 2
    };

    private final Double[] WAKE_SHAPE = new Double[]{
            0.0, WAKE_OFFSET / 2,
            WAKE_WIDTH / -2, WAKE_OFFSET / 2 + WAKE_HEIGHT,
            WAKE_WIDTH / 2, WAKE_OFFSET / 2 + WAKE_HEIGHT
    };

    private final Rotate tfmRotation = new Rotate(0, 0, 0);
    private final Scale tfmWakeSpeed = new Scale(1, 1, WAKE_SHAPE[0], WAKE_SHAPE[1]);
    private final Scale tfmWakeZoom = new Scale(1, 1, WAKE_SHAPE[0], WAKE_SHAPE[1]);
    private final Scale tfmBoatZoom = new Scale(1, 1, 0, 0);

    private final PixelMapper pixelMapper;

    /**
     * Creates a new instance of DisplayBoat
     *
     * @param name          the name of the boat
     * @param heading       the boat's heading
     * @param speed         the boat's speed over ground
     * @param boatColor     the color to display the boat in
     * @param estimatedTime the estimated time until the boat reaches the next mark
     */
    public DisplayBoat(PixelMapper pixelMapper, String name, Double heading, Double speed, Color boatColor, Long estimatedTime) {
        this.pixelMapper = pixelMapper;
        this.name = name;
        this.boatColor = boatColor;
        this.estimatedTime = estimatedTime;

        // Speed
        this.speed = speed;
        double scale = (speed != 0) ? speed * wakeScaleFactor : minWakeSize;
        tfmWakeSpeed.setX(scale);
        tfmWakeSpeed.setY(scale);

        // Heading
        tfmRotation.setAngle(heading);

        boat = new Polyline();
        boat.getPoints().addAll(BOAT_SHAPE);
        boat.setFill(boatColor);
        boat.setOnMouseClicked(event -> {
            if (location != null) {
                pixelMapper.setZoomLevel(PixelMapper.ZOOM_LEVEL_4X);
                pixelMapper.setViewPortCenter(location);
            }
        });
        boat.getTransforms().addAll(tfmRotation, tfmBoatZoom);

        wake = new Polygon();
        wake.getPoints().addAll(WAKE_SHAPE);
        wake.setFill(wakeColor);
        wake.getTransforms().addAll(tfmWakeSpeed, tfmRotation, tfmWakeZoom);

        setUpAnnotations();
    }


    private void setUpAnnotations() {
        annotation = new Text();
        visibleAnnotations = new HashMap<>();
        for (AnnotationType type : AnnotationType.values()) {
            visibleAnnotations.put(type, true); // all annotations visible by default
        }
        annotation.setVisible(true);
    }


    private void updateAnnotationText() {
        String textToDisplay = "";
        List<Map.Entry<AnnotationType, Boolean>> sortedEntries = visibleAnnotations
                .entrySet()
                .stream()
                .sorted(Comparator.comparingInt(entry -> entry.getKey().getCode()))
                .collect(Collectors.toList());
        for (Map.Entry<AnnotationType, Boolean> entry : sortedEntries) {
            if (entry.getValue()) {
                if (entry.getKey().equals(AnnotationType.NAME)) {
                    textToDisplay += name + "\n";
                } else if (entry.getKey().equals(AnnotationType.SPEED)) {
                    textToDisplay += String.format("%." + decimalPlaces + "f", speed) + " km/h\n";
                } else if (entry.getKey().equals(AnnotationType.ESTIMATED_TIME_NEXT_MARK) && estimatedTime > 0) {
                    textToDisplay += estimatedTime + "\n";
                } else if (entry.getKey().equals(AnnotationType.TIME_SINCE_LAST_MARK))
                    textToDisplay += timeSinceLastMark + "\n";
            }
        }
        annotation.setText(textToDisplay);
        annotation.setLayoutX(boat.getLayoutX() + ANNOTATION_OFFSET_X);
        annotation.setLayoutY(boat.getLayoutY());
    }

    /**
     * Update the position of a boat's image and it's wake.
     *
     * @param coordinate new position of the boat
     */
    public void moveBoat(Coordinate coordinate) {
        location = coordinate;
        XYPair pixels = pixelMapper.coordToPixel(coordinate);
        boat.setLayoutX(pixels.getX());
        boat.setLayoutY(pixels.getY());
        wake.setLayoutX(pixels.getX());
        wake.setLayoutY(pixels.getY());
        updateAnnotationText();
    }

    /**
     * Set the speed of the DisplayBoat. This will update the boats wake and annotations
     *
     * @param speed the new speed of the boat.
     */
    public void setSpeed(double speed) {
        this.speed = speed;
        double scale = (speed != 0) ? speed * wakeScaleFactor : minWakeSize;
        tfmWakeSpeed.setX(scale);
        tfmWakeSpeed.setY(scale);

        updateAnnotationText();
    }


    /**
     * Rotate the boat and it's wake according to it's heading.
     *
     * @param heading the new heading of the boat
     */
    public void setHeading(double heading) {
        tfmRotation.setAngle(heading);
    }

    /**
     * Scales boat and wake shapes
     *
     * @param scaleFactor factor by which to scale them
     */
    public void setScale(double scaleFactor) {
        tfmBoatZoom.setX(scaleFactor);
        tfmBoatZoom.setY(scaleFactor);
        tfmWakeZoom.setX(scaleFactor);
        tfmWakeZoom.setY(scaleFactor);
    }

    public void setEstimatedTime(Long estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public void addToGroup(Group group) {
        group.getChildren().add(boat);
        group.getChildren().add(wake);
        group.getChildren().add(annotation);
    }


    public void setAnnotationVisible(AnnotationType type, Boolean isVisible) {
        visibleAnnotations.replace(type, isVisible);
        updateAnnotationText();
    }

    public Boolean getAnnotationVisible(AnnotationType type) {
        return visibleAnnotations.get(type);
    }

    /**
     * Forces wake to render at the back at boat at the front
     */
    public void setDisplayOrder() {
        wake.toBack();
        boat.toFront();
    }

    public Color getBoatColor() {
        return boatColor;
    }

    public void setTimeSinceLastMark(Long timeSinceLastMark) {
        this.timeSinceLastMark = timeSinceLastMark;
    }
}

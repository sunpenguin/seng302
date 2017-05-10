package seng302.team18.visualiser.display;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import seng302.team18.util.XYPair;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Created by david on 4/8/17.
 */
public class DisplayBoat {
    private Polyline boat;
    private Color boatColor;
    private Polygon wake;
    private Color wakeColor;
    private double wakeScaleFactor;
    private double minWakeSize;

    private Text annotation;
    private String name;
    private Long estimatedTime;
    private Long timeSinceLastMark;
    private Double heading;
    private Double speed;
    private int decimalPlaces; // for speed annotation
    private Map<AnnotationType, Boolean> visibleAnnotations;

    private final int ANNOTATION_OFFSET_X = 10;
    private final double BOAT_PIVOT_X = 5;
    private final double BOAT_PIVOT_Y = 0;
    private final Double[] BOAT_SHAPE = new Double[] {
            BOAT_PIVOT_X, BOAT_PIVOT_Y,
            10.0, 10.0,
            0.0, 10.0,
            BOAT_PIVOT_X, BOAT_PIVOT_Y,
            5.0, 10.0 };
    private final Double[] WAKE_SHAPE = new Double[] {
            BOAT_PIVOT_X, BOAT_PIVOT_Y,
            0.0, 20.0,
            10.0, 20.0 };


    public DisplayBoat(String name, Double heading, Double speed, Color boatColor, Long estimatedTime) {
        this.name = name;
        this.heading = heading;
        this.speed = speed;
        this.boatColor = boatColor;
        this.estimatedTime = estimatedTime;
        // default values
        wakeColor = Color.CADETBLUE;
        wakeScaleFactor = 1.0d / 32.0d; // the wake is at normal size when the boat is moving 32 speed
        boat = new Polyline();

        boat.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("CLICKED: " + name);
            }
        });


        boat.getPoints().addAll(BOAT_SHAPE);
        boat.setFill(boatColor); // this isn't default
        wake = new Polygon();
        wake.getPoints().addAll(WAKE_SHAPE);
        wake.setFill(wakeColor);
        decimalPlaces = 1;
        minWakeSize = 0.1;

        // initial rotation + wake size
        Rotate rotation = new Rotate(this.heading, BOAT_PIVOT_X, BOAT_PIVOT_Y);
        wake.getTransforms().add(rotation);
        boat.getTransforms().add(rotation);
        if (speed != 0) {
            double scale = speed * wakeScaleFactor;
            Scale wakeSize = new Scale(scale, scale, BOAT_PIVOT_X, BOAT_PIVOT_Y);
            wake.getTransforms().add(wakeSize);
        } else {
            double scale = minWakeSize;
            Scale wakeSize = new Scale(scale, scale, BOAT_PIVOT_X, BOAT_PIVOT_Y);
            wake.getTransforms().add(wakeSize);
        }
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
     * @param pixels New position to update to.
     */
    public void moveBoat(XYPair pixels) {
        boat.setLayoutX(pixels.getX() - BOAT_PIVOT_X); // The boats are 5px from middle to outside so this will center the boat
        boat.setLayoutY(pixels.getY());
        wake.setLayoutX(pixels.getX() - BOAT_PIVOT_X); // Also need to center the wake
        wake.setLayoutY(pixels.getY());
        updateAnnotationText();
    }

    /**
     * Set the speed of the DisplayBoat. This will update the boats wake and annotations
     * @param speed the new speed of the boat.
     */
    public void setSpeed(double speed) {
        List<Transform> transforms = new ArrayList<>(wake.getTransforms());
        for (Transform transform : transforms) {
            if (transform instanceof Scale) {
                wake.getTransforms().remove(transform);
            }
        }
        double scale = speed * wakeScaleFactor;
        Scale wakeSize = new Scale(scale, scale, BOAT_PIVOT_X, BOAT_PIVOT_Y);
        wake.getTransforms().add(wakeSize);
        this.speed = speed;
        updateAnnotationText();
    }


    /**
     * Rotate the boat and it's wake according to it's heading.
     * @param heading the new heading of the boat
     */
    public void setHeading(double heading) {
        List<Transform> transforms = new ArrayList<>(wake.getTransforms());
        for (Transform transform : transforms) {
            if (transform instanceof Rotate) {
                wake.getTransforms().remove(transform);
            }
        }
        boat.getTransforms().clear();
        Rotate rotation = new Rotate(heading, BOAT_PIVOT_X, BOAT_PIVOT_Y);
        wake.getTransforms().add(rotation);
        boat.getTransforms().add(rotation);
        this.heading = heading;
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

    public void toFront() {
        wake.toFront();
        boat.toFront();
    }

    public Color getBoatColor() {
        return boatColor;
    }

    public void setTimeSinceLastMark(Long timeSinceLastMark) {
        this.timeSinceLastMark = timeSinceLastMark;
    }
}

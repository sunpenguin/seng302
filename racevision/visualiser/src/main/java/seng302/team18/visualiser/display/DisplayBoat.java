package seng302.team18.visualiser.display;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import seng302.team18.util.XYPair;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by david on 4/8/17.
 */
public class DisplayBoat {
    private String name;
    private Polyline boat;
    private Color boatColor;
    private Polygon wake;
    private Color wakeColor;
    private Double heading;
    private Double speed;
    private Double wakeScaleFactor;
    private Text annotation;
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


    public DisplayBoat(String name, Double heading, Double speed, Color boatColor) {
        this.name = name;
        this.heading = heading;
        this.speed = speed;
        this.boatColor = boatColor;
        // default values
        wakeColor = Color.ALICEBLUE;
        wakeScaleFactor = 1d / 60d; // the wake is at normal size when the boat is moving 60 speed
        boat = new Polyline();
        boat.getPoints().addAll(BOAT_SHAPE);
        boat.setFill(boatColor); // this isn't default
        wake = new Polygon();
        wake.getPoints().addAll(WAKE_SHAPE);
        wake.setFill(wakeColor);
        // initial rotation + wake size
        Rotate rotation = new Rotate(this.heading, BOAT_PIVOT_X, BOAT_PIVOT_Y);
        wake.getTransforms().add(rotation);
        boat.getTransforms().add(rotation);
        double scale = speed * wakeScaleFactor;
        Scale wakeSize = new Scale(scale, scale, BOAT_PIVOT_X, BOAT_PIVOT_Y);
        wake.getTransforms().add(wakeSize);
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
        for (Map.Entry<AnnotationType, Boolean> entry : visibleAnnotations.entrySet()) {
            if (entry.getValue()) {
                if (entry.getKey().equals(AnnotationType.NAME)) {
                    textToDisplay += name + "\n";
                } else if (entry.getKey().equals(AnnotationType.SPEED)) {
                    textToDisplay += speed + " km/h\n";
                }
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
        double scale = speed / this.speed;
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
        Rotate rotation = new Rotate(heading - this.heading, BOAT_PIVOT_X, BOAT_PIVOT_Y);
        wake.getTransforms().add(rotation);
        boat.getTransforms().add(rotation);
        this.heading = heading;
    }


    public void addToGroup(Group group) {
        group.getChildren().add(boat);
        group.getChildren().add(wake);
        group.getChildren().add(annotation);
    }


    public void setAnnotationVisibile(AnnotationType type, Boolean isVisible) {
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
}

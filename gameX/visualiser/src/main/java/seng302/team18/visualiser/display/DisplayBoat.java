package seng302.team18.visualiser.display;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import seng302.team18.model.Boat;
import seng302.team18.model.Coordinate;
import seng302.team18.util.XYPair;
import seng302.team18.visualiser.util.PixelMapper;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A class that displays the boat on the GUI
 */
public class DisplayBoat {

    private String shortName;
    private DoubleProperty speed = new SimpleDoubleProperty();
    //Set to -1 initially to prevent null pointer problems
    private Coordinate location;
    private Long timeTilNextMark;
    private Long timeSinceLastMark = 0L;
    private Long timeAtLastMark;
    private boolean sailOut;
    private boolean isControlled;
    private double apparentWindDirection;
    private Coordinate boatCenter;
    private double pixelLength;

    private PixelMapper pixelMapper;
    private Polyline boatPoly;
    private Color boatColor;
    private double boatHeight;
    private double boatWidth;
    private Double[] boatShape;

    private final Rotate rotation = new Rotate(0, 0, 0);
    private final Scale boatZoom = new Scale(1, 1, 0, 0);

    private Text annotation;
    private Long estimatedTime = 0L;
    private final int decimalPlaces = 1; // for speed annotation
    private Map<AnnotationType, Boolean> visibleAnnotations;
    private final int ANNOTATION_OFFSET_X = 10;

    protected DisplayBoat() {
    }

    public DisplayBoat(PixelMapper pixelMapper, String name, Color boatColor, double pixelLength) {
        this.pixelMapper = pixelMapper;
        this.shortName = name;
        this.boatColor = boatColor;
        this.pixelLength = pixelLength;
        boatPoly = new Polyline();
        setUpBoatShape();
        boatPoly.getPoints().addAll(boatShape);
        boatPoly.setFill(boatColor);
        boatPoly.setOnMouseClicked(event -> {
            if (location != null) {
                pixelMapper.setZoomLevel(PixelMapper.ZOOM_LEVEL_4X);
                pixelMapper.setViewPortCenter(location);
            }
        });
        boatPoly.getTransforms().addAll(rotation, boatZoom);
        setUpAnnotations();
    }

    private void setUpBoatShape(){
        boatHeight = pixelLength;
        boatWidth = pixelLength;
        boatShape = new Double[]{
                0.0, boatHeight / -2,
                boatWidth / -2, boatHeight / 2,
                boatWidth / 2, boatHeight / 2,
                0.0, boatHeight / -2
        };
    }


    private void setUpAnnotations() {
        annotation = new Text();
        visibleAnnotations = new HashMap<>();
        for (AnnotationType type : AnnotationType.values()) {
            visibleAnnotations.put(type, true); // all annotations visible by default
        }
        annotation.setVisible(true);
    }


    public void setCoordinate(Coordinate coordinate) {
        location = coordinate;
        XYPair pixels = pixelMapper.coordToPixel(coordinate);
        boatPoly.setLayoutX(pixels.getX());
        boatPoly.setLayoutY(pixels.getY());
        updateAnnotationText();
    }


    public Coordinate getCoordinate() {
        return location;
    }


    public void setSpeed(double speed) {
        this.speed.setValue(speed);
        updateAnnotationText();
    }


    public void setScale(double scaleFactor) {
        boatZoom.setX(scaleFactor);
        boatZoom.setY(scaleFactor);
    }


    public void setEstimatedTime(Long estimatedTime) {
        this.estimatedTime = estimatedTime;
    }


    public void addToGroup(Group group) {
        group.getChildren().add(boatPoly);
        group.getChildren().add(annotation);
        annotation.toFront();
        boatPoly.toFront();
    }


    public void setAnnotationVisible(AnnotationType type, Boolean isVisible) {
        visibleAnnotations.replace(type, isVisible);
        updateAnnotationText();
    }


    public Boolean isAnnotationVisible(AnnotationType type) {
        return visibleAnnotations.get(type);
    }


    private void updateAnnotationText() {
        StringBuilder annotationText = new StringBuilder();

        List<Map.Entry<AnnotationType, Boolean>> sortedEntries = visibleAnnotations
                .entrySet()
                .stream()
                .sorted(Comparator.comparingInt(entry -> entry.getKey().getCode()))
                .collect(Collectors.toList());

        for (Map.Entry<AnnotationType, Boolean> entry : sortedEntries) {
            if (entry.getValue()) {
                if (entry.getKey().equals(AnnotationType.NAME)) {
                    annotationText.append(shortName)
                            .append("\n");
                } else if (AnnotationType.SPEED.equals(entry.getKey())) {
                    String formatSpecSpeed = "%." + decimalPlaces + "f";
                    annotationText.append(String.format(formatSpecSpeed, speed.get()))
                            .append(" knots\n");
                } else if (AnnotationType.ESTIMATED_TIME_NEXT_MARK.equals(entry.getKey()) && estimatedTime > 0) {
                    annotationText.append(estimatedTime)
                            .append("\n");
                } else if (AnnotationType.TIME_SINCE_LAST_MARK.equals(entry.getKey()))
                    annotationText.append(timeSinceLastMark)
                            .append("\n");
            }
        }
        annotation.setText(annotationText.toString());
        annotation.setLayoutX(boatPoly.getLayoutX() + ANNOTATION_OFFSET_X);
        annotation.setLayoutY(boatPoly.getLayoutY());
    }


    /**
     * Removes the boat from the given group.
     *
     * @param group to remove from.
     */
    public void removeFrom(Group group) {
        group.getChildren().remove(boatPoly);
    }


    public Color getColor() {
        return boatColor;
    }


    public void setTimeSinceLastMark(Long timeSinceLastMark) {
        this.timeSinceLastMark = timeSinceLastMark;
    }


    public void setHeading(double heading) {
        rotation.setAngle(heading);
    }


    public double getHeading() {
        return rotation.getAngle();
    }


    public String getShortName() {
        return shortName;
    }


    public double getSpeed() {
        return speed.get();
    }


    public long getTimeTilNextMark() {
        return timeTilNextMark;
    }


    public void setTimeTilNextMark(long timeTilNextMark) {
        this.timeTilNextMark = timeTilNextMark;
    }


    public Long getTimeSinceLastMark() {
        return timeSinceLastMark;
    }


    public void setTimeSinceLastMark(long timeSinceLastMark) {
        this.timeSinceLastMark = timeSinceLastMark;
    }


    public Long getTimeAtLastMark() {
        return timeAtLastMark;
    }


    public void setTimeAtLastMark(Long timeAtLastMark) {
        this.timeAtLastMark = timeAtLastMark;
    }


    public boolean isControlled() {
        return isControlled;
    }


    public void setControlled(boolean controlled) {
        isControlled = controlled;
    }

    public void setSailOut(boolean sailOut) {
        this.sailOut = sailOut;
    }

    public void setApparentWindDirection(double apparentWind) {
        this.apparentWindDirection = apparentWind;
    }


    public Polyline getBoatPoly() {
        return boatPoly;
    }
}

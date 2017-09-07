package seng302.team18.visualiser.display;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import seng302.team18.model.BoatStatus;
import seng302.team18.model.Coordinate;
import seng302.team18.model.GeographicLocation;
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
public class DisplayBoat implements GeographicLocation {

    private String shortName;
    private DoubleProperty speed = new SimpleDoubleProperty();
    //Set to -1 initially to prevent null pointer problems
    private Coordinate location;
    private Long timeTilNextMark;
    private Long timeSinceLastMark = 0L;
    private Long timeAtLastMark;
    private boolean isControlled;
    private BoatStatus status = BoatStatus.UNDEFINED;

    private PixelMapper pixelMapper;
    private Polyline boatPoly;
    private Color boatColor;
    private double boatLength;

    private final Rotate rotation = new Rotate(0, 0, 0);

    private Text annotation;
    private Long estimatedTime = 0L;
    private final static int DECIMAL_PLACES = 1; // for speed annotation
    private Map<AnnotationType, Boolean> visibleAnnotations;
    private final static int ANNOTATION_OFFSET_X = 10;


    public DisplayBoat() {
    }


    public DisplayBoat(PixelMapper pixelMapper, String name, Color boatColor, double boatLength) {
        this.pixelMapper = pixelMapper;
        this.shortName = name;
        this.boatColor = boatColor;
        this.boatLength = boatLength;

        boatPoly = new Polyline();
        setUpBoatShape(pixelMapper.mappingRatio());
        boatPoly.setFill(boatColor);
        boatPoly.setOnMouseClicked(event -> {
            if (location != null) {
                pixelMapper.track(this);
                pixelMapper.setTracking(true);
                pixelMapper.setZoomLevel(4);
                pixelMapper.setViewPortCenter(location);
            }
        });
        boatPoly.getTransforms().add(rotation);

        setUpAnnotations();
    }

    private void setUpBoatShape(double mappingRatio) {
        double pixelLength = boatLength * mappingRatio / 2;

        Double[] boatShape = new Double[]{
                0.0, -pixelLength,
                -pixelLength * 0.6667, pixelLength * 0.7454,
                pixelLength * 0.6667, pixelLength * 0.7454,
                0.0, -pixelLength
        };

        boatPoly.getPoints().clear();
        boatPoly.getPoints().addAll(boatShape);
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
        XYPair pixels = pixelMapper.mapToPane(coordinate);
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
        setUpBoatShape(scaleFactor);
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
                    String formatSpecSpeed = "%." + DECIMAL_PLACES + "f";
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
    }


    public void setApparentWindDirection(double apparentWind) {
    }


    public void setDestination(Coordinate destination) {
    }


    public void setBoatStatus(BoatStatus status) {
        if (!(status.equals(this.status))) {
            switch (status) {
                case FINISHED:
                    boatPoly.setOpacity(0.5);
            }
            this.status = status;
        }
    }


    public BoatStatus getStatus() {
        return status;
    }


    public void setColour(Color boatColor) {
        this.boatColor = boatColor;
    }


    public double getBoatLength() {
        return boatLength;
    }
}

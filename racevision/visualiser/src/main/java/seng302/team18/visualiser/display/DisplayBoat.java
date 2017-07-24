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
import seng302.team18.model.IBoat;
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
public class DisplayBoat implements IBoat {

    private String boatName;
    private String shortName;
    private DoubleProperty speed = new SimpleDoubleProperty();
    //Set to -1 initially to prevent null pointer problems
    private IntegerProperty boatLegNumber = new SimpleIntegerProperty(-1);
    private Integer id;
    private Coordinate location;
    private Coordinate destination;
    private IntegerProperty place;
    private double windDirection;
    private Long timeTilNextMark;
    private Long timeSinceLastMark = 0L;
    private Long timeAtLastMark;
    private int status;
    private boolean sailOut;
    private boolean isControlled;
    private Coordinate boatCenter;
    private Boat boatObject;

    private PixelMapper pixelMapper;
    private Polyline boat;
    private Color boatColor;
    private final double BOAT_HEIGHT = 10;
    private final double BOAT_WIDTH = 10;
    private final Double[] BOAT_SHAPE = new Double[]{
            0.0, BOAT_HEIGHT / -2,
            0.0, BOAT_HEIGHT / 2,
            BOAT_WIDTH / -2, BOAT_HEIGHT / 2,
            0.0, BOAT_HEIGHT / -2,
            BOAT_WIDTH / 2, BOAT_HEIGHT / 2,
            0.0, BOAT_HEIGHT / 2
    };

    private final Rotate rotation = new Rotate(0, 0, 0);
    private final Scale boatZoom = new Scale(1, 1, 0, 0);

    private Text annotation;
    private Long estimatedTime = 0L;
    private final int decimalPlaces = 1; // for speed annotation
    private Map<AnnotationType, Boolean> visibleAnnotations;
    private final int ANNOTATION_OFFSET_X = 10;

    protected DisplayBoat() {
    }

    public DisplayBoat(PixelMapper pixelMapper, String name, Color boatColor) {
        this.pixelMapper = pixelMapper;
        this.shortName = name;
        this.boatColor = boatColor;

        boat = new Polyline();
        boat.getPoints().addAll(BOAT_SHAPE);
        boat.setFill(boatColor);
        boat.setOnMouseClicked(event -> {
            if (location != null) {
                pixelMapper.setZoomLevel(PixelMapper.ZOOM_LEVEL_4X);
                pixelMapper.setViewPortCenter(location);
            }
        });
        boat.getTransforms().addAll(rotation, boatZoom);

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


    @Override
    public void setCoordinate(Coordinate coordinate) {
        location = coordinate;
        XYPair pixels = pixelMapper.coordToPixel(coordinate);
        boat.setLayoutX(pixels.getX());
        boat.setLayoutY(pixels.getY());
        updateAnnotationText();
    }


    public Coordinate getCoordinate() {
        return location;
    }


    @Override
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
        group.getChildren().add(boat);
        group.getChildren().add(annotation);
        annotation.toFront();
        boat.toBack();
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
        annotation.setLayoutX(boat.getLayoutX() + ANNOTATION_OFFSET_X);
        annotation.setLayoutY(boat.getLayoutY());
    }


    public Double[] getBOAT_SHAPE() {
        return BOAT_SHAPE;
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


    public DoubleProperty speedProperty() {
        return speed;
    }


    public int getLegNumber() {
        return boatLegNumber.get();
    }


    public IntegerProperty legNumberProperty() {
        return boatLegNumber;
    }


    public void setLegNumber(int boatLegNumber) {
        this.boatLegNumber.set(boatLegNumber);
    }


    public String getName() {
        return boatName;
    }


    public String getShortName() {
        return shortName;
    }


    public double getSpeed() {
        return speed.get();
    }


    public Coordinate getDestination() {
        return destination;
    }


    public void setDestination(Coordinate destination) {
        this.destination = destination;
    }


    public int getPlace() {
        return place.get();
    }


    public void setPlace(int place) {
        this.place.set(place);
    }


    public IntegerProperty placeProperty() {
        return place;
    }


    public Integer getId() {
        return id;
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


    public int getStatus() {
        return status;
    }


    public void setStatus(int status) {
        this.status = status;
    }


    public boolean isControlled() {
        return isControlled;
    }


    public void setControlled(boolean controlled) {
        isControlled = controlled;
    }

    public boolean isSailOut() {
        return sailOut;
    }

    public void setSailOut(boolean sailOut) {
        this.sailOut = sailOut;
    }

    public Boat getBoatObject() {
        return boatObject;
    }

    public void setBoatObject(Boat boatObject) {
        this.boatObject = boatObject;
    }
}

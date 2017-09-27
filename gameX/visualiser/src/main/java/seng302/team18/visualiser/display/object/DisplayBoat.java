package seng302.team18.visualiser.display.object;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import seng302.team18.model.BoatStatus;
import seng302.team18.model.Coordinate;
import seng302.team18.model.GeographicLocation;
import seng302.team18.util.XYPair;
import seng302.team18.visualiser.display.AnnotationType;
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
    private Color boatColor;
    private double boatLength;

    private Text annotation;
    private final static int DECIMAL_PLACES = 1; // for speed annotation
    private Map<AnnotationType, Boolean> visibleAnnotations;
    private final static int ANNOTATION_OFFSET_X = 10;

    private Image boatImage = new Image("/images/race_view/boat_yellow.png");
    private ImageView boatView = new ImageView(boatImage);


    public DisplayBoat() {
    }


    public DisplayBoat(PixelMapper pixelMapper, String name, Color boatColor, double boatLength) {
        this.pixelMapper = pixelMapper;
        this.shortName = name;
        this.boatColor = boatColor;
        this.boatLength = boatLength;

        boatView.setOnMouseClicked(event -> {
            if (location != null) {
                pixelMapper.track(this);
                pixelMapper.setTracking(true);
                pixelMapper.setZoomLevel(4);
                pixelMapper.setViewPortCenter(location);
            }
        });

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


    public void setCoordinate(Coordinate coordinate) {
        location = coordinate;
        XYPair pixels = pixelMapper.mapToPane(coordinate);
        updateAnnotationText();


        double boatSize = pixelMapper.mappingRatio() * boatLength;
        double boatImageSize = boatImage.getHeight();
        double imageRatio = boatImageSize / (boatSize);  // Ratio for scaling image to correct size

        boatView.setScaleX(1 / imageRatio);
        boatView.setScaleY(1 / imageRatio);

        boatView.setLayoutX(pixels.getX() - (boatImageSize / 2));
        boatView.setLayoutY(pixels.getY() - (boatImageSize / 2));
    }


    public Coordinate getCoordinate() {
        return location;
    }


    public void setSpeed(double speed) {
        this.speed.setValue(speed);
        updateAnnotationText();
    }


    public void setScale(double scaleFactor) {
    }


    public void addToGroup(Group group) {
        group.getChildren().add(annotation);
        annotation.toFront();

        group.getChildren().add(boatView);
        boatView.toFront();
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
                }
            }
        }
        annotation.setText(annotationText.toString());
        annotation.setLayoutX(boatView.getLayoutX() + ANNOTATION_OFFSET_X);
        annotation.setLayoutY(boatView.getLayoutY());
    }


    /**
     * Removes the boat from the given group.
     *
     * @param group to remove from.
     */
    public void removeFrom(Group group) {
        group.getChildren().remove(boatView);
    }


    public Color getColor() {
        return boatColor;
    }


    public void setHeading(double heading) {
        boatView.setRotate(heading - 90);
    }


    public double getHeading() {
        return boatView.getRotate() + 90;
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
                    boatView.setOpacity(0.5);
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


    public void setHasCollided(boolean hasCollided) {
    }
}

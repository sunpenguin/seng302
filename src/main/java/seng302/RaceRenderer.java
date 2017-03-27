package seng302;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

/**
 * Class that takes a Race and a Group and draws the Race on the Group.
 */
public class RaceRenderer {

    private Group group;
    private Race race;
    private HashMap<String, Text> annotationsMap = new HashMap<>();
    private HashMap<String, Boolean> visibleAnnotations = new HashMap<>();
    private ArrayList<String> annotations = new ArrayList<>();
    private final int ANNOTATION_OFFSET_X = 10;
    private HashMap<String, Polyline> boats = new HashMap<>();
    private HashMap<String, Rectangle> marks = new HashMap();
    private HashMap<String, Line> gates = new HashMap<>();
    private HashMap<String, CompoundMark> compoundMarkMap = new HashMap<>();
    private Polyline border = new Polyline();

    private HashMap<String, Polygon> wakes = new HashMap<>();
    private HashMap<String, Double> boatHeadings = new HashMap<>();
    private  HashMap<String, Double> boatSpeeds = new HashMap<>();
    private double lowestSpeed;
    private AnchorPane raceViewAnchorPane;
    private final Color MARK_COLOR = Color.BLACK;
    private final double MARK_SIZE = 10.0;
    private final double PADDING = 20.0;
    private final double BOAT_PIVOT_X = 5;
    private final double BOAT_PIVOT_Y = 0;



    /**
     * Constructor for RaceRenderer, takes a Race and Group as parameters.
     *
     * @param race the race containing the boats to be drawn
     * @param group the group to be drawn on
     */
    public RaceRenderer(Race race, Group group, AnchorPane raceViewAnchorPane) {
        this.race = race;
        this.group = group;
        this.raceViewAnchorPane = raceViewAnchorPane;
        lowestSpeed = Double.MAX_VALUE;
        final ArrayList<Color> BOAT_COLOURS = new ArrayList<>(
                Arrays.asList(Color.VIOLET, Color.BEIGE, Color.GREEN, Color.YELLOW, Color.RED, Color.BROWN));

        // Add each annotation to the race
        addAnnotations();

        setupCourse();

        for (int i = 0; i < race.getStartingList().size(); i++) {
            Boat boat = race.getStartingList().get(i);

            if (lowestSpeed > boat.getSpeed()) {
                lowestSpeed = boat.getSpeed();
            }

            boatHeadings.put(boat.getBoatName(), 0d);
            boatSpeeds.put(boat.getBoatName(), 1d);

            setUpWake(boat);
            setUpBoat(boat, BOAT_COLOURS, i);
            // Connect the annotations to each boat
            setUpAnnotations(boat);

        }
    }


    private void addAnnotations() {
        // Add the name and speed annotations, set them true (visible). In future, set all false (invisible) and when
        // rendering only the ones that are true will be selected based on the chosen annotation level
        annotations.add("Name");
        visibleAnnotations.put("Name", true);
        annotations.add("Speed");
        visibleAnnotations.put("Speed", true);
    }


    private void setUpAnnotations(Boat boat) {
        Text boatAnnotation = new Text("");
        annotationsMap.put(boat.getTeamName(), boatAnnotation);
        this.group.getChildren().add(boatAnnotation);
    }


    private void setUpBoat(Boat boat, ArrayList<Color> BOAT_COLOURS, int i) {
        Polyline boatImage = new Polyline();
        boatImage.getPoints().addAll(
                BOAT_PIVOT_X, BOAT_PIVOT_Y,
                10.0, 10.0,
                0.0, 10.0,
                BOAT_PIVOT_X, BOAT_PIVOT_Y,
                5.0, 10.0);
        boatImage.setFill(BOAT_COLOURS.get(i));
        boats.put(boat.getBoatName(), boatImage);
        this.group.getChildren().add(boatImage);
    }


    private void setUpWake(Boat boat) {
        Polygon wake = new Polygon();
        wake.getPoints().addAll(
                BOAT_PIVOT_X, BOAT_PIVOT_Y,
                0.0, 20.0,
                10.0, 20.0
        );
        wake.setFill(Color.ANTIQUEWHITE);
        wakes.put(boat.getBoatName(), wake);
        group.getChildren().add(wake);
    }


    private void setupBoundary() {
        // Renders Boundaries
        for (Coordinate boundary : race.getCourse().getBoundaries()) {
            XYPair boundaryPixels = convertCoordPixel(boundary, true);
            border.getPoints().addAll(boundaryPixels.getX(), boundaryPixels.getY());
        }
        Coordinate boundary = race.getCourse().getBoundaries().get(0);
        XYPair boundaryPixels = convertCoordPixel(boundary, true);
        border.getPoints().addAll(boundaryPixels.getX(), boundaryPixels.getY());

        group.getChildren().addAll(border);
    }


    private void setupMark(CompoundMark compoundMark) {
        compoundMarkMap.put(compoundMark.getName(), compoundMark);

        for (Mark mark : compoundMark.getMarks()) {
            Rectangle markImage = new Rectangle(MARK_SIZE, MARK_SIZE, MARK_COLOR);

            Coordinate coordinate = mark.getCoordinates();
            XYPair pixelCoordinates = convertCoordPixel(coordinate, true);
            markImage.setX(pixelCoordinates.getX() - (MARK_SIZE / 2.0));
            markImage.setY(pixelCoordinates.getY() - (MARK_SIZE / 2.0));

            marks.put(mark.getName(), markImage);
            group.getChildren().add(markImage);

        }
    }


    private void setupGate(CompoundMark compoundMark) {
        ArrayList<XYPair> endPoints = new ArrayList<>();

        for (int i = 0; i < compoundMark.getMarks().size(); i++) {
            Mark mark = compoundMark.getMarks().get(i);
            Rectangle rectangle = new Rectangle(MARK_SIZE, MARK_SIZE, MARK_COLOR);
            Coordinate coordinate = mark.getCoordinates();
            XYPair pixelCoordinates = convertCoordPixel(coordinate, true);
            rectangle.setX(pixelCoordinates.getX() - (MARK_SIZE / 2));
            rectangle.setY(pixelCoordinates.getY() - (MARK_SIZE / 2));
            endPoints.add(pixelCoordinates);

            marks.put(mark.getName(), rectangle);
            group.getChildren().add(rectangle);
        }
        Line line = new Line(
                endPoints.get(0).getX(), endPoints.get(0).getY(),
                endPoints.get(1).getX(), endPoints.get(1).getY());
        line.setFill(Color.WHITE);
        line.setStyle("-fx-stroke: red");

        gates.put(compoundMark.getName(), line);
        group.getChildren().add(line);
    }


    private void setupCourse() {
        ArrayList<CompoundMark> compoundMarks = race.getCourse().getCompoundMarks();

        for (int i = 0 ; i < compoundMarks.size(); i++) {
            CompoundMark compoundMark = compoundMarks.get(i);
            if (!compoundMarkMap.containsKey(compoundMark.getName())) {
                if ((i == 0 || i == compoundMarks.size() - 1) && compoundMark.getMarks().size() == CompoundMark.GATE_SIZE) {
                    setupGate(compoundMark);
                } else {
                    setupMark(compoundMark);
                }
            }
        }
        setupBoundary();
    }


    public void renderCourse() {
        ArrayList<CompoundMark> compoundMarks = race.getCourse().getCompoundMarks();
        // Renders CompoundMarks
        for (int i = 0 ; i < compoundMarks.size(); i++) {
            CompoundMark compoundMark = compoundMarks.get(i);
            if ((i == 0 || i == compoundMarks.size() - 1) && compoundMark.getMarks().size() == CompoundMark.GATE_SIZE) {
                renderGate(compoundMark);
            } else {
                renderCompoundMark(compoundMark);
            }
        }
        // Renders Boundaries
        group.getChildren().remove(border);
        border = new Polyline();
        for (Coordinate boundary : race.getCourse().getBoundaries()) {
            renderBoundary(border, boundary);
        }
        renderBoundary(border, race.getCourse().getBoundaries().get(0));
        group.getChildren().add(border);
    }


    private void renderBoundary(Polyline border, Coordinate boundary) {
        XYPair boundaryPixels = convertCoordPixel(boundary, false);
        border.getPoints().addAll(boundaryPixels.getX(), boundaryPixels.getY());
    }


    private void renderMark(Mark mark) {
        Rectangle rectangle = marks.get(mark.getName());
        Coordinate coordinate = mark.getCoordinates();
        XYPair pixelCoordinates = convertCoordPixel(coordinate, false);
        rectangle.setX(pixelCoordinates.getX() - (MARK_SIZE / 2.0));
        rectangle.setY(pixelCoordinates.getY() - (MARK_SIZE / 2.0));
    }


    private void renderCompoundMark(CompoundMark compoundMark) {
        for (int i = 0; i < compoundMark.getMarks().size(); i++) {
            Mark mark = compoundMark.getMarks().get(i);
            renderMark(mark);
        }
    }


    private void renderGate(CompoundMark compoundMark) {


        ArrayList<XYPair> endPoints = new ArrayList<>();
        for (int i = 0; i < compoundMark.getMarks().size(); i++) {
            Mark mark = compoundMark.getMarks().get(i);

            Rectangle rectangle = marks.get(mark.getName());

            Coordinate coordinate = mark.getCoordinates();
            XYPair pixelCoordinates = convertCoordPixel(coordinate, false);
            rectangle.setX(pixelCoordinates.getX() - (MARK_SIZE / 2));
            rectangle.setY(pixelCoordinates.getY() - (MARK_SIZE / 2));
            endPoints.add(pixelCoordinates);
        }

        Line line = gates.get(compoundMark.getName());

        line.setStartX(endPoints.get(0).getX());
        line.setStartY(endPoints.get(0).getY());
        line.setEndX(endPoints.get(1).getX());
        line.setEndY(endPoints.get(1).getY());
    }


    /**
     * Draws boats in the Race on the Group as well as the visible annotations
     */
    public void renderBoats(boolean setup) {
        for (int i = 0; i < race.getStartingList().size(); i++) {
            // move boat and wake
            Boat boat = race.getStartingList().get(i);
            Coordinate boatCoordinates = boat.getCoordinate();
            XYPair pixels = convertCoordPixel(boatCoordinates, setup);
            Polyline boatImage = boats.get(boat.getBoatName());
            Polygon wake = wakes.get(boat.getBoatName());

            moveBoat(boatImage, wake, pixels);
            scaleWake(boat, wake);
            rotateBoat(boat, boatImage, wake);
            // annotations
            Text annotationToRender = setAnnotationText(boat);
            annotationToRender.setLayoutX(pixels.getX() + ANNOTATION_OFFSET_X);
            annotationToRender.setLayoutY(pixels.getY());
            annotationToRender.setVisible(true);
        }
    }

    private void moveBoat(Polyline boatImage, Polygon wake, XYPair pixels) {
        boatImage.setLayoutX(pixels.getX());
        boatImage.setLayoutY(pixels.getY());
        wake.setLayoutX(pixels.getX());
        wake.setLayoutY(pixels.getY());
    }


    private void scaleWake(Boat boat, Polygon wake) {
        if (boat.getSpeed() != boatSpeeds.get(boat.getBoatName())) {
            double scale = boat.getSpeed() / boatSpeeds.get(boat.getBoatName()) / lowestSpeed;
            Scale wakeSize = new Scale(scale, scale, BOAT_PIVOT_X, BOAT_PIVOT_Y);
            wake.getTransforms().add(wakeSize);
            boatSpeeds.replace(boat.getBoatName(), boat.getSpeed());
        }
    }

    private void rotateBoat(Boat boat, Polyline boatImage, Polygon wake) {
        // update heading if changed
        if (boat.getHeading() != boatHeadings.get(boat.getBoatName())) {
            Rotate rotation = new Rotate(boat.getHeading() - boatHeadings.get(boat.getBoatName()), BOAT_PIVOT_X, BOAT_PIVOT_Y);
            wake.getTransforms().add(rotation);
            boatImage.getTransforms().add(rotation);
            boatHeadings.replace(boat.getBoatName(), boat.getHeading());
        }
    }



    /**
     * Set the annotation text to display next to the boat based on which annotations are true in the visibleAnnotations
     * hashmap.
     * @param boat The boat to set the annotation text for.
     * @return the Text object with the correctly set text.
     */
    private Text setAnnotationText(Boat boat) {
        String textToDisplay = "";
        for (String annotation : annotations) {
            if (visibleAnnotations.get(annotation)) {
                if (annotation.equals("Name")) {
                    textToDisplay += boat.getTeamName() + "\n";
                } else if (annotation.equals("Speed")) {
                    textToDisplay += boat.getSpeed() + " km/h\n";
                }
            }
        }
        Text boatAnnotation = annotationsMap.get(boat.getTeamName());
        boatAnnotation.setText(textToDisplay);

        return boatAnnotation;
    }




    /**
     * Converts the latitude / longitude coordinates to pixel coordinates.
     * @param coord Coordinates to be converted
     * @return x and y pixel coordinates of the given coordinates
     */
    private XYPair convertCoordPixel(Coordinate coord, boolean setup) {


        double pixelWidth;
        double pixelHeight;

        if (setup) {
            pixelWidth = raceViewAnchorPane.getPrefWidth() - PADDING * 2; // TODO get size of screen
            pixelHeight = raceViewAnchorPane.getPrefHeight() - PADDING * 2;
        } else {
            pixelWidth = raceViewAnchorPane.getWidth() - PADDING * 2; // TODO get size of screen
            pixelHeight = raceViewAnchorPane.getHeight() - PADDING * 2;
        }

        GPSCalculations gps = new GPSCalculations(race.getCourse());
        gps.findMinMaxPoints(race.getCourse());
        double courseWidth = gps.getMaxX() - gps.getMinX();
        double courseHeight = gps.getMaxY() - gps.getMinY();
        XYPair planeCoordinates = GPSCalculations.GPSxy(coord);
        double widthRatio = (courseWidth - (gps.getMaxX() - planeCoordinates.getX())) / courseWidth;
        double heightRatio = (courseHeight - (gps.getMaxY() - planeCoordinates.getY())) / courseHeight;

        return new XYPair(pixelWidth * widthRatio + PADDING, (pixelHeight * heightRatio + PADDING) * -1);
    }

    public Group getGroup() {
        return group;
    }


    public HashMap<String, Boolean> getVisibleAnnotations() {
        return visibleAnnotations;
    }


    public void setVisibleAnnotations(HashMap<String, Boolean> visibleAnnotations) {
        this.visibleAnnotations = visibleAnnotations;
    }
}

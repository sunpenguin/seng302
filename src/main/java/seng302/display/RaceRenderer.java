package seng302.display;

import javafx.scene.Group;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import seng302.raceutil.GPSCalculations;
import seng302.raceutil.XYPair;
import seng302.model.*;

import java.util.*;

/**
 * Class that takes a Race and a Group and draws the Race on the Group.
 */
public class RaceRenderer {

    private Group group;
    private Race race;
    private HashMap<String, Text> annotationsMap = new HashMap<>();
    private HashMap<String, Boolean> visibleAnnotations = new HashMap<>();
    private ArrayList<String> annotations = new ArrayList<>();
    private HashMap<String, Polyline> boats = new HashMap<>();
    private HashMap<String, Rectangle> marks = new HashMap();
    private HashMap<String, Line> gates = new HashMap<>();
    private HashMap<String, Polygon> wakes = new HashMap<>();
    private HashMap<String, Double> boatHeadings = new HashMap<>();
    private HashMap<String, Double> boatSpeeds = new HashMap<>();
    private HashMap<String, CompoundMark> compoundMarkMap = new HashMap<>();

    private Map<String, Collection<Circle>> trailMap = new HashMap<>();
    private Map<Circle, Coordinate> circleCoordMap = new HashMap<>();

    private Polyline border = new Polyline();
    private AnchorPane raceViewAnchorPane;
    private double lowestSpeed;
    private final Color MARK_COLOR = Color.BLACK;
    private final double MARK_SIZE = 10.0;
    private final double PADDING = 20.0;
    private final double BOAT_PIVOT_X = 5;
    private final double BOAT_PIVOT_Y = 0;
    private final int ANNOTATION_OFFSET_X = 10;
    final ArrayList<Color> BOAT_COLOURS = new ArrayList<>(
            Arrays.asList(Color.VIOLET, Color.BEIGE, Color.GREEN, Color.YELLOW, Color.RED, Color.BROWN));



    /**
     * Constructor for RaceRenderer, takes a Race, Group  and AnchorPane as parameters.
     *
     * @param race the race containing the boats to be drawn
     * @param group the group to be drawn on
     * @param raceViewAnchorPane The AnchorPane the group is on
     */
    public RaceRenderer(Race race, Group group, AnchorPane raceViewAnchorPane) {
        this.race = race;
        this.group = group;
        this.raceViewAnchorPane = raceViewAnchorPane;
        lowestSpeed = Double.MAX_VALUE;

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

            ArrayList<Circle> circleList = new ArrayList<>();

            trailMap.put(boat.getBoatName(), circleList);

            setUpWake(boat);
            setUpBoat(boat, i);
            // Connect the annotations to each boat
            setUpAnnotations(boat);

        }
    }


    private void addAnnotations() {
        annotations.add("Name");
        visibleAnnotations.put("Name", true);
        annotations.add("Speed");
        visibleAnnotations.put("Speed", true);
    }


    /**
     * Set up an annotation for a boat by mapping the boat's team name to the annotation and
     * adding the annotation to the group so it can be displayed.
     * @param boat Boat to map annotation to.
     */
    private void setUpAnnotations(Boat boat) {
        Text boatAnnotation = new Text("");
        annotationsMap.put(boat.getTeamName(), boatAnnotation);
        this.group.getChildren().add(boatAnnotation);
    }


    /**
     * Set up a boat by creating a triangle to represent the boat onscreen, mapping the boat name to the triangle
     * and adding the triangle to the group.
     * @param boat Boat to set up
     * @param i The number of the boat to be set up.
     */
    private void setUpBoat(Boat boat, int i) {
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


    /**
     * Set up the wake for a boat by creating a triangle for it, mapping the boat name to the wake and adding
     * the wake to the group.
     * @param boat The boat to set up a wake for.
     */
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


    /**
     * Set up the boundary around the course by getting the coordinates of the boundary points and drawing
     * a PolyLine using each point.
     */
    private void setupBoundary() {
        // Renders Boundaries
        for (Coordinate boundary : race.getCourse().getBoundaries()) {
            XYPair boundaryPixels = convertCoordPixel(boundary, true);
            border.getPoints().addAll(boundaryPixels.getX(), boundaryPixels.getY());
        }
        if (race.getCourse().getBoundaries().size() != 0) {
            Coordinate boundary = race.getCourse().getBoundaries().get(0);
            XYPair boundaryPixels = convertCoordPixel(boundary, true);
            border.getPoints().addAll(boundaryPixels.getX(), boundaryPixels.getY());
        }
        group.getChildren().addAll(border);
    }


    /**
     * Set up a CompoundMark by creating a rectangle for each mark within the CompoundMark,
     * setting the necessary x, y coordinates and addinf them to the group.
     * @param compoundMark CompoundMark to set up.
     */
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


    /**
     * Set up a gate (Start or Finish only) by creating a rectangle for the endpoints and drawing a line between them
     * and adding the shapes to the group.
     * @param compoundMark CompoundMark to set up.
     */
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


    /**
     * Set up each stage of the course.
     * Only set up a CompoundMark if it has not already been added to avoid duplicates.
     */
    private void setupCourse() {
        List<CompoundMark> compoundMarks = race.getCourse().getCompoundMarks();

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


    /**
     * Called if the course needs to be re-rendered due to the window being resized.
     */
    public void renderCourse() {
        List<CompoundMark> compoundMarks = race.getCourse().getCompoundMarks();
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
        if (race.getCourse().getBoundaries().size() != 0) {
            renderBoundary(border, race.getCourse().getBoundaries().get(0));
            group.getChildren().add(border);
        }

    }


    /**
     * Reset a point on the border due to resizing
     * @param border Polyline for the border
     * @param boundary the point to reset
     */
    private void renderBoundary(Polyline border, Coordinate boundary) {
        XYPair boundaryPixels = convertCoordPixel(boundary, false);
        border.getPoints().addAll(boundaryPixels.getX(), boundaryPixels.getY());
    }


    /**
     * Reset a point for a mark due to resizing
     * @param mark Mark to reset
     */
    private void renderMark(Mark mark) {
        Rectangle rectangle = marks.get(mark.getName());
        Coordinate coordinate = mark.getCoordinates();
        XYPair pixelCoordinates = convertCoordPixel(coordinate, false);
        rectangle.setX(pixelCoordinates.getX() - (MARK_SIZE / 2.0));
        rectangle.setY(pixelCoordinates.getY() - (MARK_SIZE / 2.0));
    }


    /**
     * Call each Mark in a CompoundMark to reset it's point due to resizing
     * @param compoundMark CompoundMark to reset.
     */
    private void renderCompoundMark(CompoundMark compoundMark) {
        for (int i = 0; i < compoundMark.getMarks().size(); i++) {
            Mark mark = compoundMark.getMarks().get(i);
            renderMark(mark);
        }
    }


    /**
     * Reset the points for the endpoints of a gate as well as the line between them due to resizing
     * @param compoundMark CompundMark to reset (Start/Finish only)
     */
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
    public void renderBoats(boolean setup, int frameCount) {
        for (int i = 0; i < race.getStartingList().size(); i++) {
            // move boat and wake
            Boat boat = race.getStartingList().get(i);
            Coordinate boatCoordinates = boat.getCoordinate();
            XYPair pixels = convertCoordPixel(boatCoordinates, setup);
            Polyline boatImage = boats.get(boat.getBoatName());
            boatImage.toFront();
            Polygon wake = wakes.get(boat.getBoatName());

            moveBoat(boatImage, wake, pixels);
            scaleWake(boat, wake);
            rotateBoat(boat, boatImage, wake);

            if (frameCount == 10) {
                drawTrail(boat, pixels);
            }


            // annotations
            Text annotationToRender = setAnnotationText(boat);
            annotationToRender.setLayoutX(pixels.getX() + ANNOTATION_OFFSET_X);
            annotationToRender.setLayoutY(pixels.getY());
            annotationToRender.setVisible(true);
        }
    }


    /**
     * Drop a circle on the raceView to visualise the boat's route through the race.
     * Add the circle to the group and map the circle to it's coordinates.
     * @param boat Boat to put circle behind.
     * @param pixels point to place the circle at.
     */
    private void drawTrail(Boat boat, XYPair pixels) {
        Circle circle = new Circle();
        circleCoordMap.put(circle, boat.getCoordinate());

        circle.setCenterX(pixels.getX());
        circle.setCenterY(pixels.getY());
        circle.setRadius(0.6);
        circle.setFill(boats.get(boat.getBoatName()).getFill());

        group.getChildren().add(circle);
        Collection<Circle> circles = trailMap.get(boat.getBoatName());
        circles.add(circle);
    }


    /**
     * Redraw the the trails behind each boat by looking into the Map of circles and coordinates and
     * resetting the points.
     * @param boats Collection of boats racing.
     */
    public void reDrawTrail(Collection<Boat> boats) {
        for (Boat boat : boats) {
            for (Circle circle : trailMap.get(boat.getBoatName())) {
                XYPair newPosition = convertCoordPixel(circleCoordMap.get(circle), false);

                circle.setCenterX(newPosition.getX());
                circle.setCenterY(newPosition.getY());
            }
        }
    }


    /**
     * Update the position of a boat's image and it's wake.
     * @param boatImage Boat PolyLine to update.
     * @param wake Wake PolyLine to update.
     * @param pixels New position to update to.
     */
    private void moveBoat(Polyline boatImage, Polygon wake, XYPair pixels) {
        boatImage.setLayoutX(pixels.getX() - 5); // The boats are 5px from middle to outside so this will center the boat
        boatImage.setLayoutY(pixels.getY());
        wake.setLayoutX(pixels.getX() - 5); // Also need to center the wake
        wake.setLayoutY(pixels.getY());
    }


    /**
     * Scale the size of the wake according to the boat's speed.
     * @param boat Boat to scale wake for.
     * @param wake Wake to scale.
     */
    private void scaleWake(Boat boat, Polygon wake) {
        if (boat.getSpeed() != boatSpeeds.get(boat.getBoatName())) {
            double scale = boat.getSpeed() / boatSpeeds.get(boat.getBoatName()) / lowestSpeed;
            Scale wakeSize = new Scale(scale, scale, BOAT_PIVOT_X, BOAT_PIVOT_Y);
            wake.getTransforms().add(wakeSize);
            boatSpeeds.replace(boat.getBoatName(), boat.getSpeed());
        }
    }


    /**
     * Rotate the boat and it's wake according to it's heading.
     * @param boat Boat to rotate.
     * @param boatImage Image for the boat to rotate.
     * @param wake Wake to rotate.
     */
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
            pixelWidth = raceViewAnchorPane.getPrefWidth() - PADDING * 2;
            pixelHeight = raceViewAnchorPane.getPrefHeight() - PADDING * 2;
        } else {
            pixelWidth = raceViewAnchorPane.getWidth() - PADDING * 2;
            pixelHeight = raceViewAnchorPane.getHeight() - PADDING * 2;
        }


        if (pixelHeight > pixelWidth) {
            pixelHeight = pixelWidth;
        } else if (pixelWidth > pixelHeight) {
            pixelWidth = pixelHeight;
        }

        GPSCalculations gps = new GPSCalculations(race.getCourse());
        gps.findMinMaxPoints(race.getCourse());
        double courseWidth = gps.getMaxX() - gps.getMinX();
        double courseHeight = gps.getMaxY() - gps.getMinY();
        XYPair planeCoordinates = GPSCalculations.GPSxy(coord);

        double aspectRatio = courseWidth / courseHeight;

        if (courseHeight > courseWidth) {
            pixelWidth *= aspectRatio;
        } else {
            pixelHeight *= aspectRatio;
        }

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

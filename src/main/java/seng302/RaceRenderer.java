package seng302;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Class that takes a Race and a Group and draws the Race on the Group.
 */
public class RaceRenderer {

    private Group group;
    private Race race;
    private HashMap<String, Circle> boats;

    private HashMap<String, Text> annotationsMap = new HashMap<>();
    private HashMap<String, Boolean> visibleAnnotations = new HashMap<>();
    private ArrayList<String> annotations = new ArrayList<>();
    final private int ANNOTATION_OFFSET_X = 10;
    final private Color MARK_COLOR = Color.BLACK;
    final private double MARK_SIZE = 10.0;
    final private double PADDING = 60.0;


    /**
     * Constructor for RaceRenderer, takes a Race and Group as parameters.
     *
     * @param race the race containing the boats to be drawn
     * @param group the group to be drawn on
     */
    public RaceRenderer(Race race, Group group) {
        this.race = race;
        this.group = group;
        boats = new HashMap<>();

        final double BOAT_RADIUS = 10.0;
        final ArrayList<Color> BOAT_COLOURS = new ArrayList<>(
                Arrays.asList(Color.DODGERBLUE, Color.BEIGE, Color.GREEN, Color.YELLOW, Color.RED, Color.BROWN));

        // Add the name annotation, set it true for (visible). In future, set all false (invisible) and when
        // rendering only render the ones that are true
        annotations.add("Name");
        visibleAnnotations.put("Name", true);

        annotations.add("Speed");
        visibleAnnotations.put("Speed", true);


        for (int i = 0; i < race.getStartingList().size(); i++) {
            String boatName = race.getStartingList().get(i).getBoatName();
            String teamName = race.getStartingList().get(i).getTeamName();

            Circle boat = new Circle(BOAT_RADIUS, BOAT_COLOURS.get(i));
            boats.put(boatName, boat);
            this.group.getChildren().add(boat);

            // Create the Text objects for the boat annotations. Initially just an empty string
            Text boatAnnotation = new Text("");
            annotationsMap.put(teamName, boatAnnotation);
            this.group.getChildren().add(boatAnnotation);
        }
    }


    public void renderCourse() {
        final int SINGLE_MARK_SIZE = 1;
        final int GATE_SIZE = 2;
        ArrayList<CompoundMark> compoundMarks = race.getCourse().getCompoundMarks();
        for (int i = 0 ; i < compoundMarks.size(); i++) {
            CompoundMark compoundMark = compoundMarks.get(i);
            if (compoundMark.getMarks().size() == SINGLE_MARK_SIZE) {
                renderMark(compoundMark.getMarks().get(0));
            } else if (compoundMark.getMarks().size() == GATE_SIZE) {
                renderGate(compoundMark);
            }
        }
    }


    private void renderMark(Mark mark) {
        Rectangle rectangle = new Rectangle(MARK_SIZE, MARK_SIZE, MARK_COLOR);
        Coordinate coordinate = mark.getMarkCoordinates();
        XYPair pixelCoordinates = convertCoordPixel(coordinate);
        rectangle.setX(pixelCoordinates.getX() - (MARK_SIZE / 2));
        rectangle.setY(pixelCoordinates.getY() - (MARK_SIZE / 2));
        group.getChildren().add(rectangle);
    }


    private void renderGate(CompoundMark compoundMark) {
        ArrayList<XYPair> endPoints = new ArrayList<>();

        for (int i = 0; i < compoundMark.getMarks().size(); i++) {
            Mark mark = compoundMark.getMarks().get(i);
            Rectangle rectangle = new Rectangle(MARK_SIZE, MARK_SIZE, MARK_COLOR);
            Coordinate coordinate = mark.getMarkCoordinates();
            XYPair pixelCoordinates = convertCoordPixel(coordinate);
            rectangle.setX(pixelCoordinates.getX() - (MARK_SIZE / 2));
            rectangle.setY(pixelCoordinates.getY() - (MARK_SIZE / 2));
            endPoints.add(pixelCoordinates);
            group.getChildren().add(rectangle);
        }
        Line line = new Line(
                endPoints.get(0).getX(), endPoints.get(0).getY(),
                endPoints.get(1).getX(), endPoints.get(1).getY());
        group.getChildren().add(line);
    }


    /**
     * Draws boats in the Race on the Group as well as the visible annotations
     */
    public void renderBoats() {
        for (int i = 0; i < race.getStartingList().size(); i++) {
            Boat boat = race.getStartingList().get(i);
            Coordinate boatCoordinates = boat.getCoordinate();
            XYPair pixels = convertCoordPixel(boatCoordinates);
            Circle boatImage = boats.get(boat.getBoatName());
            boatImage.setCenterX(pixels.getX());
            boatImage.setCenterY(pixels.getY());

            // Render annotations
            Text annotationToRender = setAnnotationText(boat);
            annotationToRender.setLayoutX(pixels.getX() + ANNOTATION_OFFSET_X);
            annotationToRender.setLayoutY(pixels.getY());
            annotationToRender.setVisible(true);

        }
    }


    private Text setAnnotationText(Boat boat) {

        String textToDisplay = "";
        for (String annotation : annotations) {
            if (visibleAnnotations.get(annotation)) {
                if (annotation.equals("Name")) {
                    textToDisplay += boat.getTeamName() + "\n";
                }

                else if (annotation.equals("Speed")) {
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
    private XYPair convertCoordPixel(Coordinate coord) {
        double pixelWidth = 720.0 - PADDING * 2; // TODO get size of screen
        double pixelHeight = 720.0 - PADDING * 2;
//        double pixelHeight = group.getLayoutY() - PADDING * 2;
//        double pixelWidth = group.getParent().getBoundsInLocal().getWidth() - PADDING * 2;
//        double pixelHeight = group.getParent().getBoundsInLocal().getHeight() - PADDING * 2;
        GPSCalculations gps = new GPSCalculations(race.getCourse());
        gps.findMinMaxPoints(race.getCourse());
        double courseWidth = gps.getMaxX() - gps.getMinX();
        double courseHeight = gps.getMaxY() - gps.getMinY();
        XYPair planeCoordinates = GPSCalculations.GPSxy(coord);
        double widthRatio = (courseWidth - (gps.getMaxX() - planeCoordinates.getX())) / courseWidth;
        double heightRatio = (courseHeight - (gps.getMaxY() - planeCoordinates.getY())) / courseHeight;

        return new XYPair(pixelWidth * widthRatio + PADDING, (pixelHeight * heightRatio + PADDING) * -1);
    }

}

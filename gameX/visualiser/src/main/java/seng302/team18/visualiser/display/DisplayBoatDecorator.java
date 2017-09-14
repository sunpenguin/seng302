package seng302.team18.visualiser.display;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import seng302.team18.model.BoatStatus;
import seng302.team18.model.Coordinate;

/**
 * A decorator pattern abstract class to add functionality to the DisplayBoat class
 */
public abstract class DisplayBoatDecorator extends DisplayBoat {

    private DisplayBoat boat;


    /**
     * Creates a new instance of DisplayBoat
     *
     * @param boat the display boat being decorated
     */
    public DisplayBoatDecorator(DisplayBoat boat) {
        super();
        this.boat = boat;
    }


    @Override
    public void setCoordinate(Coordinate coordinate) {
        boat.setCoordinate(coordinate);
    }


    @Override
    public void setSpeed(double speed) {
        boat.setSpeed(speed);
    }


    @Override
    public void setScale(double scaleFactor) {
        boat.setScale(scaleFactor);
    }


    @Override
    public void setEstimatedTime(Long estimatedTime) {
        boat.setEstimatedTime(estimatedTime);
    }


    @Override
    public void addToGroup(Group group) {
        boat.addToGroup(group);

    }


    @Override
    public void setAnnotationVisible(AnnotationType type, Boolean isVisible) {
        boat.setAnnotationVisible(type, isVisible);
    }


    @Override
    public Boolean isAnnotationVisible(AnnotationType type) {
        return boat.isAnnotationVisible(type);
    }


    @Override
    public Color getColor() {
        return boat.getColor();
    }


    @Override
    public void setTimeSinceLastMark(Long timeSinceLastMark) {
        boat.setTimeSinceLastMark(timeSinceLastMark);
    }


    @Override
    public boolean isControlled() {
        return boat.isControlled();
    }


    @Override
    public void setControlled(boolean controlled) {
        boat.setControlled(controlled);
    }


    @Override
    public Coordinate getCoordinate() {
        return boat.getCoordinate();
    }


    @Override
    public double getHeading() {
        return boat.getHeading();
    }


    @Override
    public void setHeading(double heading) {
        boat.setHeading(heading);
    }


    @Override
    public String getShortName() {
        return boat.getShortName();
    }


    @Override
    public double getSpeed() {
        return boat.getSpeed();
    }


    @Override
    public long getTimeTilNextMark() {
        return boat.getTimeTilNextMark();
    }


    @Override
    public void setTimeTilNextMark(long timeTilNextMark) {
        boat.setTimeTilNextMark(timeTilNextMark);
    }


    @Override
    public Long getTimeSinceLastMark() {
        return boat.getTimeSinceLastMark();
    }


    @Override
    public void setTimeSinceLastMark(long timeSinceLastMark) {
        boat.setTimeSinceLastMark(timeSinceLastMark);
    }


    @Override
    public Long getTimeAtLastMark() {
        return boat.getTimeAtLastMark();
    }


    @Override
    public void setTimeAtLastMark(Long timeAtLastMark) {
        boat.setTimeAtLastMark(timeAtLastMark);
    }


    @Override
    public void setApparentWindDirection(double apparentWind) {
        boat.setApparentWindDirection(apparentWind);
    }


    @Override
    public void setSailOut(boolean sailOut) {
        boat.setSailOut(sailOut);
    }


    @Override
    public void setDestination(Coordinate destination) {
        boat.setDestination(destination);
    }


    @Override
    public void removeFrom(Group group) {
        boat.removeFrom(group);
    }


    @Override
    public void setBoatStatus(BoatStatus status) {
        boat.setBoatStatus(status);
    }


    @Override
    public BoatStatus getStatus() {
        return boat.getStatus();
    }


    @Override
    public void setColour(Color color) {
        boat.setColour(color);
    }


    @Override
    public double getBoatLength() {
        return boat.getBoatLength();
    }


    @Override
    public void setHasCollided(boolean hasCollided) {
        boat.setHasCollided(hasCollided);
    }
}

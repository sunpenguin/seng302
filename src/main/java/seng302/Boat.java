package seng302;

import sun.plugin.dom.core.CoreConstants;

import java.util.ArrayList;

/**
 * A class which represents a boat in the text based Application
 */

public class Boat {

    private String boatName;
    private String teamName;
    private double speed;
    private ArrayList<Mark> markList = new ArrayList<>();
    private Mark currentMark;
    private Mark nextMark;
    private double position;
    private boolean finished = false;
    private int heading;

    private Coordinate boatCoordinates;

    /**
     * A constructor for the Boat class
     * @param boatName The name of the boat
     * @param teamName The name of the team the boat belongs to
     */
    public Boat(String boatName, String teamName) {
        this.boatName = boatName;
        this.teamName = teamName;
    }


    /**
     * Getter for the boat's coordinates
     * @return the coordinates
     */
    public Coordinate getBoatCoordinates() {
        return this.boatCoordinates;
    }


    /**
     * Setter for boat's coordinates
     * @param coordinates the coordinates
     */
    public void setBoatCoordinates(Coordinate coordinates) {
        this.boatCoordinates = coordinates;
    }

    /**
     * Setter for the next mark the boat must pass
     * @param nextMark The mark
     */
    void setNextMark(Mark nextMark) {
        this.nextMark = nextMark;
    }


    /**
     * Getter for the next mark the boat must pass
     * @return the next mark
     */
    Mark getNextMark() {
        return nextMark;
    }


    /**
     * Setter for the current mark.
     * Called when a race is created to set each boat's current mark
     * to the first mark on the course.
     */
    void setCurrentMark(Mark currentMark) {
        this.currentMark = currentMark;
    }


    /**
     * Getter for a boats list of marks
     * @return the list of marks
     */
    ArrayList<Mark> getMarkList() {
        return this.markList;
    }


    /**
     * Setter for a boats list of marks
     * @param markList the list of marks
     */
    void setMarkList(ArrayList<Mark> markList) {
        this.markList = markList;
    }


    /**
     * A getter for the name of the boat
     * @return The boatName
     */
    String getBoatName() {
        return boatName;
    }


    /**
     * A getter for the heading of the boat
     * @return The heading of the boat
     */
    public int getHeading() {
        return heading;
    }


    /**
     * A setter for the heading of the boat
     * @param heading The value the boat heading will be set to
     */
    public void setHeading(int heading) {
        this.heading = heading;
    }

    /**
     * A setter for the name of the boat
     * @param name The name the boatName will be set to
     */
    public void setBoatName(String name) {
        this.boatName = name;
    }


    /**
     * A getter for the team name that the boat belongs to
     * @return The teamName
     */
    String getTeamName() {
        return teamName;
    }


    /**
     * Checks the value of the boolean finished
     * @return The value of finished
     */
    boolean isFinished() {
        return finished;
    }


    /**
     * A setter of the value of finished
     * @param finished the value finished will be set to
     */
    void setFinished(boolean finished) {
        this.finished = finished;
    }


    /**
     * A setter for the team name that the boat belongs to
     * @param name The name that the teamName variable will be set to
     */
    void setTeamName(String name) {
        this.teamName = name;
    }


    /**
     * A getter for the speed of the boat
     * @return The speed of the boat
     */
    double getSpeed() {
        return speed;
    }


    /**
     * A getter for the speed of the boat
     * @param speed the speed of the boat
     */
    void setSpeed(double speed) {
        this.speed = speed;
    }


    /**
     * returns the mark that the boat has most recently passed
     * @return the mark the boat has most recently passed
     */
    Mark getCurrentMark() {
        return currentMark;
    }


    /**
     * A getter for the position of the boat
     * @return the position the boat is from the start of the race
     */
    double getPosition() {
        return position;
    }


    /**
     * Sets the position that the boat is at
     * @param position The value position will be set to
     */
    void setPosition(double position) {
        this.position = position;
    }


    /**
     * A method to display where the boat currently is on the course
     */
    void viewPlaceOnCourse () {
        System.out.printf("%s -> Current mark: %s, Next mark: %s\n",
                boatName, currentMark.getMarkName(), nextMark.getMarkName());
    }


    /**
     * An overidden toString for the boat objects which displays all of the boats variables
     * @return A string showing all the boats variables
     */
    @Override
    public String toString() {
        return "Boat{" +
                "boatName='" + boatName + '\'' +
                ", teamName='" + teamName + '\'' +
                '}';
    }
}

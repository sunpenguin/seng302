package seng302;

import java.util.ArrayList;

/**
 * A class which represents a boat in the text based Application
 */

public class Boat {

    private String boatName;
    private String teamName;
    private int speed;
    private ArrayList<Mark> markList = new ArrayList<>();
    private Mark currentMark;
    private Mark nextMark;


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
     * A setter for the team name that the boat belongs to
     * @param name The name that the teamName variable will be set to
     */
    public void setTeamName(String name) {
        this.teamName = name;
    }


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

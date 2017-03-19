package seng302;

import java.util.ArrayList;

/**
 * A class which represents a boat in the text based Application
 */

public class Boat {

    private String boatName;
    private String teamName;
    private double speed;
//    private ArrayList<CompoundMark> compoundMarkList = new ArrayList<>();
//    private CompoundMark currentCompoundMark;
//    private CompoundMark nextCompoundMark;
    private Leg currentLeg;
    private Leg nextLeg;
    private double position;
    private boolean finished = false;
    private int heading;
    private Coordinate coordinate;
    private double distanceTravelled;



    /**
     * A constructor for the Boat class
     * @param boatName The name of the boat
     * @param teamName The name of the team the boat belongs to
     */
    public Boat(String boatName, String teamName) {
        this.boatName = boatName;
        this.teamName = teamName;
    }

    public Boat(String boatName, String teamName, double speed) {
        this.boatName = boatName;
        this.teamName = teamName;
        this.speed = speed;
    }

    public double getDistanceTravelled() {
        return distanceTravelled;
    }

    public void setDistanceTravelled(double distanceTravelled) {
        this.distanceTravelled = distanceTravelled;
    }


    //    /**
//     * Setter for the next CompoundMark the boat must pass
//     * @param nextCompoundMark The next CompoundMark to pass
//     */
//    public void setNextCompoundMark(CompoundMark nextCompoundMark) {
//        this.nextCompoundMark = nextCompoundMark;
//    }
//
//
//    /**
//     * Getter for the next CompoundMark the boat must pass
//     * @return the next CompoundMark to pass
//     */
//    public CompoundMark getNextCompoundMark() {
//        return nextCompoundMark;
//    }


//    /**
//     * Setter for the current CompoundMark.
//     * Called when a race is created to set each boat's current mark
//     * to the first mark on the course.
//     * @param currentCompoundMark the most recently passed CompoundMark.
//     */
//    public void setCurrentCompoundMark(CompoundMark currentCompoundMark) {
//        this.currentCompoundMark = currentCompoundMark;
//    }
//
//
//    /**
//     * Getter for the current CompoundMark.
//     * @return the most recently passed CompoundMark.
//     */
//    public CompoundMark getCurrentCompoundMark() {
//        return this.currentCompoundMark;
//    }

//    /**
//     * Getter for a boats list of marks
//     * @return the list of CompoundMarks
//     */
//    public ArrayList<CompoundMark> getCompoundMarkList() {
//        return this.compoundMarkList;
//    }
//
//
//    /**
//     * Setter for a boats list of marks
//     * @param compoundMarkList the list of CompoundMarks
//     */
//    public void setCompoundMarkList(ArrayList<CompoundMark> compoundMarkList) {
//        this.compoundMarkList = compoundMarkList;
//    }


    /**
     * A getter for the name of the boat
     * @return The boatName
     */
    public String getBoatName() {
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
    public String getTeamName() {
        return teamName;
    }


    /**
     * Checks the value of the boolean finished
     * @return The value of finished
     */
    public boolean isFinished() {
        return finished;
    }


    /**
     * A setter of the value of finished
     * @param finished the value finished will be set to
     */
    public void setFinished(boolean finished) {
        this.finished = finished;
    }


    /**
     * A setter for the team name that the boat belongs to
     * @param name The name that the teamName variable will be set to
     */
    public void setTeamName(String name) {
        this.teamName = name;
    }


    /**
     * A getter for the speed of the boat
     * @return The speed of the boat
     */
    public double getSpeed() {
        return speed;
    }


    /**
     * A setter for the speed of the boat
     * @param speed the speed of the boat
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }



    /**
     * A getter for the position of the boat
     * @return the position the boat is from the start of the race
     */
    public double getPosition() {
        return position;
    }


    /**
     * Sets the position that the boat is at
     * @param position The value position will be set to
     */
    public void setPosition(double position) {
        this.position = position;
    }


//    /**
//     * A method to display where the boat currently is on the course
//     */
//    public void viewPlaceOnCourse() {
//        System.out.printf("%s -> Current mark: %s, Next mark: %s\n",
//                boatName, currentCompoundMark.getName(), nextCompoundMark.getName());
//    }


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

    public Leg getNextLeg() {
        return nextLeg;
    }

    public void setNextLeg(Leg nextLeg) {
        this.nextLeg = nextLeg;
    }

    public Leg getCurrentLeg() {
        return currentLeg;
    }

    public void setCurrentLeg(Leg currentLeg) {
        this.currentLeg = currentLeg;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }
}

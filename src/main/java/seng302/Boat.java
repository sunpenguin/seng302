package seng302;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * A class which represents a boat in the text based Application
 */

public class Boat {

    private String boatName;
    private String teamName;
    private double speed;
    private Leg leg;
//    private double position;
    private double heading;
    private Coordinate coordinate;
    private Coordinate destination;
    private String place;

    /**
     * A constructor for the Boat class
     * @param boatName The name of the boat
     * @param teamName The name of the team the boat belongs to
     * @param speed The speed of the boat
     */
    public Boat(String boatName, String teamName, double speed) {
        this.boatName = boatName;
        this.teamName = teamName;
        this.speed = speed;
    }

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
    public double getHeading() {
        return heading;
    }


    /**
     * A setter for the heading of the boat
     * @param heading The value the boat heading will be set to
     */
    public void setHeading(double heading) {
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

//    /**
//     * A getter for the position of the boat
//     * @return the position the boat is from the start of the race
//     */
//    public double getPosition() {
//        return position;
//    }
//
//    /**
//     * Sets the position that the boat is at
//     * @param position The value position will be set to
//     */
//    public void setPosition(double position) {
//        this.position = position;
//    }


    public Leg getLeg() {
        return leg;
    }


    public void setLeg(Leg leg) {
        this.leg = leg;
    }


    public Coordinate getCoordinate() {
        return coordinate;
    }


    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }


    public Coordinate getDestination() {
        return destination;
    }


    public void setDestination(Coordinate destination) {
        this.destination = destination;
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

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

//    public String getPlace() {
//        if (place == null) {
//            place = new SimpleStringProperty();
//        }
//        return place.get();
//    }
//
//    public void setPlace(String place) {
//        this.place.setValue(place);
//    }
}

package seng302;

/**
 * A class which represents a boat in the text based Application
 */

public class Boat {

    private String boatName;
    private String teamName;
    private int speed;

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

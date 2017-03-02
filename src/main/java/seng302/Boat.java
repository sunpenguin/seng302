package seng302;

/**
 * A class which represents a boat in the text based Application
 */

public class Boat {

    private String boatName;
    private String teamName;
    private int speed;

    public Boat(String boatName, String teamName) {
        this.boatName = boatName;
        this.teamName = teamName;
    }

    String getBoatName() {
        return boatName;
    }

    public void setBoatName(String name) {
        this.boatName = name;
    }

    String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    @Override
    public String toString() {
        return "Boat{" +
                "boatName='" + boatName + '\'' +
                ", teamName='" + teamName + '\'' +
                '}';
    }
}

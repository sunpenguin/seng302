package seng302;

/**
 * A class which represents a boat in the text based Application
 */

public class Boat {

    private String name;

    public Boat(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Boat{" +
                "name='" + name + '\'' +
                '}';
    }
}

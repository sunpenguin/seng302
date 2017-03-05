package seng302;

import java.util.ArrayList;
import java.util.Collections;

/**
 *  A class that represents a mark on the race course
 */
public class Mark {

    private ArrayList<Boat> boats = new ArrayList<>();
    private String markName;


    /**
     * Constructor for the Mark class
     * @param markName The name of the mark
     */
    Mark(String markName) {
            this.markName = markName;
    }


    /**
     * A getter to return the variable boats from the Mark
     * @return The list of boats for the mark
     */
    public ArrayList<Boat> getBoats() {
            return boats;
        }


    /**
     * Setter the boats at the mark
     * @param boats Arraylist of boats at the mark
     */
    void setBoats(ArrayList<Boat> boats) {
            this.boats = boats;
    }


    /**
     * Getter for the mark name
     * @return the name of the mark
     */
    String getMarkName() {
            return markName;
    }


    /**
     * A setter for the name of the mark
     * @param markName The name that the mark will  be set to
     */
    public void setMarkName(String markName) {
            this.markName = markName;
        }


    /**
     * A method which displays the order the boats passed through the mark
     */
    void displayBoatOrder(){
        Collections.shuffle(boats);
        System.out.println(markName);

        for(int i = 0; i < boats.size(); i++){
            System.out.println(i+1 + ". " + boats.get(i).getTeamName() + " " + boats.get(i).getBoatName());
        }
    }
}


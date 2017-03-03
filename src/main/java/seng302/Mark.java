package seng302;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by csl62 on 3/03/17.
 */
public class Mark {


        private ArrayList<Boat> boats = new ArrayList<>();
        private String markName;

        public Mark(ArrayList<Boat> boats, String markName) {
            this.boats = boats;
            this.markName = markName;
        }


        public ArrayList<Boat> getBoats() {
            return boats;
        }

        public void setBoats(ArrayList<Boat> boats) {
            this.boats = boats;
        }

        public String getMarkName() {
            return markName;
        }

        public void setMarkName(String markName) {
            this.markName = markName;
        }

        void displayBoatOrder(){
            Collections.shuffle(boats);
            System.out.println(markName);
            for(int i = 0; i < boats.size(); i++){

                System.out.println(i+1 + ". " + boats.get(i).getTeamName() + " " + boats.get(i).getBoatName());
            }
        }
    }


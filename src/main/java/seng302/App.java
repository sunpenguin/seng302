package seng302;

import java.util.ArrayList;

public class App {

    /**
     * The main method for the application
     */
    public static void main(String[] args) {

        FileReader fileReader = new FileReader();
        ArrayList<Boat> ac35 = fileReader.readBoatListFile("testAc35_1.txt");

        System.out.println("-----------------------------COMPETITORS---------------------------------------");
        for (Boat boat : ac35) {
            System.out.printf("Boatname: %s, Teamname: %s\n", boat.getBoatName(), boat.getTeamName());
        }
        System.out.println("-------------------------------------------------------------------------------");

        Course testCourse = new Course("testCourse.txt");
        System.out.println("--------------------------COURSE DESCRIPTION-----------------------------------");
        testCourse.displayCourse();
        System.out.println("-------------------------------------------------------------------------------");

        Race race = new Race(ac35, testCourse);
        float speed = 40;

        System.out.println("----------------------------------STARTING---------------------------------------");
        for (Boat boat : ac35) {
            boat.viewPlaceOnCourse();
            boat.setSpeed(speed);
            speed += 5;
        }
        System.out.println("---------------------------------------------------------------------------------");

        race.runRace();

    }
}






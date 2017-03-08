package seng302;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class App {

    /**
     * The main method for the application
     */
    public static void main(String[] args) {

        boolean process = true;

        while(process) {
            try {
                Scanner sc = new Scanner(System.in);
                System.out.println("Please enter how long you would like the race to take in minutes");

                double scaledTime;
                double time = sc.nextDouble();
                scaledTime = 1 / time;

                FileReader fileReader = new FileReader();
                ArrayList<Boat> ac35 = fileReader.readBoatListFile("testAc35_1.txt");
                Course testCourse = new Course("testCourse.txt");
                Race race = new Race(ac35, testCourse);

                double speed = 0.539957 * 60 * scaledTime; // 1 km/h = 0.54 knots

                for (Boat boat : ac35) {
                    boat.setSpeed(speed);
                    speed += 0; // All boats set to same speed
                }

                System.out.println("-----------------------------STARTING LIST-------------------------------------");
                race.viewStartingList();
                System.out.println("-------------------------------------------------------------------------------");

                System.out.println("--------------------------COURSE DESCRIPTION-----------------------------------");
                testCourse.displayCourse();
                System.out.println("-------------------------------------------------------------------------------");

                race.runRace();
                process = false;
            } catch (InputMismatchException e){
                System.out.println("The input is invalid, please try again");
            }
        }
    }
}






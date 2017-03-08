package seng302;

import java.util.ArrayList;
import java.util.Collections;
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
                System.out.println("Please enter how long you would like the race to take in minutes:");

                double scaledTime;
                double time = sc.nextDouble();

                scaledTime = 1 / time;

                System.out.println("Please enter how many boats you would like to race:");
                int numBoats = sc.nextInt();

                FileReader fileReader = new FileReader();
                ArrayList<Boat> ac35 = fileReader.readBoatListFile("testAc35_1.txt");
                Collections.shuffle(ac35); // shuffle the list so that different boats race each time

                Course testCourse = new Course("testCourse.txt");

                ArrayList<Boat> raceList = new ArrayList<>(ac35.subList(0, numBoats));
                Race race = new Race(raceList, testCourse);

                double speed = 0.539957 * 60 * scaledTime; // 1 km/h = 0.54 knots

                for (Boat boat : raceList) {
                    boat.setSpeed(speed);
                    speed += 0; // All boats set to same speed
                }

                race.viewStartingList();
                testCourse.displayCourse();
                race.runRace();

                process = false;
            } catch (InputMismatchException e){
                System.out.println("The input is invalid, please try again");
            } catch (IndexOutOfBoundsException e){
                System.out.println("You have selected too many boats! :(");
            }
        }
    }
}






package seng302;

public class App 
{


    public static void main( String[] args )
    {
//        Boat boat1 = new Boat("Emirates", "New Zealand");
//        Boat boat2 = new Boat("Oracle" , "USA");
//        if(!(boat1.getBoatName().equals(boat2.getBoatName()))) {
//            System.out.println(boat1.getBoatName());
//            System.out.println(boat2.getBoatName());
//        }
//        else {
//            System.out.println("Error: The boats have the same name");
//        }

        Boat boat1 = new Boat("Emirates", "New Zealand");
        Boat boat2 = new Boat("Oracle" , "USA");

        Race raceTest = new Race();
        raceTest.addBoat(boat1);
        raceTest.addBoat(boat2);

        raceTest.viewBoats();
    }
}
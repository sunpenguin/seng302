package seng302;

import java.util.ArrayList;
import java.util.Collections;

public class App
{

    public static void main( String[] args )
    {

        ArrayList<Boat> ac35 = new ArrayList<>();
        Boat emirates = new Boat("Emirates", "New Zealand");
        Boat oracle = new Boat("Oracle" , "USA");
        Boat artemis = new Boat("Artemis", "Sweden");
        Boat groupama = new Boat("Groupama", "France");
        Boat landRover = new Boat("Land Rover", "Britain");
        Boat softBank = new Boat("SoftBank", "Japan");

        Race raceTest = new Race();
        ac35.add(emirates);
        ac35.add(oracle);
        ac35.add(artemis);
        ac35.add(groupama);
        ac35.add(landRover);
        ac35.add(softBank);
        Collections.shuffle(ac35);
        raceTest.addBoat(ac35.get(0));
        raceTest.addBoat(ac35.get(1));
        raceTest.viewFinishOrder();

    }
}
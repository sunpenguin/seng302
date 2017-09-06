package cucumber.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import seng302.team18.model.*;
import seng302.team18.model.updaters.MovementUpdater;
import seng302.team18.model.updaters.OutOfBoundsUpdater;
import seng302.team18.model.updaters.Updater;

import java.util.*;

/**
 * Created by dhl25 on 10/08/17.
 */
public class BoundaryDetection {

    private Boat boat;
    private Race race;
    private BoatStatus oldStatus = BoatStatus.PRE_START;
    private BoatStatus newStatus = BoatStatus.PRE_START;

    @Given("^a course$")
    public void a_course() throws Throwable {
        Coordinate boundary1 = new Coordinate(32.30502, -64.85857);
        Coordinate boundary2 = new Coordinate(32.30502, -64.85235);
        Coordinate boundary3 = new Coordinate(32.29925, -64.85235);
        Coordinate boundary4 = new Coordinate(32.29925, -64.85857);
        List<Coordinate> boundaries = new ArrayList<>();
        boundaries.add(boundary1);
        boundaries.add(boundary2);
        boundaries.add(boundary3);
        boundaries.add(boundary4);

        race = new Race();
        List<Updater> updaters = new ArrayList<>();
        updaters.add(new MovementUpdater());
        updaters.add(new OutOfBoundsUpdater());
        race.setUpdaters(updaters);
        Course course = new Course(getCompoundMarks(), boundaries, getRoundings());
        race.setCourse(course);
    }

    @Given("^a boat inside the courses bounds$")
    public void a_boat_inside_the_courses_bounds() throws Throwable {
        boat = new Boat("name", "shortName", 1, 1);
        boat.setCoordinate(new Coordinate(32.30463, -64.85245));
        boat.setStatus(oldStatus);
        race.addParticipant(boat);
    }

    @When("^the player stays inside the boundary$")
    public void the_player_stays_inside_the_boundary() throws Throwable {
        race.update(1);
    }

    @Then("^the players status will stay the same$")
    public void the_players_status_will_stay_the_same() throws Throwable {
        Assert.assertEquals(oldStatus, boat.getStatus());
    }

    @When("^the player moves outside the boundary$")
    public void the_player_moves_outside_the_boundary() throws Throwable {
        boat.setSailOut(false);
        boat.setSpeed(100000);
        race.update(999999999);
    }

    @Then("^the players status will be set to disqualified\\.$")
    public void the_players_status_will_be_set_to_disqualified() throws Throwable {
        Assert.assertEquals(BoatStatus.DSQ, boat.getStatus());
    }



    private List<CompoundMark> getCompoundMarks() {
        List<CompoundMark> compoundMarks = new ArrayList<>();

        // Start & Finish Line
        Mark mark1 = new Mark(231, new Coordinate(32.30269, -64.85787));
        mark1.setHullNumber("LC21");
        mark1.setStoweName("PRO");
        mark1.setShortName("S1");
        mark1.setBoatName("StartLine1");

        Mark mark2 = new Mark(232, new Coordinate(32.30159, -64.85847));
        mark2.setHullNumber("LC22");
        mark2.setStoweName("PIN");
        mark2.setShortName("S2");
        mark2.setBoatName("StartLine2");

        CompoundMark compoundMark0 = new CompoundMark("Start/Finish Line", Arrays.asList(mark1, mark2), 11);
        compoundMarks.add(compoundMark0);

        Mark mark3 = new Mark(233, new Coordinate(32.30182, -64.85397));
        mark3.setHullNumber("LC21");
        mark3.setStoweName("PRO");
        mark3.setShortName("S1");
        mark3.setBoatName("First Mark");

        CompoundMark compoundMark1 = new CompoundMark("First Mark", Arrays.asList(mark3), 12);
        compoundMarks.add(compoundMark1);

        Mark mark4 = new Mark(234, new Coordinate(32.30492, -64.85354));
        mark1.setHullNumber("LC21");
        mark1.setStoweName("PRO");
        mark1.setShortName("S1");
        mark1.setBoatName("Upwind1");

        Mark mark5 = new Mark(235, new Coordinate(32.30463, -64.85245));
        mark2.setHullNumber("LC22");
        mark2.setStoweName("PIN");
        mark2.setShortName("S2");
        mark2.setBoatName("Upwind2");

        CompoundMark compoundMark2 = new CompoundMark("Upwind", Arrays.asList(mark4, mark5), 13);
        compoundMarks.add(compoundMark2);

        Mark mark6 = new Mark(236, new Coordinate(32.29966, -64.85654));
        mark1.setHullNumber("LC21");
        mark1.setStoweName("PRO");
        mark1.setShortName("S1");
        mark1.setBoatName("Downwind1");

        Mark mark7 = new Mark(237, new Coordinate(32.29935, -64.85564));
        mark2.setHullNumber("LC22");
        mark2.setStoweName("PIN");
        mark2.setShortName("S2");
        mark2.setBoatName("Downwind2");


        CompoundMark compoundMark3 = new CompoundMark("Downwind", Arrays.asList(mark6, mark7), 14);
        compoundMarks.add(compoundMark3);
        return compoundMarks;
    }


    private List<MarkRounding> getRoundings() {
        List<MarkRounding> markRoundings = new ArrayList<>();

        markRoundings.add(new MarkRounding(1, getCompoundMarks().get(0), MarkRounding.Direction.SP, 3));
        markRoundings.add(new MarkRounding(2, getCompoundMarks().get(1), MarkRounding.Direction.PORT, 3));
        markRoundings.add(new MarkRounding(3, getCompoundMarks().get(2), MarkRounding.Direction.PS, 6));
        markRoundings.add(new MarkRounding(4, getCompoundMarks().get(3), MarkRounding.Direction.PS, 6));
        markRoundings.add(new MarkRounding(5, getCompoundMarks().get(0), MarkRounding.Direction.PS, 6));

        return markRoundings;
    }
}

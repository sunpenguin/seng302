package cucumber.steps;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import seng302.team18.message.PowerType;
import seng302.team18.model.*;
import seng302.team18.model.updaters.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Steps to run the projectile creation feature tests
 */
public class ProjectileCreation {

    private Race race;
    private Boat boat1;
    private Boat boat2;

    @Given("^a race with projectiles$")
    public void a_race_with_projectiles() throws Throwable {
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
        updaters.add(new BoatsUpdater());
        updaters.add(new ProjectileUpdater());
        updaters.add(new ProjectileHitUpdater());
        updaters.add(new OutOfBoundsUpdater());
        race.setUpdaters(updaters);
        Course course = new Course(getCompoundMarks(), boundaries, getRoundings());
        race.setCourse(course);
        race.setPositionSetter(new StartLineSetter(5));
    }

    @Given("^two boats$")
    public void two_boats() throws Throwable {
        boat1 = new Boat("boat name 1", "shortName1", 1, 14);
        boat1.setCoordinate(new Coordinate(32.30463, -64.85245));
        boat2 = new Boat("boat name 2", "shortName2", 2, 14);
        boat2.setCoordinate(new Coordinate(32.2999, -64.857));
        race.addParticipant(boat1);
        race.addParticipant(boat2);
    }

    @Given("^the first boat has a tiger shark power up$")
    public void the_first_boat_has_a_tiger_shark_power_up() throws Throwable {
       boat1.setPowerUp(new SharkPowerUp());
       boat1.setPowerActive(false);
    }

    @When("^the boat fires the projectile$")
    public void the_boat_fires_the_projectile() throws Throwable {
        boat1.activatePowerUp();
        race.update(20);
    }

    @When("^picks up another projectile$")
    public void picks_up_another_projectile() throws Throwable {
        boat1.setPowerUp(new SharkPowerUp());
        boat1.setPowerActive(false);
    }

    @When("^fires the second projectile$")
    public void fires_the_second_projectile() throws Throwable {
        boat1.activatePowerUp();
        race.update(20);
    }

    @Then("^the the first projectiles id will differ form the second projectiles id$")
    public void the_the_first_projectiles_id_will_differ_form_the_second_projectiles_id() throws Throwable {
        Assert.assertNotEquals(race.getProjectiles().get(0).getId(),race.getProjectiles().get(1).getId());
    }

    @When("^the projectile leave the boundaries$")
    public void the_projectile_leave_the_boundaries() throws Throwable {
        race.update(50000);
    }

    @Then("^the projectile will cease to exist$")
    public void the_projectile_will_cease_to_exist() throws Throwable {
        Assert.assertEquals(race.getProjectiles().size(), 0);
    }

    @When("^it hits a second boat$")
    public void it_hits_a_second_boat() throws Throwable {
        boat2.setCoordinate(new Coordinate(32.2999, -64.857));
        race.getProjectiles().get(0).setLocation(boat2.getCoordinate());
        race.update(0.000003);
    }

    @Then("^the second boat will be stunned$")
    public void the_second_boat_will_be_stunned() throws Throwable {
        Assert.assertEquals(boat2.getPowerUp().getType(), PowerType.STUN);
        Assert.assertEquals(boat2.isPowerActive(), true);
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


    private PickUp createPrototype() {
        BodyMass mass = new BodyMass();
        mass.setWeight(0);
        mass.setRadius(12);

        PickUp pickUp = new PickUp(-1);
        pickUp.setBodyMass(mass);

        return pickUp;
    }
}
package seng302.team18.racemodel.model;

import org.junit.Before;
import org.junit.Test;
import seng302.team18.model.Course;
import seng302.team18.model.Race;
import seng302.team18.model.RaceType;
import seng302.team18.model.RaceMode;
import seng302.team18.model.Regatta;
import seng302.team18.model.updaters.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AbstractRaceBuilderTest {
    private static final int RACE_ID = 1234;
    private static final RaceType RACE_TYPE = RaceType.MATCH;
    private static final Course COURSE = new Course();
    private static final Regatta REGATTA = new Regatta();

    private Race race;

    @Before
    public void setUp() throws Exception {
        AbstractRaceBuilder raceBuilder = new ConcreteRaceBuilder();
        race = raceBuilder.buildRace(new Race(), REGATTA, COURSE);
    }


    @Test
    public void buildRaceTest_raceId() throws Exception {
        assertEquals(RACE_ID, race.getId());
    }


    @Test
    public void buildRaceTest_raceType() throws Exception {
        assertEquals(RACE_TYPE, race.getRaceType());
    }


    @Test
    public void buildRaceTest_course() throws Exception {
        assertEquals(COURSE, race.getCourse());
    }


    @Test
    public void buildRaceTest_regatta() throws Exception {
        assertEquals(REGATTA, race.getRegatta());
    }


    private class ConcreteRaceBuilder extends AbstractRaceBuilder {
        @Override
        protected int getId() {
            return RACE_ID;
        }


        @Override
        protected RaceType getRaceType() {
            return RACE_TYPE;
        }

        @Override
        protected List<Updater> getUpdaters(){
            List<Updater> updaters = new ArrayList<>();
            updaters.add(new MovementUpdater());
            updaters.add(new CollisionUpdater());
            updaters.add(new OutOfBoundsUpdater());
            updaters.add(new MarkRoundingUpdater());

            return updaters;
        }

        @Override
        protected RaceMode getRaceMode(){
            return RaceMode.RACE;
        }

    }
}